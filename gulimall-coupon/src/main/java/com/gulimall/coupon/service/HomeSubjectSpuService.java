package com.gulimall.coupon.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gulimall.coupon.entity.HomeSubjectSpuEntity;
import com.gulimall.service.utils.PageUtils;

/**
 * 专题商品
 *
 * @author Carter
 * @email 2903780002@qq.com
 * @date 2020-06-09 09:23:34
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
