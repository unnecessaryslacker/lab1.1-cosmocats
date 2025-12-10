package com.cosmocats.market.web;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ProblemDetail onInvalidBody(MethodArgumentNotValidException ex) {
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            "Request body validation failed"
    );
    pd.setType(URI.create("https://api.cosmocats/errors/validation"));
    pd.setTitle("Bad Request");
    pd.setProperty(
            "errors",
            ex.getBindingResult().getFieldErrors().stream()
                    .map(fe -> Map.of(
                            "field", fe.getField(),
                            "rejectedValue", Optional.ofNullable(fe.getRejectedValue()).orElse("null"),
                            "message", fe.getDefaultMessage()
                    ))
                    .toList()
    );
    return pd;
  }

  @ExceptionHandler({BindException.class, ConstraintViolationException.class})
  ProblemDetail onBind(Exception ex) {
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
    );
    pd.setType(URI.create("https://api.cosmocats/errors/validation"));
    pd.setTitle("Bad Request");
    return pd;
  }

  @ExceptionHandler(NoSuchElementException.class)
  ProblemDetail notFound(NoSuchElementException ex) {
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
    );
    pd.setType(URI.create("https://api.cosmocats/errors/not-found"));
    pd.setTitle("Not Found");
    pd.setProperty("message", ex.getMessage());
    return pd;
  }

  @ExceptionHandler(NoResourceFoundException.class)
  ProblemDetail notFoundPath(NoResourceFoundException ex) {
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            "No such path"
    );
    pd.setType(URI.create("https://api.cosmocats/errors/not-found"));
    pd.setTitle("Not Found");
    return pd;
  }

  @ExceptionHandler(Exception.class)
  ProblemDetail generic(Exception ex) {
    ProblemDetail pd = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected error"
    );
    pd.setType(URI.create("https://api.cosmocats/errors/internal"));
    pd.setTitle("Internal Server Error");
    return pd;
  }
}
