package net.atos.usrmanagementservice.config;

import net.atos.usrmanagementservice.mappers.toUserSignUpDto;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public toUserSignUpDto toUserSignUpDto() {
        return Mappers.getMapper(toUserSignUpDto.class);
    }
}