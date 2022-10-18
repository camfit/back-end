package kr.hs.dgsw.camfit.exception.controller;

import kr.hs.dgsw.camfit.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    //400
    @ExceptionHandler({
            DuplicateNameException.class,
            FileFailedException.class,
            WrongDateException.class,
            WrongIdException.class,
            WrongMemberException.class
    })
    public ResponseEntity<Object> BadRequestException(final RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    //500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
