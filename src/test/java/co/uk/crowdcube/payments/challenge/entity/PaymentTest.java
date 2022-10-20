package co.uk.crowdcube.payments.challenge.entity;

import co.uk.crowdcube.payments.challenge.dto.PaymentDTO;
import co.uk.crowdcube.payments.challenge.exception.InvalidInformationException;
import co.uk.crowdcube.payments.challenge.repository.PaymentRepository;
import co.uk.crowdcube.payments.challenge.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static co.uk.crowdcube.payments.challenge.enums.CardBrand.MASTERCARD;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.hamcrest.core.Is.is;

@SpringBootTest
public class PaymentTest {

    @Autowired
    private PaymentService service;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long ID = 1L;
    private static final Long CARD_NUMBER = 5162428146911108L;
    private static final String NAME_ON_CARD = "SAMUEL D N CATALANO";
    private static final String EXPIRATION = "07/2028";
    private static final String SECURITY_CODE = "079";

    @Nested
    @DisplayName("Payment")
    class PaymentUnitTest {

        @Test
        void payment_returnsSuccessfulResponse() throws Exception {
            // Given
            var payment = getCompletePayment();
            var dto  = objectMapper.convertValue(payment, PaymentDTO.class);

            // When
            var response = service.createPayment(dto);

            // Then
            assertThat(response.getId(), is(ID));
            assertThat(response.getNameOnCard(), is(NAME_ON_CARD));
            assertThat(response.getBrand(), is(MASTERCARD));
            assertThat(response.getCardNumber(), is(CARD_NUMBER));
            assertThat(response.getSecurityCode(), is(SECURITY_CODE));
            assertThat(response.getExpiration(), is(EXPIRATION));
        }

        @Test
        void payment_returnsErrorResponseNameOnCard() throws Exception {
            // Given
            var payment = getCompletePayment();
            payment.setNameOnCard(null);
            var dto  = objectMapper.convertValue(payment, PaymentDTO.class);

            // When
            final Exception exception = assertThrows(InvalidInformationException.class,
                    () -> service.createPayment(dto),
                    "Should throw InvalidInformationException with missing or invalid request details");

            // Then
            assertThat(exception.getMessage(), is("Error validating card info: Name on card couldn't be blank!"));
        }

        @Test
        void payment_returnsErrorResponseCardNumber() throws Exception {
            // Given
            var payment = getCompletePayment();
            payment.setCardNumber(5162911108L);
            var dto  = objectMapper.convertValue(payment, PaymentDTO.class);

            // When
            final Exception exception = assertThrows(InvalidInformationException.class,
                    () -> service.createPayment(dto),
                    "Should throw InvalidInformationException with missing or invalid request details");

            // Then
            assertThat(exception.getMessage(), is("Error validating card info: Card number is not 16-19 digits!"));
        }

        @Test
        void payment_returnsErrorResponseExpirationPattern() throws Exception {
            // Given
            var payment = getCompletePayment();
            payment.setExpiration("02/23");
            var dto  = objectMapper.convertValue(payment, PaymentDTO.class);

            // When
            final Exception exception = assertThrows(InvalidInformationException.class,
                    () -> service.createPayment(dto),
                    "Should throw InvalidInformationException with missing or invalid request details");

            // Then
            assertThat(exception.getMessage(), is("Error validating card info: Expiration date has to be [1-12]/YYYY"));
        }

        @Test
        void payment_returnsErrorResponseExpirationInvalidMonth() throws Exception {
            // Given
            var payment = getCompletePayment();
            payment.setExpiration("13/2021");
            var dto  = objectMapper.convertValue(payment, PaymentDTO.class);

            // When
            final Exception exception = assertThrows(InvalidInformationException.class,
                    () -> service.createPayment(dto),
                    "Should throw InvalidInformationException with missing or invalid request details");

            // Then
            assertThat(exception.getMessage(), is("Error validating card info: Expiration date has to be [1-12]/YYYY"));
        }

        @Test
        void payment_returnsErrorResponseExpirationInvalidYear() throws Exception {
            // Given
            var payment = getCompletePayment();
            payment.setExpiration("12/2020");
            var dto  = objectMapper.convertValue(payment, PaymentDTO.class);

            // When
            final Exception exception = assertThrows(InvalidInformationException.class,
                    () -> service.createPayment(dto),
                    "Should throw InvalidInformationException with missing or invalid request details");

            // Then
            assertThat(exception.getMessage(), is("Error validating card info: The card is expired!"));
        }
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
