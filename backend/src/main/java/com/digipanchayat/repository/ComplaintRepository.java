package com.digipanchayat.repository;

import com.digipanchayat.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByConsumerId(Long consumerId);

    List<Complaint> findByStatus(String status);

    long countByStatus(String status);
}
