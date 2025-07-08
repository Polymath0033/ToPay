package com.polymath.topay.exceptions;

import com.polymath.topay.dtos.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(CustomNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<Object>> handleCustomNotFound(CustomNotFound ex){
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.NOT_FOUND.value(),"not found",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CustomBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleCustomBadRequest(CustomBadRequest ex){
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(),"bad request",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CustomNotAuthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<Object>> handleCustomNotAuthorized(CustomNotAuthorized ex){
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.UNAUTHORIZED.value(),"not authorized",ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        String message = extractFriendlyMessage(ex);
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(),"bad request",message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse<Object>> handleHttpClientErrorExceptionForbidden(HttpClientErrorException.Forbidden ex){
        String message = extractFriendlyMessage(ex);
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.FORBIDDEN.value(),"forbidden",message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleSQLException(SQLException ex){
        String message = extractFriendlyMessage(ex);
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(),"bad request",message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(err->errors.put(err.getPropertyPath().toString(),err.getMessage()));
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(),errors,"bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err->errors.put(err.getField(),err.getDefaultMessage()));
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(),errors,"bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        String message = "Invalid request body or malformed JSON. Please check your input and try again.";
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(),message,"bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        String message = String.format("Missing required parameter '%s'.",ex.getParameterName());
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(),message,"bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        String message = String.format("Invalid value '%s' for field '%s'.",ex.getValue(),ex.getName());
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(),message,"bad request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ApiResponse<Object>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex){
        String message = String.format("Method '%s' is not allowed for this request.",ex.getMethod());
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.METHOD_NOT_ALLOWED.value(),message,"method not allowed");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<Object>> handleNoHandlerFoundException(NoHandlerFoundException ex){
        String message = String.format("Endpoint '%s' not found",ex.getRequestURL());
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.NOT_FOUND.value(),message,"not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex){
        ApiResponse<Object> response = ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),"internal server error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private String extractFriendlyMessage(Exception ex){
        String message = ex.getMessage();
        if(message==null){
            return "A database error occurred.";
        }

        if(message.contains("duplicate")){
            return extractDuplicateKeyMessage(message);
        }

        if(message.contains("foreign key constraint")){
            return "Cannot perform this operation due to existing related data";
        }

        if(message.contains("not-null property references a null or transient value")){
            return "Require field cannot be null";
        }

        if (message.contains("check constraint")) {
            return "Invalid data: constraint validation failed";
        }

        return "Invalid data provided. Please check your input and try again.";
    }
    private String extractDuplicateKeyMessage(String errorMessage){
        Pattern pattern = Pattern.compile("Key \\(([^)]+)\\)=\\(([^)]+)\\) already exists");
        Matcher matcher = pattern.matcher(errorMessage);
        if(matcher.find()){
            String fieldName = matcher.group(1);
            String value = matcher.group(2);
            return String.format("A record with %s '%s' already exists.",fieldName,value);
        }
        return "A record with the information already exists";
     }
}
