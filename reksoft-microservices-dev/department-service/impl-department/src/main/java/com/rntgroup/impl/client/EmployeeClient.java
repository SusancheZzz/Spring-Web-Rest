package com.rntgroup.impl.client;

import com.rntgroup.api.controller.CommunicatorWithDepartmentServiceApi;
import com.rntgroup.impl.client.fallback.EmployeeClientFallbackFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
  value = "employee-service",
  fallbackFactory = EmployeeClientFallbackFactory.class
)
@CircuitBreaker(name = "default")
public interface EmployeeClient extends CommunicatorWithDepartmentServiceApi {

}
