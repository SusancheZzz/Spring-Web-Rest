package com.rntgroup.impl.controller.advice;

import static com.rntgroup.impl.controller.advice.ErrorCode.BUSINESS_ERROR;
import static com.rntgroup.impl.controller.advice.ErrorCode.ENTITY_NOT_FOUND;
import static com.rntgroup.impl.controller.advice.ErrorCode.INTERNAL_ERROR;
import static com.rntgroup.impl.controller.advice.ErrorCode.VALIDATE_FAILED;

import com.rntgroup.impl.exception.DepartmentStillHasEmployeesException;
import com.rntgroup.impl.exception.DepartmentWithNotFoundException;
import com.rntgroup.impl.exception.PaymentNotValidException;
import com.rntgroup.impl.exception.UniqueAttributeAlreadyExistException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmployeeRestControllerAdvice {

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

  @ExceptionHandler(DepartmentWithNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleDepartmentWithNotFoundException(
    DepartmentWithNotFoundException ex) {
    return new ErrorResponse(ENTITY_NOT_FOUND, ex.getMessage(), LocalDateTime.now());
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

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
    List<ErrorResponse> errors = new ArrayList<>();
    ex.getBindingResult()
      .getAllErrors()
      .forEach(error -> {
          String fieldName = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          errors.add(new ErrorResponse(
              VALIDATE_FAILED,
              "Validate fail: %s: %s".formatted(fieldName, errorMessage),
              LocalDateTime.now()
            )
          );
        }
    );
    return errors;
  }
}
