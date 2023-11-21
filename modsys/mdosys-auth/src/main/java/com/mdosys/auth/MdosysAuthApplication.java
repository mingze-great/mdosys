package com.mdosys.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.mdosys.common.security.annotation.EnableMdosysFeignClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权中心
 *
 * @author ruoyi
 */
@EnableMdosysFeignClients
//@EnableFeignClients("com.mdosys.system.api")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MdosysAuthApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MdosysAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  __  __     _                            \n" +
                " |  \\/  |   | |                          \n" +
                " | \\  / | __| | ___  ___ _   _ ___       \n" +
                " | |\\/| |/ _` |/ _ \\/ __| | | / __|     \n" +
                " | |  | | (_| | (_) \\__ \\ |_| \\__ \\   \n" +
                " |_|  |_|\\__,_|\\___/|___/\\__, |___/    \n" +
                "                          __/ |           \n" +
                "                         |___/            ");
    }
}

