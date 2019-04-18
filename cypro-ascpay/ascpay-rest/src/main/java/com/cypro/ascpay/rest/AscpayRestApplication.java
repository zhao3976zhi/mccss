/**
 *  Project: RfidRestApplication
 * File Created at 2017年9月25日
 * 
 * Copyright 2017 ZGRCJT Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZGRCJT Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license.
 */
package com.cypro.ascpay.rest;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Type RfidRestApplication.java
 * @Desc 
 * @author
 * @date
 * @version 
 */
@EnableTransactionManagement  //事务自动扫描
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableCaching // 启动缓存
@ImportResource({"classpath:applicationContext-dubbox-consumer.xml"})
@ComponentScan(basePackages={"com.cypro.ascpay.rest"})
@EnableScheduling //开启定时任务
@ServletComponentScan//开启过滤器配置
public class AscpayRestApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(AscpayRestApplication.class, args);
    }
    
    /**
     * 1.需要先定义一个convert 转换消息的对象
     * 2.添加fastJson的配置信息,比如，是否需要格式化返回的json数据
     * 3.在convert中添加配置信息
     * 4.将convert添加到converters当中
     * @param converters
     */
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        super.configureMessageConverters(converters);
        //1.需要先定义一个convert 转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2.添加fastJson的配置信息,比如，是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat
        );
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        //3.在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
       // 4.将convert添加到converters当中
        converters.add(fastConverter);

    }
    
    /**
     * @Author
     * @Description //TODO 初始化拦截器
     * @Date
     * @Param []
     * @return
     **/
   /* @Bean
    public LoginCheckTokenInterceptor userInterceptor() {
        return new LoginCheckTokenInterceptor();
    }
*/
    /**
     * interceptor配置
     */
    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
  	  registry.addInterceptor(userInterceptor())
          //添加需要验证登录用户操作权限的请求
        .addPathPatterns("/**")
          //排除不需要验证登录用户操作权限的请求
        .excludePathPatterns("/css/**")
        .excludePathPatterns("/js/**")
        .excludePathPatterns("/images/**")
        .excludePathPatterns("/error")
          // app登录不拦截
        .excludePathPatterns("/driverapp/login")
    }*/

}