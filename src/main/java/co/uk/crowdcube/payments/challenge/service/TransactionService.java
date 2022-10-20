package co.uk.crowdcube.payments.challenge.service;

import co.uk.crowdcube.payments.challenge.dto.TransactionDTO;
import co.uk.crowdcube.payments.challenge.entity.Transaction;
import co.uk.crowdcube.payments.challenge.exception.InvalidInformationException;
import co.uk.crowdcube.payments.challenge.repository.PaymentRepository;
import co.uk.crowdcube.payments.challenge.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static co.uk.crowdcube.payments.challenge.enums.TransactionStatus.REFUNDED;
import static java.util.UUID.randomUUID;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final ObjectMapper objectMapper;
    private final PaymentRepository paymentRepository;

    @Autowired
    public TransactionService(final TransactionRepository repository, final ObjectMapper objectMapper,
                              final PaymentRepository paymentRepository) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Return the transaction by its id.
     * @param id the payment id
     * @return the payment by its id
     */
    public TransactionDTO getTransactionById(final Long id) {
        var transaction = repository.findById(id).orElse(null);
        return objectMapper.convertValue(transaction, TransactionDTO.class);
    }

    /**
     * Returns all transactions.
     * @return all transactions
     */
    public List<TransactionDTO> getAllTransactions() {
        var transactions = repository.findAll();
        return objectMapper.convertValue(transactions, List.class);
    }

    /**
     * Creates a new payment entry.
     * @param payload the payment entry
     * @return PaymentDTO
     * @throws InvalidInformationException to be thrown
     */
    public TransactionDTO createTransaction(final TransactionDTO payload) throws InvalidInformationException {
        validateTransaction(payload);

        var payment = paymentRepository.findById(payload.getPaymentId()).orElse(null);
        if (payment == null) {
            log.error("Error creating transaction: payment method not found!");
            throw new InvalidInformationException("Error creating transaction: payment method not found!");
        }

        var transaction = objectMapper.convertValue(payload, Transaction.class);
        transaction.setPayment(payment);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionToken(randomUUID().toString());

        transaction = repository.save(transaction);

        var transactionDTO = objectMapper.convertValue(transaction, TransactionDTO.class);
        transactionDTO.setPaymentId(null);
        return transactionDTO;
    }

    /**
     * Refunds a transaction.
     * @param id the transaction id
     * @return transaction refunded
     * @throws InvalidInformationException to be thrown
     */
    public TransactionDTO refundTransaction(final Long id) throws InvalidInformationException {
        var transaction = repository.findById(id).orElse(null);
        if (transaction == null) {
            log.error("Error refund transaction!: transaction with ID "+id+ " not found!");
            throw new InvalidInformationException("Error refund transaction!: transaction with ID "+id+ " not found!");
        }
        transaction.setStatus(REFUNDED);
        transaction = repository.save(transaction);

        return objectMapper.convertValue(transaction, TransactionDTO.class);
    }

    /**
     * Validates a new transaction entry.
     * @param transaction the transaction entry
     * @throws InvalidInformationException to be thrown
     */
    private void validateTransaction(final TransactionDTO transaction) throws InvalidInformationException {
        if (transaction.getPaymentId() == null) {
            log.error("Error creating transaction: Payment type is not selected!");
            throw new InvalidInformationException("Error creating transaction: Payment type is not selected!");
        }
        if (transaction.getDescription() == null || transaction.getDescription().equals("")) {
            log.error("Error creating transaction: Description cannot be empty!");
            throw new InvalidInformationException("Error creating transaction: Description cannot be empty!");
        }
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.error("Error creating transaction: Transaction amount cannot be less than 0!");
            throw new InvalidInformationException("Error creating transaction: Transaction amount cannot be less than 0!");
        }
    }
}
