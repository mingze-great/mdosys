package com.mdosys.file;

import com.mdosys.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Mdosys文件服务
 *
 * @author ruoyi
 */
@EnableCustomSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MdosysFileApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(MdosysFileApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Mdosys文件服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
