package co.uk.crowdcube.payments.challenge.entity;

import co.uk.crowdcube.payments.challenge.dto.PaymentDTO;
import co.uk.crowdcube.payments.challenge.dto.TransactionDTO;
import co.uk.crowdcube.payments.challenge.exception.InvalidInformationException;
import co.uk.crowdcube.payments.challenge.service.PaymentService;
import co.uk.crowdcube.payments.challenge.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static co.uk.crowdcube.payments.challenge.enums.CardBrand.MASTERCARD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TransactionTest {

    @Autowired
    private TransactionService service;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long ID = 1L;
    private static final Long CARD_NUMBER = 5162428146911108L;
    private static final String NAME_ON_CARD = "SAMUEL D N CATALANO";
    private static final String EXPIRATION = "07/2028";
    private static final String SECURITY_CODE = "079";

    private static final String AUTH_TOKEN = UUID.randomUUID().toString();
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(500.00);
    private static final String DESCRIPTION = "New product equity";

    @Nested
    @DisplayName("Transaction")
    class TransactionUnitTest {

        @Test
        void transaction_returnsSuccessfulResponse() throws Exception {
            // Given
            var payment = getCompletePayment();
            var paymentDTO  = objectMapper.convertValue(payment, PaymentDTO.class);

            var savedPayment = paymentService.createPayment(paymentDTO);

            var transaction = getCompleteTransaction();
            var dto  = objectMapper.convertValue(transaction, TransactionDTO.class);

            dto.setPayment(savedPayment);
            dto.setPaymentId(ID);

            // When
            var response = service.createTransaction(dto);

            // Then
            assertThat(response.getId(), is(ID));
            assertThat(response.getDescription(), is(DESCRIPTION));
            assertThat(response.getAmount(), is(AMOUNT));
            assertThat(response.getPayment(), is(response.getPayment()));
        }

        @Test
        void transaction_returnsErrorResponseInvalidPayment() throws Exception {
            // Given
            var payment = getCompletePayment();
            var paymentDTO  = objectMapper.convertValue(payment, PaymentDTO.class);

            var transaction = getCompleteTransaction();
            var dto  = objectMapper.convertValue(transaction, TransactionDTO.class);

            dto.setPaymentId(ID);

            // When
            final Exception exception = assertThrows(InvalidInformationException.class,
                    () -> service.createTransaction(dto),
                    "Should throw InvalidInformationException with missing or invalid request details");

            // Then
            assertThat(exception.getMessage(), is("Error creating transaction: payment method not found!"));
        }

        @Test
        void transaction_returnsErrorResponseEmptyDescription() throws Exception {
            // Given
            var payment = getCompletePayment();
            var paymentDTO  = objectMapper.convertValue(payment, PaymentDTO.class);

            var savedPayment = paymentService.createPayment(paymentDTO);

            var transaction = getCompleteTransaction();
            var dto  = objectMapper.convertValue(transaction, TransactionDTO.class);

            dto.setPayment(savedPayment);
            dto.setPaymentId(ID);
            dto.setDescription(null);

            // When
            final Exception exception = assertThrows(InvalidInformationException.class,
                    () -> service.createTransaction(dto),
                    "Should throw InvalidInformationException with missing or invalid request details");

            // Then
            assertThat(exception.getMessage(), is("Error creating transaction: Description cannot be empty!"));
        }

        @Test
        void transaction_returnsErrorResponseInvalidAmount() throws Exception {
            // Given
            var payment = getCompletePayment();
            var paymentDTO  = objectMapper.convertValue(payment, PaymentDTO.class);

            var savedPayment = paymentService.createPayment(paymentDTO);

            var transaction = getCompleteTransaction();
            var dto  = objectMapper.convertValue(transaction, TransactionDTO.class);

            dto.setPayment(savedPayment);
            dto.setPaymentId(ID);
            dto.setAmount(BigDecimal.valueOf(-100.00));

            // When
            final Exception exception = assertThrows(InvalidInformationException.class,
                    () -> service.createTransaction(dto),
                    "Should throw InvalidInformationException with missing or invalid request details");

            // Then
            assertThat(exception.getMessage(), is("Error creating transaction: Transaction amount cannot be less than 0!"));
        }
    }

    private Transaction getCompleteTransaction() {
        return Transaction.builder()
               .id(ID)
               .transactionDate(LocalDateTime.now())
               .transactionToken(AUTH_TOKEN)
               .payment(getCompletePayment())
               .amount(AMOUNT)
               .description(DESCRIPTION)
               .build();
    }

    private Payment getCompletePayment() {
        return Payment.builder()
               .id(ID)
               .nameOnCard(NAME_ON_CARD)
               .cardNumber(CARD_NUMBER)
               .brand(MASTERCARD)
               .expiration(EXPIRATION)
               .securityCode(SECURITY_CODE)
               .build();
    }
}
