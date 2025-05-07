package com.rntgroup.api.exception;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String BUSINESS_ERROR = "BUSINESS_ERROR";
  private static final String ENTITY_NOT_FOUND = "ENTITY_NOT_FOUND";
  private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
    return new ErrorResponse(ENTITY_NOT_FOUND, ex.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler(PaymentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handlePaymentNotValidException(PaymentNotValidException ex) {
    return new ErrorResponse(BUSINESS_ERROR, ex.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler(DepartmentStillHasEmployeesException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleDepartmentStillHasEmployeesException(
    DepartmentStillHasEmployeesException ex) {
    return new ErrorResponse(BUSINESS_ERROR, ex.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler(UniqueAttributeAlreadyExistException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleUniqueAttributeAlreadyExistException(
    UniqueAttributeAlreadyExistException ex) {
    return new ErrorResponse(BUSINESS_ERROR, ex.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleOtherException(Exception ex) {
    return new ErrorResponse(INTERNAL_ERROR, "Internal Server Error: "
      + ex.getMessage(), LocalDateTime.now());
  }
}
