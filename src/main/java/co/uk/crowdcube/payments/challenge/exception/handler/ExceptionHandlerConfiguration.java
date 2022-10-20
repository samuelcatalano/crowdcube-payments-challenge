package co.uk.crowdcube.payments.challenge.exception.handler;

import co.uk.crowdcube.payments.challenge.exception.PaymentException;
import co.uk.crowdcube.payments.challenge.exception.TransactionException;
import co.uk.crowdcube.payments.challenge.json.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerConfiguration {

    @ExceptionHandler(value = {PaymentException.class})
    public ResponseEntity<Object> handleInvalidCreditCardException(final PaymentException e) {
        var error = new ErrorMessage().message(e.getCause().getMessage()).status(BAD_REQUEST.name())
                                                   .code(BAD_REQUEST.value());
        log.error(e.getCause().getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(error);
    }

    @ExceptionHandler(value = {TransactionException.class})
    public ResponseEntity<Object> handleInvalidCreditCardException(final TransactionException e) {
        var error = new ErrorMessage().message(e.getCause().getMessage()).status(BAD_REQUEST.name())
                .code(BAD_REQUEST.value());
        log.error(e.getCause().getMessage());
        return ResponseEntity.status(BAD_REQUEST).contentType(APPLICATION_JSON).body(error);
    }
}