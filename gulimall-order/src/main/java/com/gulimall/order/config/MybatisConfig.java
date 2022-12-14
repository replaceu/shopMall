package com.gulimall.order.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**
*@author aqiang9 2020-07-30
*/
@Configuration
@EnableTransactionManagement //开启事务
@MapperScan("com.gulimall.order.dao")
public class MybatisConfig {
	//引入分页插件
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		//请求大于最大页后回到首页
		paginationInterceptor.setOverflow(true);
		paginationInterceptor.setDbType(DbType.MYSQL);
		//页面最大数据量-1不受限制
		paginationInterceptor.setLimit(200);
		return paginationInterceptor;
	}

	/**
	*自动填充时间的配置
	*/
	@Bean
	public MetaObjectHandler metaObjectHandler() {
		return new MetaObjectHandler() {
			@Override
			public void insertFill(MetaObject metaObject) {
				this.setFieldValByName("create_time", new Date(), metaObject);
				this.setFieldValByName("update_time", new Date(), metaObject);
			}

			@Override
			public void updateFill(MetaObject metaObject) {
				this.setFieldValByName("update_time", new Date(), metaObject);
			}
		};
	}

}
