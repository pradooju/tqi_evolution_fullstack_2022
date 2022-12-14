package br.com.tqi.bootcamp.bookstore.exception;

import br.com.tqi.bootcamp.bookstore.api.response.error.ErrorDetailResponse;
import br.com.tqi.bootcamp.bookstore.api.response.error.ErrorMessageResponse;
import br.com.tqi.bootcamp.bookstore.api.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class BookExceptionHandler {

    @ExceptionHandler(APIBusinessException.class)
    public ResponseEntity<ErrorResponse> businessHandler(APIBusinessException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> validationHandler(HttpMessageNotReadableException ex) {
        log.info("Validation error={}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorMessageResponse> validationHandler(BindException ex) {
        List<ErrorDetailResponse> detailList = ex.getFieldErrors().stream().map(ErrorDetailResponse::new).collect(Collectors.toList());
        log.info("Validation error={}", detailList);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponse(detailList));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> genericExceptionHandler(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Internal server error"));
    }
}
