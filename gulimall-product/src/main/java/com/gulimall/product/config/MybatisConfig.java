package com.gulimall.product.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author aqiang9  2020-07-30
 */
@Configuration
@EnableTransactionManagement // 开启事务
@MapperScan("com.gulimall.product.dao")

public class MybatisConfig {
    // 引入分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 请求大于最大页后 回到首页
        paginationInterceptor.setOverflow(true) ;
        paginationInterceptor.setDbType(DbType.MYSQL);
//        页面最大数据量  -1 不受限制
        paginationInterceptor.setLimit(200) ;
        return paginationInterceptor ;
    }


}
