package com.gulimall.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gulimall.common.utils.CommonResult;
import com.gulimall.common.vo.AttrRespVo;

/**
 * @author Carter  2020-08-22
 */
@FeignClient("gulimall-product")
@RequestMapping("/product")
public interface ProductFeignService {
	/**
	 *
	 * @param attrId 属性id
	 * @return 属性的基本信息
	 */
	@PostMapping("/attr/info/{attrId}")
	CommonResult<AttrRespVo> info(@PathVariable("attrId") Long attrId);
}
