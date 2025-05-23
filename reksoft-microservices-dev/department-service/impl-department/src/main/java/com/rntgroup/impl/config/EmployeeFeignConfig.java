package com.rntgroup.impl.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeeFeignConfig {

  @Bean(name = "employeeFeignLogger")
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}
