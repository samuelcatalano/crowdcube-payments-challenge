package co.uk.crowdcube.payments.challenge.service;

import co.uk.crowdcube.payments.challenge.dto.PaymentDTO;
import co.uk.crowdcube.payments.challenge.entity.Payment;
import co.uk.crowdcube.payments.challenge.exception.InvalidInformationException;
import co.uk.crowdcube.payments.challenge.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.valueOf;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final ObjectMapper objectMapper;

    @Value("${card.date.expiration.regex}")
    private String expirationRegex;

    @Value("${card.security.code.regex}")
    private String securityCodeRegex;

    @Autowired
    public PaymentService(final PaymentRepository repository, final ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    /**
     * Return the payment by its id.
     * @param id the payment id
     * @return the payment by its id
     */
    public PaymentDTO getPaymentById(final Long id) {
        var payment = repository.findById(id).orElse(null);
        return objectMapper.convertValue(payment, PaymentDTO.class);
    }

    /**
     * Returns all payments.
     * @return all payments
     */
    public List<PaymentDTO> getAllPayments() {
        var payments = repository.findAll();
        return objectMapper.convertValue(payments, List.class);
    }

    /**
     * Creates a new payment entry.
     * @param payload the payment entry
     * @return PaymentDTO
     * @throws InvalidInformationException to be thrown
     */
    public PaymentDTO createPayment(final PaymentDTO payload) throws InvalidInformationException {
        validateCardPayment(payload);
        var payment = objectMapper.convertValue(payload, Payment.class);
        payment = repository.save(payment);
        return objectMapper.convertValue(payment, PaymentDTO.class);
    }

    /**
     * Updates an existent payment entry.
     * @param payload the payment entry
     * @return PaymentDTO
     * @throws InvalidInformationException to be thrown
     */
    public PaymentDTO updatePayment(final PaymentDTO payload) throws InvalidInformationException {
        if (payload.getId() == null) {
            log.error("Error updating payment!: ID null");
            throw new InvalidInformationException("Error updating payment!: ID null");
        }
        validateCardPayment(payload);
        var payment = objectMapper.convertValue(payload, Payment.class);
        payment = repository.save(payment);
        return objectMapper.convertValue(payment, PaymentDTO.class);
    }

    /**
     * Deletes an existent payment by its id.
     * @param id the payment id
     * @return message
     */
    public String deletePayment(final Long id) throws InvalidInformationException {
        try {
            repository.deleteById(id);
            return "Payment deleted successfully!";
        } catch (EmptyResultDataAccessException e) {
            log.error("Error deleting payment!: entity ID " +id+ " does not exist in Database");
            throw new InvalidInformationException("Error deleting payment!: entity ID " +id+ " does not exist in Database");
        }
    }

    /**
     * Validates a new payment entry.
     * @param payment the payment entry
     * @throws InvalidInformationException to be thrown
     */
    private void validateCardPayment(final PaymentDTO payment) throws InvalidInformationException {
        if (payment.getNameOnCard() == null || payment.getNameOnCard().equals("")) {
            log.error("Error validating card info: Name on card couldn't be blank!");
            throw new InvalidInformationException("Error validating card info: Name on card couldn't be blank!");
        }
        if (valueOf(payment.getCardNumber()).length() < 16 || valueOf(payment.getCardNumber()).length() > 19) {
            log.error("Error validating card info: Card number is not 16-19 digits!");
            throw new InvalidInformationException("Error validating card info: Card number is not 16-19 digits!");
        }
        if (!payment.getExpiration().matches(expirationRegex)) {
            log.error("Error validating card info: Expiration date has to be [1-12]/YYYY");
            throw new InvalidInformationException("Error validating card info: Expiration date has to be [1-12]/YYYY");
        }

        var year  = Integer.parseInt(payment.getExpiration().split("/")[1]);
        if (LocalDate.now().getYear() > year) {
            log.error("Error validating card info: The card is expired!");
            throw new InvalidInformationException("Error validating card info: The card is expired!");
        }
        if (!payment.getSecurityCode().matches(securityCodeRegex)) {
            log.error("Error validating card info: Security code has to be ex: 123");
            throw new InvalidInformationException("Error validating card info: Security code has to be ex: 123");
        }
    }
}
