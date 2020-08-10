package com.thoughtworks.rslist.configuration;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.RsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RsListAppConfiguration {
    /*@Bean
    public RsController rsController(RsEventRepository rsEventRepository, UserRepository userRepository) {
        return new RsController(rsService(rsEventRepository, userRepository));
    }*/

    @Bean
    public RsService rsService(RsEventRepository rsEventRepository, UserRepository userRepository) {
        return new RsService(rsEventRepository, userRepository);
    }
}
