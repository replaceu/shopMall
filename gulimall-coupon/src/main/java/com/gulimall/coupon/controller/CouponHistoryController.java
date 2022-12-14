package com.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gulimall.common.utils.R;
import com.gulimall.coupon.entity.CouponHistoryEntity;
import com.gulimall.coupon.service.CouponHistoryService;
import com.gulimall.service.utils.PageUtils;

/**
 * 优惠券领取历史记录
 *
 * @author aqiang9
 * @email 2903780002@qq.com
 * @date 2020-06-09 09:23:34
 */
@RestController
@RequestMapping("coupon/couponhistory")
public class CouponHistoryController {
	@Autowired
	private CouponHistoryService couponHistoryService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = couponHistoryService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		CouponHistoryEntity couponHistory = couponHistoryService.getById(id);

		return R.ok().put("couponHistory", couponHistory);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody CouponHistoryEntity couponHistory) {
		couponHistoryService.save(couponHistory);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody CouponHistoryEntity couponHistory) {
		couponHistoryService.updateById(couponHistory);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids) {
		couponHistoryService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}
