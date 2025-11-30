package com.paytabs.core.controller;

import com.paytabs.core.entity.TransactionLog;
import com.paytabs.core.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/process")
@CrossOrigin(origins = "*")
public class ProcessingController {

    private final CardService cardService;

    public ProcessingController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<?> process(@RequestBody Map<String, Object> req) {
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

        TransactionLog tx = cardService.processTransaction(cardNumber, pin, amount, type);
        Map<String,Object> out = new HashMap<>();
        out.put("status", tx.getStatus());
        out.put("reason", tx.getReason());
        out.put("cardNumber", tx.getCardNumber());
        out.put("amount", tx.getAmount());
        out.put("type", tx.getType());
        return ResponseEntity.ok(out);
    }

    @GetMapping("/all")
    public List<TransactionLog> all() {
        return cardService.getAllTransactions();
    }

    @GetMapping("/card/{cardNumber}")
    public List<TransactionLog> forCard(@PathVariable String cardNumber) {
        return cardService.getTransactionsForCard(cardNumber);
    }
}
