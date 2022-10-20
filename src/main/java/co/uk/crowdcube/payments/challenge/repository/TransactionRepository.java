package co.uk.crowdcube.payments.challenge.repository;

import co.uk.crowdcube.payments.challenge.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}