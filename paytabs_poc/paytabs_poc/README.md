# PayTabs Banking System POC

This repository contains a Proof-of-Concept for a simplified banking system with:
- System 1 (Gateway) - runs on port 8081
- System 2 (CoreBank) - runs on port 8082
- React UI (banking-ui) - runs with `npm run dev`

## Quickstart (Java + Node.js required)
1. Start System 2 (CoreBank):
   - cd system2-corebank
   - mvn spring-boot:run
2. Start System 1 (Gateway):
   - cd system1-gateway
   - mvn spring-boot:run
3. Start frontend:
   - cd banking-ui
   - npm install
   - npm run dev

## Seeded data
- Card: 4123456789012345
- PIN: 1234 (stored as SHA-256 hash)
- Balance: 1000.00

## APIs
- POST http://localhost:8081/transaction  → gateway endpoint
- POST http://localhost:8082/process      → corebank processing
- GET  http://localhost:8082/process/all  → get all transactions
- GET  http://localhost:8082/process/card/{cardNumber} → get transactions for a card

## Notes
- This is a starter project. Fill in more robust error handling, security, and UI as needed.
