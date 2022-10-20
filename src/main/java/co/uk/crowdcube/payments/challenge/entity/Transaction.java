package co.uk.crowdcube.payments.challenge.entity;

import co.uk.crowdcube.payments.challenge.entity.base.BaseEntity;
import co.uk.crowdcube.payments.challenge.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @JoinColumn(name = "payment", referencedColumnName = "id")
    @ManyToOne
    private Payment payment;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_token")
    private String transactionToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;
}
