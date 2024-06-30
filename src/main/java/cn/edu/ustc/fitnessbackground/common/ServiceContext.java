package cn.edu.ustc.fitnessbackground.common;

import org.springframework.context.ApplicationContext;

/**
 * @ClassName : Context  //类名
 * @Description :   //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/27  15:48
 */
public class ServiceContext {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ServiceContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
