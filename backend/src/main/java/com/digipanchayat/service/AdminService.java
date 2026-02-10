package com.digipanchayat.service;
import com.digipanchayat.repository.BillRepository;
import com.digipanchayat.repository.ComplaintRepository;
import com.digipanchayat.repository.ConsumerRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    private final ConsumerRepository consumerRepository;
    private final BillRepository billRepository;
    private final ComplaintRepository complaintRepository;

    public AdminService(ConsumerRepository consumerRepository, BillRepository billRepository, ComplaintRepository complaintRepository) {
        this.consumerRepository = consumerRepository;
        this.billRepository = billRepository;
        this.complaintRepository = complaintRepository;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalConsumers", consumerRepository.countByRole("CONSUMER"));
        stats.put("pendingBills", billRepository.countByStatus("PENDING"));
        stats.put("paidBills", billRepository.countByStatus("PAID"));
        stats.put("totalComplaints", complaintRepository.count());
        stats.put("pendingComplaints", complaintRepository.countByStatus("PENDING"));
        stats.put("resolvedComplaints", complaintRepository.countByStatus("RESOLVED"));

        return stats;
    }
}
