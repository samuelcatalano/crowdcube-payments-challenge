package co.uk.crowdcube.payments.challenge.dto;

import co.uk.crowdcube.payments.challenge.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    private Long id;
    private Long paymentId;
    private PaymentDTO payment;
    private String description;
    private BigDecimal amount;
    private String transactionToken;
    private TransactionStatus status;

}
