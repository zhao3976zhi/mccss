package com.cypro.ascpay.provider;

import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>非web应用程序SpringBoot启动</p>
 * @Type AscpayProviderApplication.java
 * @author Ascme
 * @date 2019年1月17日 凌晨00:52:12
 * @version 1.0
 */
@EnableTransactionManagement  //事务自动扫描
@SpringBootApplication//springboot启动
//@EnableCaching // 启动缓存
@ImportResource({"classpath:applicationContext-dubbox-provider.xml"})//注入文件
@ComponentScan(basePackages={"com.cypro.ascpay"})// 用于扫描指定包下的类
@MapperScan("com.cypro.ascpay.provider.*")
// 获取启动类，加载配置，确定装载 Spring 程序的装载方法，它回去寻找 主配置启动类（被 @SpringBootApplication 注解的）
@SpringBootTest
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class AscpayProviderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        // 不占用端口启动
        new SpringApplicationBuilder(AscpayProviderApplication.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("-----------------------provider启动成功--------------------------------");
    }
}
