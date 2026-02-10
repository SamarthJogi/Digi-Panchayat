package com.digipanchayat.repository;

import com.digipanchayat.entity.NewConnectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewConnectionRequestRepository extends JpaRepository<NewConnectionRequest, Long> {
    List<NewConnectionRequest> findByConsumerId(Long consumerId);

    List<NewConnectionRequest> findByStatus(String status);

    List<NewConnectionRequest> findByServiceType(String serviceType);
}
