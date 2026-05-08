#  Gym Management System

A production-grade **REST API** for managing a gym's members, trainers, memberships, payments, and attendance — built with **Java 21 + Spring Boot 4.0**, secured with **JWT-based authentication** and **role-based access control**.

---

##  Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0 |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Database | MySQL 8.0 |
| ORM | JPA + Hibernate |
| Mapping | MapStruct 1.5.5 |
| Validation | Spring Validation |
| Documentation | SpringDoc OpenAPI (Swagger) 3.0.2 |
| Build | Maven |
| Utilities | Lombok |
| Logging | SLF4J |

---

##  Features

- **JWT Authentication** with refresh token rotation and secure logout
- **Role-Based Access Control** — ADMIN, TRAINER, MEMBER roles with `@PreAuthorize`
- **Member Management** — CRUD, status control, trainer assignment
- **Trainer Management** — CRUD, activation/deactivation, assigned members
- **Membership Plans** — Create and manage gym plans with pricing and duration
- **Membership Lifecycle** — Auto-cancels old membership on renewal, full history preserved
- **Payment Tracking** — Auto-creates PENDING payment on membership creation, mark as PAID at counter
- **Trainer Salary Payments** — Monthly salary tracking with PENDING/PAID/OVERDUE status
- **Pending Dues** — Admin endpoint to view all unpaid member and trainer payments
- **Attendance System** — Daily check-in/check-out with one check-in per day enforced
- **Dashboard** — Complete gym overview with revenue, expenses, net profit, and member stats
- **Self-service** — Members and trainers can view and update their own profiles
- **Custom Exception Handling** — Proper HTTP status codes for all error cases
- **Input Validation** — `@Valid` + `@Pattern` on all request DTOs
- **Swagger UI** — Full API documentation at `/swagger-ui/index.html`

---

##  Architecture

```
Client (Postman / Frontend)
        ↓
JwtAuthenticationFilter     → validates JWT on every request
        ↓
Controller Layer            → handles HTTP, applies @PreAuthorize
        ↓
Service Layer               → all business logic
        ↓
Repository Layer            → JPA database operations
        ↓
MySQL Database
```

**Design Patterns:** Layered Architecture · DTO Pattern · MapStruct · Repository Pattern · Custom Exception Hierarchy

---

##  Authentication Flow

```
POST /auth/login → returns accessToken (15 min) + refreshToken (7 days)

Every request → Authorization: Bearer <accessToken>

Token expired → POST /auth/refresh → new tokens issued (rotation)

Logout → POST /auth/logout → refresh token deleted from DB
```

---

##  API Overview

| Module | Endpoints | Roles |
|---|---|---|
| Auth | Login, Refresh, Logout, Change Password | Public / All |
| Members | CRUD, Status, Trainer Assignment, Self-service | ADMIN, MEMBER |
| Trainers | CRUD, Status, Assigned Members, Self-service | ADMIN, TRAINER |
| Membership Plans | CRUD, Toggle Active | ADMIN, All |
| Memberships | Create, History, Cancel, Expiry Tracking | ADMIN, MEMBER |
| Member Payments | Record, Mark Paid, Pending Dues | ADMIN, MEMBER |
| Trainer Payments | Salary Recording, Monthly Tracking, Pending Dues | ADMIN |
| Attendance | Check-in, Check-out, History, Date Range | ADMIN, TRAINER, MEMBER |
| Dashboard | Stats, Revenue, Expenses, Net Profit | ADMIN |

**Total: 61 endpoints**

---

##  Key Design Decisions

| Decision | Reason |
|---|---|
| Phone number as username | Always unique, easy to remember |
| Refresh token stored in DB | Enables logout and token invalidation |
| Token rotation on refresh | Prevents refresh token reuse attacks |
| Payment auto-created as PENDING | Single flow — create membership, then mark payment |
| ManyToOne on Membership | Preserves full membership history |
| DTOs for all responses | Never expose raw entities to client |
| MapStruct over manual mapping | Compile-time, fast, no reflection |
| `@Transactional` on create/delete | Member + User created/deleted atomically |

---

##  Running Locally

### Prerequisites
- Java 21
- MySQL 8.0
- Maven 3.x

### Steps

**1. Create the database**
```sql
CREATE DATABASE gym_system;
```

**2. Configure `application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym_system
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

jwt.secret=<base64-encoded-256-bit-key>
jwt.expiration=900000
admin.username=yourusername
admin.password=yourpassword
default.member.password=yourmemberpassword
default.trainer.password=yourtrainerpassword
```

**3. Run**
```bash
mvn spring-boot:run
```

**4. Access**
- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

**5. First login**
```
Use the default admin credentials configured in your environment variables.

```

---

##  Project Structure

```
com.gym.management.system
├── config          → Security config, Admin seeder
├── controller      → REST controllers
├── service         → Business logic (interfaces + implementations)
├── repository      → JPA repositories
├── entity          → JPA entities
├── dto             → Request/Response DTOs + MapStruct mappers
├── security        → JWT filter, utility, SecurityUtils
├── exception       → GlobalExceptionHandler + custom exceptions
└── enums           → Roles, statuses, payment methods
```

---
