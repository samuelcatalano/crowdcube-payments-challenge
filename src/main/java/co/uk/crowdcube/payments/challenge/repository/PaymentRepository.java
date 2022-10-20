package co.uk.crowdcube.payments.challenge.repository;

import co.uk.crowdcube.payments.challenge.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}