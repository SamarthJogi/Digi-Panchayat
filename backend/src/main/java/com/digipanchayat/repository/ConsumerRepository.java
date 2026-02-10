package com.digipanchayat.repository;

import com.digipanchayat.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
    Optional<Consumer> findByEmail(String email);

    Optional<Consumer> findByMobile(String mobile);

    Optional<Consumer> findByConsumerNumber(String consumerNumber);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    long countByRole(String role);
}
