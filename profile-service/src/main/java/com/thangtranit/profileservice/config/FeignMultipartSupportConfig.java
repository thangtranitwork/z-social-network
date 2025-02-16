package com.thangtranit.profileservice.config;

import feign.form.spring.SpringFormEncoder;
import feign.codec.Encoder;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class FeignMultipartSupportConfig {
    @Bean
    public Encoder multipartFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(HttpMessageConverters::new));
    }
}
