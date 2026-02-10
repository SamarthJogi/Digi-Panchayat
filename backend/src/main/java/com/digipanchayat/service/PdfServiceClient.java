package com.digipanchayat.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PdfServiceClient {

    private final RestTemplate restTemplate;
    private static final String PDF_SERVICE_URL = "http://localhost:5001/api/pdf";

    public PdfServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public byte[] generateWaterBillPdf(Map<String, Object> billData) {
        String url = PDF_SERVICE_URL + "/generate-water-bill";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(billData, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                byte[].class);

        return response.getBody();
    }

    public byte[] generatePropertyTaxPdf(Map<String, Object> billData) {
        String url = PDF_SERVICE_URL + "/generate-property-tax";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(billData, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                byte[].class);

        return response.getBody();
    }
}
