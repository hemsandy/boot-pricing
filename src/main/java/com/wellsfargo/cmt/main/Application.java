package com.wellsfargo.cmt.main;

import com.wellsfargo.cmt.jms.PriceTickSubscriber;
import com.wellsfargo.cmt.util.AppProperties;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
@ComponentScan(basePackages={"com.wellsfargo.cmt"})
@EnableConfigurationProperties(AppProperties.class)
public class Application {

   
    @Autowired
    AppProperties appProperties;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
