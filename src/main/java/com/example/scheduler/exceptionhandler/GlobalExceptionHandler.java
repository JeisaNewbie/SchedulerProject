package com.example.scheduler.exceptionhandler;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("ERROR", ex.getReason());
        response.put("STATUS",  ex.getStatusCode());
        response.put("TIMESTAMP", LocalDateTime.now());

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {

        Map<String, Object> response = new HashMap<>();

        response.put("ERROR", ex.getMessage());
        response.put("TIMESTAMP", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    //RequestParams 과 PathVariable 에서 검증되는 값에 대한 예외를 처리
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> response = new HashMap<>();


        /*
        * HandlerMethodValidationException 는 1개 이상의 검증이 필요한 값이 존재할 경우 모든 검증이 필요한 값을 검증한 후에 검증에 통과하지 못한 값들을 List 로 반환한다.
        * 따라서 정확한 에러메세지 처리 방법은 반환된 List 의 길이만큼을 순회하여 검증에 실패한 값의 getDefaultMessage(message = "" 로 초기화 한 값) 를 추출해야 하지만
        * 여기서는 하나의 값(Long id) 만 검증하기 때문에 ex.getAllErrors().get(0).getDefaultMessage() 로 간단하게 처리했다.
        */

        response.put("ERROR", ex.getAllErrors().get(0).getDefaultMessage());
        response.put("TIMESTAMP", LocalDateTime.now());
        response.put("STATUS", status);

        return ResponseEntity.status(status).body(response);
    }

    //RequestBody 에서 검증되는 값에 대한 예외를 처리
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, Object> response = new HashMap<>();

        response.put("ERROR", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.put("TIMESTAMP", LocalDateTime.now());
        response.put("STATUS", status);

        return ResponseEntity.status(status).body(response);
    }
}
