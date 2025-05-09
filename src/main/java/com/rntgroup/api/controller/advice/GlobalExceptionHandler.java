package com.rntgroup.api.controller.advice;

import static com.rntgroup.api.controller.advice.ErrorCode.ALREADY_EXISTS;
import static com.rntgroup.api.controller.advice.ErrorCode.BUSINESS_ERROR;
import static com.rntgroup.api.controller.advice.ErrorCode.ENTITY_NOT_FOUND;
import static com.rntgroup.api.controller.advice.ErrorCode.FORBIDDEN_RESOURCE;
import static com.rntgroup.api.controller.advice.ErrorCode.INTERNAL_ERROR;
import static com.rntgroup.api.controller.advice.ErrorCode.USER_NOT_FOUND;

import com.rntgroup.api.exception.AccountWithAlreadyExistsException;
import com.rntgroup.api.exception.DepartmentStillHasEmployeesException;
import com.rntgroup.api.exception.PaymentNotValidException;
import com.rntgroup.api.exception.UniqueAttributeAlreadyExistException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleEntityNotFoundException(EntityNotFoundException ex) {
    return new ErrorResponse(ENTITY_NOT_FOUND, ex.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler(AccountWithAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleAccountWithAlreadyExistsException(
    AccountWithAlreadyExistsException ex) {
    return new ErrorResponse(ALREADY_EXISTS, ex.getMessage(), LocalDateTime.now());
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

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleBadCredentialsException(
    BadCredentialsException ex) {
    return new ErrorResponse(
      USER_NOT_FOUND, ex.getMessage()
      + ": Incorrect login or password",
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorResponse handleAccessDeniedException(
    AccessDeniedException ex) {
    return new ErrorResponse(
      FORBIDDEN_RESOURCE, ex.getMessage()
      + ": Insufficient permissions to perform this operation",
      LocalDateTime.now()
    );
  }
}
