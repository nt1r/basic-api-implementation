package com.thoughtworks.rslist.configuration;

import com.thoughtworks.rslist.service.RsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RsListAppConfiguration {
    @Bean
    public RsService rsService() {
        return new RsService();
    }
}
