package com.gulimall.secondKill.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gulimall.common.constant.ICommonConstants;
import com.gulimall.common.exception.BizException;
import com.gulimall.common.to.mq.SecondKillOrderTo;
import com.gulimall.common.utils.R;
import com.gulimall.common.vo.MemberRespVo;
import com.gulimall.secondKill.feign.CouponFeignService;
import com.gulimall.secondKill.feign.ProductFeignService;
import com.gulimall.secondKill.interceptor.LoginUserInterceptor;
import com.gulimall.secondKill.service.SecondKillService;
import com.gulimall.secondKill.to.SecondKillSkuRedisTo;
import com.gulimall.secondKill.vo.SecondKillSessionWithSkus;
import com.gulimall.secondKill.vo.SkuInfoVo;

@Service
public class SecondKillServiceImpl implements SecondKillService {
	@Autowired
	StringRedisTemplate	redisTemplate;
	@Autowired
	CouponFeignService	couponFeignService;
	@Autowired
	ProductFeignService	productFeignService;
	@Autowired
	RedissonClient		redissonClient;
	@Autowired
	RabbitTemplate		rabbitTemplate;

	private final String SESSIONS_CACHE_PREFIX = "secondKill:sessions:";

	private final String SKUKILL_CACHE_PREFIX = "secondKill:skus:";

	private final String SKU_STOCK_SEMAPHORE = "secondKill:stock:";

	@Override
	public void uploadSecondKillSkuLatest3Days() {
		//1.?????????????????????????????????????????????
		R lasts3DaySession = couponFeignService.getLasts3DaySession();
		if (lasts3DaySession.getCode() == 0) {
			List<SecondKillSessionWithSkus> data = lasts3DaySession.getData(new TypeReference<List<SecondKillSessionWithSkus>>() {
			});
			//2.?????????????????????Redis
			saveSessionInfosToRedis(data);
			//3.??????????????????????????????Redis
			saveSessionSkuInfosToRedis(data);
		}
	}

	/**
	 * ???????????????Redis????????????????????????????????????
	 * @return
	 */
	@Override
	public List<SecondKillSkuRedisTo> getSecondKillSkuList() {

		Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
		long currentTime = System.currentTimeMillis();

		for (String key : keys) {
			String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
			String[] split = replace.split("_");

			long startTime = Long.parseLong(split[0]);
			long endTime = Long.parseLong(split[1]);

			//??????????????????????????????
			if (currentTime > startTime && currentTime < endTime) {
				//?????????????????????????????????????????????
				List<String> range = redisTemplate.opsForList().range(key, -100, 100);
				BoundHashOperations<String, String, String> hashOperations = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
				//???????????????????????????
				assert range != null;
				List<String> strings = hashOperations.multiGet(range);
				if (!CollectionUtils.isEmpty(strings)) { return strings.stream().map(e -> JSON.parseObject(e, SecondKillSkuRedisTo.class)).collect(Collectors.toList()); }
			}
			break;
		}
		return null;
	}

	@Override
	public SecondKillSkuRedisTo getSkuSecondKillInfo(Long skuId) {
		//1.???????????????????????????????????????key
		BoundHashOperations<String, String, String> hashOperations = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
		Set<String> keys = hashOperations.keys();
		if (null != keys) {
			String regx = "\\d_" + skuId;
			for (String key : keys) {
				if (Pattern.matches(regx, key)) {
					String json = hashOperations.get(key);
					SecondKillSkuRedisTo skuRedisTo = JSON.parseObject(json, SecondKillSkuRedisTo.class);
					//?????????
					long current = new Date().getTime();
					if (current >= skuRedisTo.getStartTime().getTime() && current <= skuRedisTo.getEndTime().getTime()) {
					} else {
						skuRedisTo.setRandomCode(null);
					}
					return skuRedisTo;
				}
			}
		}
		return null;
	}

