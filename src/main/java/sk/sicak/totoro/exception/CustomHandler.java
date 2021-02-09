package sk.sicak.totoro.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import sk.sicak.totoro.model.Message;

import java.util.Map;

@RestControllerAdvice
public class CustomHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleStatusException(ResponseStatusException ex){
        logger.info("{} status with message [{}]", ex.getStatus(), ex.getReason());
        return ResponseEntity.status(ex.getStatus()).body(new Message(ex.getReason()));
    }


}
