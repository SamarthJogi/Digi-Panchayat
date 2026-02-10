package com.digipanchayat.service;

import com.digipanchayat.dto.BillWithPenaltyDTO;
import com.digipanchayat.dto.PropertyTaxRequest;
import com.digipanchayat.entity.Bill;
import com.digipanchayat.entity.Consumer;
import com.digipanchayat.repository.BillRepository;
import com.digipanchayat.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final ConsumerRepository consumerRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int GRACE_PERIOD_MONTHS = 2;
    private static final double PENALTY_RATE = 0.01; // 1% per month

    public BillService(BillRepository billRepository, ConsumerRepository consumerRepository) {
        this.billRepository = billRepository;
        this.consumerRepository = consumerRepository;
    }

    @Value("${app.property.tax.rate.per.sqft}")
    private double propertyTaxRatePerSqft;

    @Value("${app.water.tax.fixed.annual}")
    private double waterTaxFixedAnnual;

    public BillWithPenaltyDTO getWaterBillWithPenalty(String consumerServiceNumber) {
        Bill bill = billRepository.findLatestByConsumerServiceNumber(consumerServiceNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Water bill not found for consumer number: " + consumerServiceNumber));
        return calculateBillWithPenalty(bill);
    }

    public BillWithPenaltyDTO getPropertyTaxBillWithPenalty(String consumerServiceNumber) {
        Bill bill = billRepository.findLatestByConsumerServiceNumber(consumerServiceNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Property tax bill not found for consumer number: " + consumerServiceNumber));
        return calculateBillWithPenalty(bill);
    }

    public Bill getWaterBill(String consumerServiceNumber) {
        return billRepository.findLatestByConsumerServiceNumber(consumerServiceNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Water bill not found for consumer number: " + consumerServiceNumber));
    }

    public Bill getPropertyTaxBill(String consumerServiceNumber) {
        return billRepository.findLatestByConsumerServiceNumber(consumerServiceNumber)
                .orElseThrow(() -> new RuntimeException(
                        "Property tax bill not found for consumer number: " + consumerServiceNumber));
    }

    public Bill calculatePropertyTax(PropertyTaxRequest request, Long consumerId) {
        // Calculate property tax: Area × Rate per sq ft × Usage Factor
        double usageFactor = getUsageFactor(request.getUsageType());
        double propertyTax = request.getArea() * propertyTaxRatePerSqft * usageFactor;

        // Create bill object with calculated values
        Bill bill = new Bill();
        bill.setConsumerServiceNumber(request.getConsumerServiceNumber());
        bill.setBillType("PROPERTY_TAX");
        bill.setArea(request.getArea());
        bill.setUsageType(request.getUsageType());
        bill.setAmount(String.format("%.2f", propertyTax));

        LocalDate billDate = LocalDate.now();
        bill.setBillDate(billDate.format(DATE_FORMATTER));

        // Set due date (2 months grace period)
        LocalDate dueDate = billDate.plusMonths(GRACE_PERIOD_MONTHS);
        bill.setDueDate(dueDate.format(DATE_FORMATTER));

        bill.setStatus("PENDING");
        bill.setPenaltyAmount(0.0);
        bill.setMonthsDelayed(0);

        // If consumerId is null, just return calculated bill without saving
        if (consumerId == null) {
            return bill;
        }

        // Check if bill already exists for this consumer
        Bill existingBill = billRepository.findByConsumerServiceNumber(request.getConsumerServiceNumber())
                .orElse(null);

        if (existingBill != null) {
            return existingBill;
        }

        // Find consumer and save bill
        Consumer consumer = consumerRepository.findById(consumerId)
                .orElseThrow(() -> new RuntimeException("Consumer not found"));

        bill.setConsumer(consumer);
        return billRepository.save(bill);
    }

    public List<BillWithPenaltyDTO> getConsumerBillsWithPenalty(Long consumerId) {
        List<Bill> bills = billRepository.findByConsumerId(consumerId);
        return bills.stream()
                .map(this::calculateBillWithPenalty)
                .collect(Collectors.toList());
    }

    public List<Bill> getConsumerBills(Long consumerId) {
        return billRepository.findByConsumerId(consumerId);
    }

    public List<Bill> getPendingBills() {
        return billRepository.findByStatus("PENDING");
    }

    public List<Bill> getPaidBills() {
        return billRepository.findByStatus("PAID");
    }

    private BillWithPenaltyDTO calculateBillWithPenalty(Bill bill) {
        double baseAmount = Double.parseDouble(bill.getAmount());
        double penaltyAmount = 0.0;
        int monthsDelayed = 0;

        if ("PENDING".equals(bill.getStatus()) && bill.getDueDate() != null) {
            LocalDate dueDate = LocalDate.parse(bill.getDueDate(), DATE_FORMATTER);
            LocalDate currentDate = LocalDate.now();

            if (currentDate.isAfter(dueDate)) {
                monthsDelayed = (int) ChronoUnit.MONTHS.between(dueDate, currentDate);
                if (monthsDelayed > 0) {
                    // Penalty = Base Amount × 1% × Number of Delay Months
                    penaltyAmount = baseAmount * PENALTY_RATE * monthsDelayed;
                }
            }
        }

        double totalAmount = baseAmount + penaltyAmount;

        BillWithPenaltyDTO dto = new BillWithPenaltyDTO();
        dto.setId(bill.getId());
        dto.setBillType(bill.getBillType());
        dto.setConsumerServiceNumber(bill.getConsumerServiceNumber());
        dto.setBillDate(bill.getBillDate());
        dto.setDueDate(bill.getDueDate());
        dto.setBaseAmount(baseAmount);
        dto.setPenaltyAmount(penaltyAmount);
        dto.setTotalAmount(totalAmount);
        dto.setMonthsDelayed(monthsDelayed);
        dto.setStatus(bill.getStatus());
        dto.setArea(bill.getArea());
        dto.setUsageType(bill.getUsageType());

        return dto;
    }

    private double getUsageFactor(String usageType) {
        return switch (usageType.toUpperCase()) {
            case "RESIDENTIAL" -> 1.0;
            case "RENTED" -> 1.2;
            case "COMMERCIAL" -> 1.5;
            default -> throw new RuntimeException("Invalid usage type");
        };
    }

    public List<Bill> generateSampleWaterBills() {
        List<Bill> sampleBills = new java.util.ArrayList<>();

        // Sample Bill 1
        Bill bill1 = new Bill();
        bill1.setConsumerServiceNumber("WTR1001");
        bill1.setBillType("WATER");

        bill1.setAmount("750.00");
        bill1.setBillDate(LocalDate.now().minusMonths(1).format(DATE_FORMATTER));
        bill1.setDueDate(LocalDate.now().plusMonths(1).format(DATE_FORMATTER));
        bill1.setStatus("PENDING");
        bill1.setPenaltyAmount(0.0);
        bill1.setMonthsDelayed(0);
        sampleBills.add(billRepository.save(bill1));

        // Sample Bill 2
        Bill bill2 = new Bill();
        bill2.setConsumerServiceNumber("WTR1002");
        bill2.setBillType("WATER");

        bill2.setAmount("1000.00");
        bill2.setBillDate(LocalDate.now().minusMonths(2).format(DATE_FORMATTER));
        bill2.setDueDate(LocalDate.now().format(DATE_FORMATTER));
        bill2.setStatus("PENDING");
        bill2.setPenaltyAmount(0.0);
        bill2.setMonthsDelayed(0);
        sampleBills.add(billRepository.save(bill2));

        // Sample Bill 3 - Overdue
        Bill bill3 = new Bill();
        bill3.setConsumerServiceNumber("WTR1003");
        bill3.setBillType("WATER");

        bill3.setAmount("900.00");
        bill3.setBillDate(LocalDate.now().minusMonths(4).format(DATE_FORMATTER));
        bill3.setDueDate(LocalDate.now().minusMonths(2).format(DATE_FORMATTER));
        bill3.setStatus("PENDING");
        bill3.setPenaltyAmount(0.0);
        bill3.setMonthsDelayed(2);
        sampleBills.add(billRepository.save(bill3));

        return sampleBills;
    }
}
