package com.sbapp;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Configuration
@Slf4j
public class AppConfig implements WebMvcConfigurer {

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm";

    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    public static LocalDateTime atStartOfDayOrMin(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime : MIN_DATE;
    }

    public static LocalDateTime atStartOfDayOrMax(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime : MAX_DATE;
    }

// Подключим TCP сервер H2 DB  для того, чтобы редактировать базу на лету в Idea
    /*
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2server() throws SQLException {
        log.info("Start H2 TCP Server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
 */

    /*
    //https://stackoverflow.com/questions/40274353/how-to-use-localdatetime-requestparam-in-spring-i-get-failed-to-convert-string
    Spring/Spring Boot only supports the date/date-time format in BODY parameters.
 */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);

    }


//if we want to use Java 8 date types and set a default date format,
// then we need to look at creating a Jackson2ObjectMapperBuilderCustomizer bean: https://www.baeldung.com/spring-boot-formatting-json-dates

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jsonCustomizer(){
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        };
    }

}
