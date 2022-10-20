package co.uk.crowdcube.payments.challenge.dto;

import co.uk.crowdcube.payments.challenge.enums.CardBrand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements Serializable {

    private Long id;
    private CardBrand brand;
    private String nameOnCard;
    private Long cardNumber;
    private String expiration;
    private String securityCode;

}
