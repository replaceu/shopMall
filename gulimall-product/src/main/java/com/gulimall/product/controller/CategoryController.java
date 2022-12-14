package com.gulimall.product.controller;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.gulimall.common.constant.SwaggerParamType;
import com.gulimall.common.utils.CommonResult;
import com.gulimall.common.valid.AddGroup;
import com.gulimall.product.convert.CategoryConvert;
import com.gulimall.product.entity.CategoryEntity;
import com.gulimall.product.service.CategoryService;
import com.gulimall.product.vo.CategoryVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
*商品三级分类
*
*/
@RestController
@RequestMapping("product/category")
@Api(value = "商品分类", tags = { "商品分类-catrgory" })
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	/**
	*获取所有三级分类
	*/
	@ApiOperation(value = "获取所有分类以及子分类,返回json树形结构")
	@GetMapping("/list/tree")
	public CommonResult<List<CategoryVo>> list() {
		List<CategoryVo> categoryVo = categoryService.listCategoryWithTree();
		return CommonResult.ok(categoryVo);
	}

	/**
	*通过分类id获取详细信息
	*/
	@ApiOperation("通过分类id获取详细信息")
	@ApiImplicitParam(name = "categoryId", value = "品牌id", required = true, dataTypeClass = Long.class, paramType = SwaggerParamType.PATH)
	@GetMapping("/info/{categoryId}")
	public CommonResult<CategoryVo> categoryInfo(@PathVariable Long categoryId) {
		CategoryEntity categoryEntity = categoryService.getById(categoryId);
		CategoryVo categoryVo = CategoryConvert.INSTANCE.entity2vo(categoryEntity);
		return CommonResult.ok(categoryVo);
	}

	/**
	*菜单修改
	*/

	@ApiOperation("修改分类")
	@PutMapping("/update")
	public CommonResult<String> update(@RequestBody CategoryVo categoryVo) {
		categoryService.updateCategoryDetail(categoryVo);
		return CommonResult.ok("菜单 修改成功");
	}

	/**
	*保存分类
	*/

	@ApiOperation("新增分类")
	@PostMapping("/save")
	public CommonResult<String> save(@RequestBody @Validated({ AddGroup.class }) CategoryVo category) {
		//数据转换
		CategoryEntity categoryEntity = CategoryConvert.INSTANCE.vo2entity(category);
		categoryService.save(categoryEntity);

		return CommonResult.ok(category.getName() + " 菜单 保存成功");
	}

	/**
	*修改排序
	*/
	@ApiOperation("修改菜单排序")
	@ApiImplicitParam(value = "categories", dataTypeClass = List.class)
	@PutMapping("/update/sort")
	public CommonResult updateSort(@RequestBody List<CategoryEntity> categories) {
		categoryService.updateBatchById(categories);
		return CommonResult.ok("修改成功");
	}

	/**
	*批量删除分类
	*/
	@ApiOperation(value = "批量删除分类")
	@ApiImplicitParam(value = "catIds", dataTypeClass = List.class, required = true)
	@DeleteMapping("/delete")
	public CommonResult delete(@RequestBody @Validated @NotEmpty(message = "没有需要删除的数据") List<Long> catIds) {
		categoryService.removeMenuByIds(catIds);
		return CommonResult.ok("批量删除成功");
	}
}
