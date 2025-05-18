package com.rntgroup.impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.rntgroup.impl.client")
@SpringBootApplication
@EnableScheduling
public class DepartmentApi {

  public static void main(String[] args) {
    SpringApplication.run(DepartmentApi.class, args);
  }
}
