package com.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gulimall.common.utils.R;
import com.gulimall.coupon.entity.MemberPriceEntity;
import com.gulimall.coupon.service.MemberPriceService;
import com.gulimall.service.utils.PageUtils;

/**
 * 商品会员价格
 *
 * @author aqiang9
 * @email 2903780002@qq.com
 * @date 2020-06-09 09:23:34
 */
@RestController
@RequestMapping("coupon/memberprice")
public class MemberPriceController {
	@Autowired
	private MemberPriceService memberPriceService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = memberPriceService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		MemberPriceEntity memberPrice = memberPriceService.getById(id);

		return R.ok().put("memberPrice", memberPrice);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody MemberPriceEntity memberPrice) {
		memberPriceService.save(memberPrice);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody MemberPriceEntity memberPrice) {
		memberPriceService.updateById(memberPrice);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids) {
		memberPriceService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}
