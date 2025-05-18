package com.rntgroup.impl.client;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.impl.client.fallback.DepartmentClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
  value = "department-service",
  fallbackFactory = DepartmentClientFallbackFactory.class,
  dismiss404 = true
)
public interface DepartmentClient {

  @GetMapping("/api/v1/department/{id}")
  ResponseEntity<DepartmentMessageDto> findDepartmentById(@PathVariable("id") Long id);
}
