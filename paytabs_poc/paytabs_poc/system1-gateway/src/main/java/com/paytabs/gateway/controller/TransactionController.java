package com.paytabs.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String system2Url = "http://localhost:8082/process";

    @PostMapping
    public ResponseEntity<?> handle(@RequestBody Map<String, Object> req) {
        String cardNumber = (String) req.get("cardNumber");
        String pin = (String) req.get("pin");
        Object amountObj = req.get("amount");
        String type = (String) req.get("type");

        Map<String, Object> resp = new HashMap<>();
        if (cardNumber == null || pin == null || amountObj == null || type == null) {
            resp.put("status", "FAILED");
            resp.put("reason", "Missing fields");
            return ResponseEntity.badRequest().body(resp);
        }

        double amount;
        try {
            amount = Double.parseDouble(amountObj.toString());
        } catch (Exception e) {
            resp.put("status", "FAILED");
            resp.put("reason", "Invalid amount");
            return ResponseEntity.badRequest().body(resp);
        }

        if (amount <= 0) {
            resp.put("status", "FAILED");
            resp.put("reason", "Amount must be > 0");
            return ResponseEntity.badRequest().body(resp);
        }

        if (!"withdraw".equalsIgnoreCase(type) && !"topup".equalsIgnoreCase(type)) {
            resp.put("status", "FAILED");
            resp.put("reason", "Invalid type");
            return ResponseEntity.badRequest().body(resp);
        }

        // Card range check: only cards starting with '4'
        if (cardNumber == null || !cardNumber.startsWith("4")) {
            resp.put("status", "FAILED");
            resp.put("reason", "Card range not supported");
            return ResponseEntity.badRequest().body(resp);
        }

        // forward to system2
        try {
            ResponseEntity<Map> r = restTemplate.postForEntity(system2Url, req, Map.class);
            return ResponseEntity.status(r.getStatusCode()).body(r.getBody());
        } catch (Exception ex) {
            resp.put("status", "FAILED");
            resp.put("reason", "CoreBank unavailable");
            return ResponseEntity.status(503).body(resp);
        }
    }
}
