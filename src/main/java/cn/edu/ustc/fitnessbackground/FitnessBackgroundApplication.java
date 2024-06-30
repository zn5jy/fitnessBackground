package cn.edu.ustc.fitnessbackground;

import cn.edu.ustc.fitnessbackground.common.ServiceContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FitnessBackgroundApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FitnessBackgroundApplication.class, args);
        ServiceContext.setApplicationContext(applicationContext);
    }

}
