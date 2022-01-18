package com.sbapp;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
@Slf4j
public class AppConfig {

// Подключим TCP сервер H2 DB  для того, чтобы редактировать базу на лету в Idea
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2server() throws SQLException {
        log.info("Start H2 TCP Server");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }


}
