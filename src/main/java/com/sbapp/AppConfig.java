package com.sbapp;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
public class AppConfig {

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm";

// Подключим TCP сервер H2 DB  для того, чтобы редактировать базу на лету в Idea
    /*
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2server() throws SQLException {
        log.info("Start H2 TCP Server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

     */
//if we want to use Java 8 date types and set a default date format, then we need to look at creating a Jackson2ObjectMapperBuilderCustomizer bean: https://www.baeldung.com/spring-boot-formatting-json-dates
    @Bean
    Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(){
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }


}
