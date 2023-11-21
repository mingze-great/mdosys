package com.mdosys.project;

import com.mdosys.common.security.annotation.EnableCustomConfig;
import com.mdosys.common.security.annotation.EnableMdosysFeignClients;
import com.mdosys.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 系统模块
 *
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableTransactionManagement
//@EnableMdosysFeignClients
@SpringBootApplication
public class MdosysProjectApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MdosysProjectApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ 项目管理模块启动成功  ლ(´ڡ`ლ)ﾞ  \n" +
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