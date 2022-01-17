package com.sbapp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintStream;

@RestController
@SpringBootApplication(exclude = {ActiveMQAutoConfiguration.class})
public class SbappApplication {
    private static final Logger log = LoggerFactory.getLogger(SbappApplication.class);
    private MyAppProperties props;


    public SbappApplication(MyAppProperties props) {
        this.props = props;
    }

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(SbappApplication.class);

        app.setBanner(new Banner() {
            @Override
            public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
                out.print("\n\n\tThis is my own banner!\n\n".toUpperCase());
            }
        });

        app.run(args);


        /*
        // Можно подключать прослушиватели для некоторых из событий ApplicationEvent

        Logger log = LoggerFactory.getLogger(SbappApplication.class);
        new SpringApplicationBuilder(SbappApplication.class)
                .listeners(new ApplicationListener<ApplicationEvent>() {
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
                        log.info("#### > " + event.getClass().getCanonicalName());
                    }
                })
                .run(args);

         */

    }

    @Value("${message}")
    String info;

    @Value("${myapp.server-ip}")
    String serverIp;

    @Bean
    CommandLineRunner myMethod() {
        return args -> {
            log.info("##-> CommandLineRunner Implementation");
            log.info("Accessing the Info bean: {}", info);
            log.info(" > The Server IP is: " + serverIp);
            log.info(" > App Name: " + props.getName());
            log.info(" > App Info: " + props.getDescription());
            for (String arg : args) {
                log.info(arg);
            }
        };
    }

    @Component
    @ConfigurationProperties(prefix = "myapp")
    @Data
    public static class MyAppProperties {
        private String name;
        private String description;
        private String serverIp;
    }


    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

}
