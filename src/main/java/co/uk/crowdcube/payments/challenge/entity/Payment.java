package co.uk.crowdcube.payments.challenge.entity;

import co.uk.crowdcube.payments.challenge.entity.base.BaseEntity;
import co.uk.crowdcube.payments.challenge.enums.CardBrand;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand")
    private CardBrand brand;

    @Column(name = "name_on_card")
    private String nameOnCard;

    @Column(name = "card_number")
    private Long cardNumber;

    @Column(name = "expiration")
    private String expiration;

    @Column(name = "security_code")
    private String securityCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        var payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return 1545849159;
    }
}