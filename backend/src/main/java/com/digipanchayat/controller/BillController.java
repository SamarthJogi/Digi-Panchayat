package com.digipanchayat.controller;

import com.digipanchayat.dto.ApiResponse;
import com.digipanchayat.dto.BillWithPenaltyDTO;
import com.digipanchayat.dto.PropertyTaxRequest;
import com.digipanchayat.entity.Bill;
import com.digipanchayat.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")

public class BillController {

    private final BillService billService;
    private final com.digipanchayat.service.PdfServiceClient pdfServiceClient;

    public BillController(BillService billService, com.digipanchayat.service.PdfServiceClient pdfServiceClient) {
        this.billService = billService;
        this.pdfServiceClient = pdfServiceClient;
    }

    @GetMapping("/water/{consumerServiceNumber}")
    public ResponseEntity<ApiResponse> getWaterBill(@PathVariable String consumerServiceNumber) {
        try {
            Bill bill = billService.getWaterBill(consumerServiceNumber);
            return ResponseEntity.ok(new ApiResponse(true, "Bill retrieved successfully", bill));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/water/penalty/{consumerServiceNumber}")
    public ResponseEntity<ApiResponse> getWaterBillWithPenalty(@PathVariable String consumerServiceNumber) {
        try {
            BillWithPenaltyDTO bill = billService.getWaterBillWithPenalty(consumerServiceNumber);
            return ResponseEntity.ok(new ApiResponse(true, "Bill with penalty retrieved successfully", bill));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/property/{consumerServiceNumber}")
    public ResponseEntity<ApiResponse> getPropertyTaxBill(@PathVariable String consumerServiceNumber) {
        try {
            Bill bill = billService.getPropertyTaxBill(consumerServiceNumber);
            return ResponseEntity.ok(new ApiResponse(true, "Bill retrieved successfully", bill));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/property/penalty/{consumerServiceNumber}")
    public ResponseEntity<ApiResponse> getPropertyTaxBillWithPenalty(@PathVariable String consumerServiceNumber) {
        try {
            BillWithPenaltyDTO bill = billService.getPropertyTaxBillWithPenalty(consumerServiceNumber);
            return ResponseEntity.ok(new ApiResponse(true, "Bill with penalty retrieved successfully", bill));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/property/calculate")
    public ResponseEntity<ApiResponse> calculatePropertyTax(@RequestBody PropertyTaxRequest request,
            @RequestParam(required = false) Long consumerId) {
        try {
            Bill bill = billService.calculatePropertyTax(request, consumerId);
            return ResponseEntity.ok(new ApiResponse(true, "Property tax calculated successfully", bill));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/consumer/{consumerId}")
    public ResponseEntity<ApiResponse> getConsumerBills(@PathVariable Long consumerId) {
        try {
            List<Bill> bills = billService.getConsumerBills(consumerId);
            return ResponseEntity.ok(new ApiResponse(true, "Bills retrieved successfully", bills));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/consumer/penalty/{consumerId}")
    public ResponseEntity<ApiResponse> getConsumerBillsWithPenalty(@PathVariable Long consumerId) {
        try {
            List<BillWithPenaltyDTO> bills = billService.getConsumerBillsWithPenalty(consumerId);
            return ResponseEntity.ok(new ApiResponse(true, "Bills with penalty retrieved successfully", bills));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/generate-samples")
    public ResponseEntity<ApiResponse> generateSampleBills() {
        try {
            List<Bill> bills = billService.generateSampleWaterBills();
            return ResponseEntity.ok(new ApiResponse(true, "Sample bills created successfully", bills));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/water/{consumerServiceNumber}/pdf")
    public ResponseEntity<byte[]> downloadWaterBillPdf(@PathVariable String consumerServiceNumber) {
        try {
            BillWithPenaltyDTO bill = billService.getWaterBillWithPenalty(consumerServiceNumber);

            java.util.Map<String, Object> billData = new java.util.HashMap<>();
            billData.put("billId", bill.getId());
            billData.put("consumerServiceNumber", bill.getConsumerServiceNumber());
            billData.put("consumerName", "Consumer"); // You may want to add this to the bill
            billData.put("consumerAddress", "Address"); // You may want to add this to the bill
            billData.put("billDate", bill.getBillDate());
            billData.put("dueDate", bill.getDueDate());
            billData.put("baseAmount", bill.getBaseAmount());
            billData.put("penaltyAmount", bill.getPenaltyAmount());
            billData.put("totalAmount", bill.getTotalAmount());
            billData.put("status", bill.getStatus());

            byte[] pdfBytes = pdfServiceClient.generateWaterBillPdf(billData);

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    org.springframework.http.ContentDisposition.attachment()
                            .filename("WaterBill_" + consumerServiceNumber + ".pdf")
                            .build());

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/property/{consumerServiceNumber}/pdf")
    public ResponseEntity<byte[]> downloadPropertyTaxPdf(@PathVariable String consumerServiceNumber) {
        try {
            BillWithPenaltyDTO bill = billService.getPropertyTaxBillWithPenalty(consumerServiceNumber);

            java.util.Map<String, Object> billData = new java.util.HashMap<>();
            billData.put("billId", bill.getId());
            billData.put("consumerServiceNumber", bill.getConsumerServiceNumber());
            billData.put("consumerName", "Consumer");
            billData.put("propertyAddress", "Property Address");
            billData.put("area", bill.getArea() != null ? bill.getArea() : 0.0);
            billData.put("usageType", bill.getUsageType() != null ? bill.getUsageType() : "RESIDENTIAL");
            billData.put("billDate", bill.getBillDate());
            billData.put("dueDate", bill.getDueDate());
            billData.put("baseAmount", bill.getBaseAmount());
            billData.put("penaltyAmount", bill.getPenaltyAmount());
            billData.put("totalAmount", bill.getTotalAmount());
            billData.put("status", bill.getStatus());

            byte[] pdfBytes = pdfServiceClient.generatePropertyTaxPdf(billData);

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    org.springframework.http.ContentDisposition.attachment()
                            .filename("PropertyTax_" + consumerServiceNumber + ".pdf")
                            .build());

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
