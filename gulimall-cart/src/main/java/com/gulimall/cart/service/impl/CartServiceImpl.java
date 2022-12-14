package com.gulimall.cart.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.gulimall.cart.constant.CartConstant;
import com.gulimall.cart.feign.ProductFeignService;
import com.gulimall.cart.interceptor.CartInterceptor;
import com.gulimall.cart.service.CartService;
import com.gulimall.cart.to.UserInfoTo;
import com.gulimall.cart.vo.CartItemVo;
import com.gulimall.cart.vo.CartVo;
import com.gulimall.common.to.SkuInfoTo;
import com.gulimall.common.utils.CommonResult;

/**
*@author aqiang9 2020-09-08
*/
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	ThreadPoolExecutor executor;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	ProductFeignService productFeignService;

	@Override
	public CartItemVo addItemUserShoppingCart(Long skuId, Integer num) {
		//判断是否有此数据
		BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
		CartItemVo cartItemVo = cartOps.get(skuId.toString());

		if (cartItemVo != null) {
			//有此数据修改
			cartItemVo.setCount(cartItemVo.getCount() + num);
		} else {
			//查询当前要添加的商品信息
			cartItemVo = getItemInfo(skuId, num);
			System.out.println(cartItemVo == null);
		}

		//更新数据
		cartOps.put(skuId.toString(), cartItemVo);
		//重置数量
		cartItemVo.setCount(num);
		return cartItemVo;
	}

	@Override
	public CartItemVo getCartItemInfo(Long skuId) {
		BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
		return cartOps.get(skuId.toString());
	}

	@Override
	public CartVo getCartList() {
		CartVo cartVo = new CartVo();
		//1、登陆
		UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
		BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
		if (userInfoTo.getUserId() != null) {
			//cart ops是已登录的ops
			String tempCartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
			BoundHashOperations<String, String, CartItemVo> tempCartOps = redisTemplate.boundHashOps(tempCartKey);
			List<CartItemVo> tempCartItems = tempCartOps.values();
			if (tempCartItems != null && tempCartItems.size() > 0) {
				//需要合并
				for (CartItemVo tempCartItem : tempCartItems) {
					CartItemVo cartItemVo = cartOps.get(tempCartItem.getSkuId().toString());
					if (cartItemVo == null) {
						cartOps.put(tempCartItem.getSkuId().toString(), tempCartItem);
					} else {
						cartItemVo.setCount(tempCartItem.getCount() + cartItemVo.getCount());
						cartOps.put(tempCartItem.getSkuId().toString(), cartItemVo);
					}
				}
			}
			//清空购物车
			clearCart(tempCartKey);

			cartVo.setItems(cartOps.values());
		} else {
			List<CartItemVo> values = getCartOps().values();
			cartVo.setItems(values);
		}
		//未登录
		return cartVo;
	}

	@Override
	public CartVo getMallUserCartList() {
		CartVo cartVo = new CartVo();
		//1、登陆
		UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
		BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
		if (userInfoTo.getUserId() != null) {
			//cart ops是已登录的ops
			String tempCartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
			BoundHashOperations<String, String, CartItemVo> tempCartOps = redisTemplate.boundHashOps(tempCartKey);
			List<CartItemVo> tempCartItems = tempCartOps.values();
			if (tempCartItems != null && tempCartItems.size() > 0) {
				//需要合并
				for (CartItemVo tempCartItem : tempCartItems) {
					CartItemVo cartItemVo = cartOps.get(tempCartItem.getSkuId().toString());
					if (cartItemVo == null) {
						cartOps.put(tempCartItem.getSkuId().toString(), tempCartItem);
					} else {
						cartItemVo.setCount(tempCartItem.getCount() + cartItemVo.getCount());
						cartOps.put(tempCartItem.getSkuId().toString(), cartItemVo);
					}
				}
			}
			//清空购物车
			clearCart(tempCartKey);

			cartVo.setItems(cartOps.values());
		} else {
			List<CartItemVo> values = getCartOps().values();
			cartVo.setItems(values);
		}
		//未登录
		return cartVo;
	}

	@Override
	public void clearCart() {

	}

	@Override
	public void checkUserShoppingCart(Long skuId, int check) {
		CartItemVo cartItemInfo = getCartItemInfo(skuId);
		cartItemInfo.setCheck(check == 1);
		BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
		cartOps.put(cartItemInfo.getSkuId().toString(), cartItemInfo);
	}

	@Override
	public void changeUserShoppingCartNum(Long skuId, int num) {
		CartItemVo cartItemInfo = getCartItemInfo(skuId);
		cartItemInfo.setCount(num);
		BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();
		cartOps.put(cartItemInfo.getSkuId().toString(), cartItemInfo);
	}

	@Override
	public void deleteItem(Long skuId) {
		BoundHashOperations<String, String, CartItemVo> cartOps = getCartOps();

		cartOps.delete(skuId.toString());
	}

	//这是在Redis里面操作
	@Override
	public List<CartItemVo> getCurrentUserCartItems() {

		UserInfoTo currentUser = CartInterceptor.threadLocal.get();
		if (currentUser == null) {
			return null;
		} else {
			String cartKey = CartConstant.CART_PREFIX + currentUser.getUserId();
			List<CartItemVo> cartItems = getCartItems(cartKey);
			List<CartItemVo> retList = cartItems.stream().filter(e -> {
				return e.getCheck() == true;
			}).map(e -> {
				//todo:将价格更新成最新的价格
				e.setPrice(productFeignService.getPrice(e.getSkuId()));
				return e;
			}).collect(Collectors.toList());

			return retList;
		}
	}

	private List<CartItemVo> getCartItems(String cartKey) {
		BoundHashOperations<String, Object, CartItemVo> operations = redisTemplate.boundHashOps(cartKey);
		List<CartItemVo> cartItems = operations.values();

		return cartItems;
	}

	public void clearCart(String cartKey) {
		redisTemplate.delete(cartKey);
	}

	//查询当前要添加的商品信息
	private CartItemVo getItemInfo(Long skuId, Integer num) {
		CartItemVo cartItemVo = new CartItemVo();
		//基本信息
		CompletableFuture<Void> skuInfoTask = CompletableFuture.runAsync(() -> {
			CommonResult<SkuInfoTo> skuInfo = productFeignService.getSkuInfo(skuId);
			if (skuInfo.isOk()) {
				SkuInfoTo skuInfoTo = skuInfo.getData();
				cartItemVo.setSkuId(skuInfoTo.getSkuId());
				cartItemVo.setTitle(skuInfoTo.getSkuTitle());
				cartItemVo.setCount(num);
				cartItemVo.setPrice(skuInfoTo.getPrice());
				cartItemVo.setImage(skuInfoTo.getSkuDefaultImg());
				System.out.println(skuInfo);
			}
		}, executor);
		//属性组合信息
		CompletableFuture<Void> saleAttrTask = CompletableFuture.runAsync(() -> {
			List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
			cartItemVo.setSkuAttr(skuSaleAttrValues);
			System.out.println(skuSaleAttrValues);
		}, executor);

		CompletableFuture.allOf(saleAttrTask, skuInfoTask);

		try {
			saleAttrTask.get();
			skuInfoTask.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return cartItemVo;
	}

	private BoundHashOperations<String, String, CartItemVo> getCartOps() {
		UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
		String cartKey = "";

		if (userInfoTo.getUserId() == null) {
			//临时用户
			cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
		} else {
			cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
		}
		return redisTemplate.boundHashOps(cartKey);
	}

}
