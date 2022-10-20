package co.uk.crowdcube.payments.challenge.controller;

import co.uk.crowdcube.payments.challenge.dto.TransactionDTO;
import co.uk.crowdcube.payments.challenge.exception.InvalidInformationException;
import co.uk.crowdcube.payments.challenge.exception.TransactionException;
import co.uk.crowdcube.payments.challenge.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> get(@PathVariable(value = "id") final Long id) {
        return ResponseEntity.ok(service.getTransactionById(id));
    }

    @PostMapping(value = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> save(@RequestBody final TransactionDTO transactionDTO) throws TransactionException {
        try {
            return ResponseEntity.ok(service.createTransaction(transactionDTO));
        } catch (final InvalidInformationException e) {
            throw new TransactionException(e);
        }
    }

    @PutMapping(value = "/refund/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> refund(@PathVariable(value = "id") final Long id) throws TransactionException {
        try {
            return ResponseEntity.ok(service.refundTransaction(id));
        } catch (final InvalidInformationException e) {
            throw new TransactionException(e);
        }
    }
}
