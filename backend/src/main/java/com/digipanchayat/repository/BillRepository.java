package com.digipanchayat.repository;

import com.digipanchayat.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByConsumerServiceNumber(String consumerServiceNumber);

    @Query("SELECT b FROM Bill b WHERE b.consumerServiceNumber = ?1 ORDER BY b.billDate DESC LIMIT 1")
    Optional<Bill> findLatestByConsumerServiceNumber(String consumerServiceNumber);

    List<Bill> findByConsumerId(Long consumerId);

    List<Bill> findByConsumerIdAndStatus(Long consumerId, String status);

    long countByStatus(String status);

    List<Bill> findByStatus(String status);
}
