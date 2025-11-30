package com.paytabs.core.service;

import com.paytabs.core.entity.Card;
import com.paytabs.core.entity.TransactionLog;
import com.paytabs.core.repository.CardRepository;
import com.paytabs.core.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepo;
    private final TransactionLogRepository txRepo;

    public CardService(CardRepository cardRepo, TransactionLogRepository txRepo) {
        this.cardRepo = cardRepo;
        this.txRepo = txRepo;
    }

    public Optional<Card> findByCardNumber(String cardNumber) {
        return cardRepo.findById(cardNumber);
    }

    public String hashPin(String pin) {
        if (pin == null) return null;
        return DigestUtils.sha256Hex(pin);
    }

    public TransactionLog processTransaction(String cardNumber, String pin, double amount, String type) {
        TransactionLog tx = new TransactionLog();
        tx.setCardNumber(cardNumber);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setTimestamp(LocalDateTime.now());

        Optional<Card> cardOpt = findByCardNumber(cardNumber);
        if (cardOpt.isEmpty()) {
            tx.setStatus("FAILED");
            tx.setReason("Invalid card");
            return txRepo.save(tx);
        }

        Card card = cardOpt.get();
        String hashed = hashPin(pin);
        if (!card.getPinHash().equals(hashed)) {
            tx.setStatus("FAILED");
            tx.setReason("Invalid PIN");
            return txRepo.save(tx);
        }

        if ("withdraw".equalsIgnoreCase(type)) {
            if (card.getBalance() < amount) {
                tx.setStatus("FAILED");
                tx.setReason("Insufficient funds");
                return txRepo.save(tx);
            }
            card.setBalance(card.getBalance() - amount);
        } else if ("topup".equalsIgnoreCase(type)) {
            card.setBalance(card.getBalance() + amount);
        } else {
            tx.setStatus("FAILED");
            tx.setReason("Invalid type");
            return txRepo.save(tx);
        }

        cardRepo.save(card);
        tx.setStatus("SUCCESS");
        tx.setReason("Processed");
        txRepo.save(tx);
        return tx;
    }

    public List<TransactionLog> getAllTransactions() {
        return txRepo.findAll();
    }

    public List<TransactionLog> getTransactionsForCard(String cardNumber) {
        return txRepo.findByCardNumber(cardNumber);
    }
}
