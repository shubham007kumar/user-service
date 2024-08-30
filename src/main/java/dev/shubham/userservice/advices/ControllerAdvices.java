package dev.shubham.userservice.advices;

import dev.shubham.userservice.dtos.ErrorDto;
import dev.shubham.userservice.exceptions.UserPresentException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvices {

    @ExceptionHandler(UserPresentException.class)
    public ResponseEntity<ErrorDto> productFound(UserPresentException exception){
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(exception.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatusCode.valueOf(400));
    }
}