	/**
	 * ?????????????????????????????????????????????
	 * @param killId
	 * @param key
	 * @param num
	 * @return
	 */
	public String mallUserDoSecondKill(String killId, String key, Integer num) {
		//??????????????????????????????
		MemberRespVo currentUser = LoginUserInterceptor.loginUser.get();
		//todo:?????????????????????????????????
		BoundHashOperations<String, String, String> hashOperations = redisTemplate.boundHashOps("SKUKILL_CACHE_PREFIX");
		String json = hashOperations.get(killId);
		if (!StringUtils.isEmpty(json)) {
			SecondKillSkuRedisTo skuRedisTo = JSON.parseObject(json, SecondKillSkuRedisTo.class);
			//todo:???????????????
			Long startTime = skuRedisTo.getStartTime().getTime();
			long endTime = skuRedisTo.getEndTime().getTime();
			long currentTime = new Date().getTime();
			long ttl = endTime - startTime;
			//todo:????????????????????????
			if (currentTime >= startTime && currentTime <= endTime) {
				//todo:????????????????????????id
				String skuRandomCode = skuRedisTo.getRandomCode();
				String skuId = skuRedisTo.getPromotionSessionId() + "_" + skuRedisTo.getSkuInfo().getSkuId();
				if (skuRandomCode.equals(key) && killId.equals(skuId)) {
					//todo:????????????????????????
					if (num <= skuRedisTo.getRelationEntities().get(0).getSecondKillLimit()) {
						//todo:????????????????????????
						String userRedisKey = currentUser.getId() + "_" + skuId;
						Boolean isLock = redisTemplate.opsForValue().setIfAbsent(userRedisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
						if (isLock) {
							//todo:???????????????????????????????????????????????????????????????????????????????????????????????????
							RSemaphore skuStockSemaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE);
							boolean acquire = skuStockSemaphore.tryAcquire(num);
							if (acquire) {
								//todo:?????????????????????????????????MQ??????
								String orderSnTimeId = IdWorker.getTimeId();
								SecondKillOrderTo orderTo = new SecondKillOrderTo();
								orderTo.setMemberId(currentUser.getId());
								orderTo.setOrderSn(orderSnTimeId);
								orderTo.setSkuId(skuRedisTo.getSkuInfo().getSkuId());
								orderTo.setPromotionSessionId(skuRedisTo.getPromotionSessionId());
								orderTo.setNum(num);
								orderTo.setSecondKillPrice(skuRedisTo.getRelationEntities().get(0).getSecondKillPrice());
								rabbitTemplate.convertAndSend("order-event-exchange", "order.secondKill.order", orderTo);
								return orderSnTimeId;
							} else {
								throw new BizException(ICommonConstants.RspCode.secondKillErr);
							}

						} else {
							throw new BizException(ICommonConstants.RspCode.userUnLockErr);
						}
					} else {
						throw new BizException(ICommonConstants.RspCode.userSecondKillNumErr);
					}
				} else {
					throw new BizException(ICommonConstants.RspCode.secondKillErr);
				}
			} else {
				throw new BizException(ICommonConstants.RspCode.secondKillTimeUnMatchErr);
			}
		} else {
			throw new BizException(ICommonConstants.RspCode.secondKillSkuOntExistErr);
		}
	}

	private void saveSessionSkuInfosToRedis(List<SecondKillSessionWithSkus> sessions) {
		//??????hash??????
		BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);

		sessions.stream().forEach(e -> {
			e.getSecondKillSkuRelationEntities().stream().forEach(secondKillSkuVo -> {
				//????????????
				SecondKillSkuRedisTo secondKillSkuRedisTo = new SecondKillSkuRedisTo();
				//1.sku???????????????
				R skuInfo = productFeignService.getSkuInfo(secondKillSkuVo.getSkuId());
				if (skuInfo != null) {
					SkuInfoVo data = skuInfo.getData(new TypeReference<SkuInfoVo>() {
					});
					secondKillSkuRedisTo.setSkuInfo(data);
				}
				//2.sku???????????????
				BeanUtils.copyProperties(secondKillSkuVo, secondKillSkuRedisTo);
				//3.???????????????????????????????????????
				secondKillSkuRedisTo.setStartTime(e.getStartTime());
				secondKillSkuRedisTo.setEndTime(e.getEndTime());
				//4.???????????????
				String token = UUID.randomUUID().toString().replace("_", "");
				secondKillSkuRedisTo.setRandomCode(token);
				//todo:5.????????????????????????????????????????????????????????????
				RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
				semaphore.trySetPermits(secondKillSkuVo.getSecondKillCount());

				String jsonString = JSON.toJSONString(secondKillSkuRedisTo);
				hashOperations.put(secondKillSkuVo.getSkuId(), jsonString);

			});
		});
	}

	/**
	 * ???redis???????????????????????????
	 * @param sessions
	 */
	private void saveSessionInfosToRedis(List<SecondKillSessionWithSkus> sessions) {
		if (!CollectionUtils.isEmpty(sessions)) {
			sessions.stream().forEach(session -> {
				long startTime = session.getStartTime().getTime();
				long endTime = session.getEndTime().getTime();
				String key = SESSIONS_CACHE_PREFIX + startTime + "_" + endTime;
				Boolean hasKey = redisTemplate.hasKey(key);
				if (!hasKey) {
					List<String> collect = session.getSecondKillSkuRelationEntities().stream().map(e -> {
						return e.getPromotionId().toString() + "_" + e.getSkuId().toString();
					}).collect(Collectors.toList());

					//??????????????????
					redisTemplate.opsForList().leftPushAll(key, collect);
				}

			});
		}
	}
}
