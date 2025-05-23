package com.rntgroup.impl.client;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.impl.client.fallback.DepartmentClientFallbackFactory;
import com.rntgroup.impl.config.DepartmentFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
  value = "department-service",
  fallbackFactory = DepartmentClientFallbackFactory.class,
  configuration = DepartmentFeignConfig.class
)
public interface DepartmentClient {

  @GetMapping("/api/v1/department/{id}")
  ResponseEntity<DepartmentMessageDto> findDepartmentById(@PathVariable("id") Long id);
}
