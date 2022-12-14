package com.gulimall.product.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gulimall.product.entity.CategoryEntity;
import com.gulimall.product.vo.Category2Vo;
import com.gulimall.product.vo.CategoryVo;
import com.gulimall.service.utils.PageUtils;

/**
*商品三级分类
*
*@authoraqiang9
*@email2903780002@qq.com
*@date2020-06-1011:26:28
*/
public interface CategoryService extends IService<CategoryEntity> {
	PageUtils queryPage(Map<String, Object> params);

	/**
	*查询所有的分类，并组装成父子结构
	*
	*@return
	*/
	List<CategoryVo> listCategoryWithTree();

	/**
	*通过菜单id批量删除菜单
	*
	*@param catIds
	*/
	void removeMenuByIds(List<Long> catIds);

	/**
	*获取此三级分类的完整路径
	*
	*@return List<Long>完整路径
	*/
	List<Long> findCategoryPath(Long categoryId);

	/**
	*修改详细信息并级联更新
	*/
	void updateCategoryDetail(CategoryVo categoryVo);

	/**
	*获取一级分类
	*@return List<CategoryEntity>
	*/
	List<CategoryEntity> getCategoryLevel1();

	/**
	*获取所有三级分类并封装成一个map
	*@return Map<String,Object>
	*/
	Map<String, List<Category2Vo>> getCategoryJson();

}
