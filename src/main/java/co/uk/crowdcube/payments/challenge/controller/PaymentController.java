package co.uk.crowdcube.payments.challenge.controller;

import co.uk.crowdcube.payments.challenge.dto.PaymentDTO;
import co.uk.crowdcube.payments.challenge.exception.InvalidInformationException;
import co.uk.crowdcube.payments.challenge.exception.PaymentException;
import co.uk.crowdcube.payments.challenge.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/payments")
public class PaymentController {

    private final PaymentService service;

    @Autowired
    public PaymentController(final PaymentService service) {
        this.service = service;
    }

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(service.getAllPayments());
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> get(@PathVariable(value = "id") final Long id) {
        return ResponseEntity.ok(service.getPaymentById(id));
    }

    @PostMapping(value = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> save(@RequestBody final PaymentDTO paymentDTO) throws PaymentException {
        try {
            return ResponseEntity.ok(service.createPayment(paymentDTO));
        } catch (final InvalidInformationException e) {
            throw new PaymentException(e);
        }
    }

    @PutMapping(value = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@RequestBody final PaymentDTO paymentDTO) throws PaymentException {
        try {
            return ResponseEntity.ok(service.updatePayment(paymentDTO));
        } catch (final InvalidInformationException e) {
            throw new PaymentException(e);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") final Long id) throws PaymentException {
        try {
            return ResponseEntity.ok(service.deletePayment(id));
        } catch (InvalidInformationException e) {
            throw new PaymentException(e);
        }
    }
}
