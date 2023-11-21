package com.mdosys.system;

import com.mdosys.common.security.annotation.EnableCustomConfig;
import com.mdosys.common.security.annotation.EnableMdosysFeignClients;
import com.mdosys.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统模块
 * 
 * @author
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableMdosysFeignClients
@SpringBootApplication
public class MdosysSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MdosysSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
