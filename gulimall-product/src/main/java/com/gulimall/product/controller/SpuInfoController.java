package com.gulimall.product.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gulimall.common.utils.CommonResult;
import com.gulimall.product.entity.SpuInfoEntity;
import com.gulimall.product.service.SpuInfoService;
import com.gulimall.product.vo.SpuPageVo;
import com.gulimall.product.vo.SpuSaveVo;
import com.gulimall.service.utils.PageUtils;

/**
 * spu信息
 *
 * @author aqiang9
 * @email 2903780002@qq.com
 * @date 2020-06-10 11:26:28
 */
@RestController
@RequestMapping("/product/spuinfo")
public class SpuInfoController {
	@Autowired
	private SpuInfoService spuInfoService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public CommonResult list(SpuPageVo params) {
		PageUtils page = spuInfoService.queryPageOnCondition(params);
		return CommonResult.ok().data(page);
	}

	/**
	 * 商品上架
	 *
	 * @param spuId 需要上架的id
	 * @return
	 */
	@PutMapping("/{spuId}/up")
	public CommonResult up(@PathVariable Long spuId) {
		spuInfoService.up(spuId);
		return CommonResult.ok();
	}

	@PostMapping("/{spuId}/up")
	public CommonResult upProduct(@PathVariable Long spuId) {
		spuInfoService.upProduct(spuId);
		return CommonResult.ok();
	}

	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	public CommonResult<SpuInfoEntity> info(@PathVariable("id") Long id) {
		SpuInfoEntity spuInfo = spuInfoService.getById(id);
		return CommonResult.ok(spuInfo);
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public CommonResult save(@RequestBody SpuSaveVo spuSaveVo, HttpServletRequest request) {
		spuInfoService.saveInfo(spuSaveVo);
		return CommonResult.ok();
	}

	/**
	 * 修改
	 */
	@PutMapping("/update")
	public CommonResult update(@RequestBody SpuInfoEntity spuInfo) {
		spuInfoService.updateById(spuInfo);

		return CommonResult.ok();
	}

	/**
	 * 删除
	 */
	@DeleteMapping("/delete")
	public CommonResult delete(@RequestBody Long[] ids) {
		spuInfoService.removeByIds(Arrays.asList(ids));

		return CommonResult.ok();
	}

}
