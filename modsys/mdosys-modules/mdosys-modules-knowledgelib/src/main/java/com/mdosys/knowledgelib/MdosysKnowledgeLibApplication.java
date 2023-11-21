package com.mdosys.knowledgelib;

import com.mdosys.common.security.annotation.EnableCustomConfig;
import com.mdosys.common.security.annotation.EnableMdosysFeignClients;
import com.mdosys.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * 知识库服务
 *
 * @author mdosys
 */
@EnableCustomSwagger2
@EnableMdosysFeignClients
@EnableCustomConfig
@SpringBootApplication
public class MdosysKnowledgeLibApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MdosysKnowledgeLibApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Mdosys知识库模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
