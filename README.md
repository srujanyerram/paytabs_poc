PayTabs Banking System – Proof of Concept (POC)

A fully functional two-tier banking system demonstrating transaction routing, card verification, secure PIN authentication, balance processing, and role-based dashboards.

This POC simulates real-world payment processing like a Visa/Mastercard switch → core banking engine → customer dashboard.

🚀 Features
🔹 System 1 — Gateway (Port 8081)

Receives transactions, validates input, checks card range, and routes valid requests to System 2.

Validates card number, PIN, amount > 0, type (withdraw/topup)

Accepts only cards starting with 4

Routes to System 2 using REST

Returns success/failure JSON

🔹 System 2 — Core Banking Engine (Port 8082)

Handles secure processing:

SHA-256 PIN hashing (no plain-text PIN stored)

Balance checks (withdraw + top-up)

Card validation

Transaction logging

Exposes APIs for customer & admin dashboards

In-memory H2 database auto-seeds:

Card Number: 4123456789012345
PIN: 1234 (hashed)
Balance: 1000.00
Customer: John Doe

🔹 React Frontend (Port 5173)

Provides two dashboards:

👤 Customer

View balance

View personal transactions

Perform top-ups

🛡 Super Admin

View ALL system transactions

Monitor failures & routing decisions

🗂 Project Structure
paytabs_poc/
├── system1-gateway/         # API Gateway (Spring Boot)
├── system2-corebank/        # Core Banking Engine (Spring Boot)
└── banking-ui/              # React Frontend

🛠 Tech Stack
Backend

Java 17

Spring Boot 3

Spring Web

Spring Security

Spring Data JPA

H2 In-memory DB

Maven

Frontend

React + Vite

Axios

React Router

Material UI (optional)

⚙️ How to Run (Local Setup)
1️⃣ Start System 2 (Core Banking Engine)
cd system2-corebank
mvn spring-boot:run


Runs at:

👉 http://localhost:8082

2️⃣ Start System 1 (Gateway)
cd system1-gateway
mvn spring-boot:run


Runs at:

👉 http://localhost:8081

3️⃣ Start Frontend (React)
cd banking-ui
npm install
npm run dev


Runs at:

👉 http://localhost:5173/

🔗 API Reference
System 1 (Gateway) – Port 8081
▶ POST /transaction

Example:

{
  "cardNumber": "4123456789012345",
  "pin": "1234",
  "amount": 50,
  "type": "withdraw"
}

System 2 (Core Banking) – Port 8082
▶ POST /process

Processes a transaction (withdraw / topup)

▶ GET /process/all

Returns all transactions (Admin)

▶ GET /process/card/{cardNumber}

Returns customer-only transactions

▶ H2 Console
http://localhost:8082/h2-console


JDBC URL:

jdbc:h2:mem:corebankdb

🧪 Test Scenarios
✔ Successful top-up
✔ Successful withdrawal
✔ Card not starting with 4 → rejected
✔ Invalid card → rejected
✔ Wrong PIN → rejected
✔ Insufficient balance → failed
✔ Admin sees all transactions
✔ Customer sees own transactions only
🔐 Security

PINs stored only as SHA-256 hash

No plain-text PIN logged or stored

H2 in-memory DB (resets each run)

Hash sample:

DigestUtils.sha256Hex("1234")

👨‍💻 Login Credentials
Customer:
username: cust1
password: pass

Admin:
username: admin
password: admin

📝 Notes

This is a POC; for production you’d add JWT auth, PostgreSQL, dockerization, logging, and rate limiting.

The routing logic simulates scheme-based card routing commonly used in payment systems.
