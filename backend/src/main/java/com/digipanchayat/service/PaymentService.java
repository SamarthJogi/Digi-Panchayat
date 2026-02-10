package com.digipanchayat.service;
import com.digipanchayat.entity.Bill;
import com.digipanchayat.entity.Payment;
import com.digipanchayat.repository.BillRepository;
import com.digipanchayat.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public PaymentService(PaymentRepository paymentRepository, BillRepository billRepository) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
    }

    @Transactional
    public Payment processPayment(Long billId, String paymentMethod) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if ("PAID".equals(bill.getStatus())) {
            throw new RuntimeException("Bill already paid");
        }

        // Create payment record
        Payment payment = new Payment();
        payment.setAmount(bill.getAmount());
        payment.setPaymentDate(LocalDate.now().toString());
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId("TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setBill(bill);

        Payment savedPayment = paymentRepository.save(payment);

        // Update bill status
        bill.setStatus("PAID");
        billRepository.save(bill);

        return savedPayment;
    }

    public List<Payment> getConsumerPayments(Long consumerId) {
        return paymentRepository.findByBillConsumerId(consumerId);
    }

    public List<Payment> getBillPayments(Long billId) {
        return paymentRepository.findByBillId(billId);
    }
}
