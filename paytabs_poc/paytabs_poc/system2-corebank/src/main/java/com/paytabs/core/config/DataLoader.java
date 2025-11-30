package com.paytabs.core.config;

import com.paytabs.core.entity.Card;
import com.paytabs.core.repository.CardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.codec.digest.DigestUtils;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(CardRepository cardRepository) {
        return args -> {
            // Seed a sample Visa-like card starting with 4
            Card c = new Card();
            c.setCardNumber("4123456789012345");
            c.setPinHash(DigestUtils.sha256Hex("1234"));
            c.setBalance(1000.00);
            c.setCustomerName("John Doe");
            cardRepository.save(c);
        };
    }
}
