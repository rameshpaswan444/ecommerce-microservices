# 🛒 E-Commerce Microservices System

A production-inspired **E-Commerce Backend** built with **Java 21, Spring Boot, Spring Cloud, Spring Security, JWT, MySQL, and Microservices Architecture**.

This project demonstrates how modern enterprise applications are built using independent services, API Gateway, Service Discovery, and secure JWT-based authentication.

---

## 📌 Project Architecture

```
                   +----------------------+
                   |       Client         |
                   +----------+-----------+
                              |
                         HTTP / REST
                              |
                              ▼
                   +----------------------+
                   |     API Gateway      |
                   | JWT Authentication   |
                   +----------+-----------+
                              |
          +-------------------+-------------------+
          |                                       |
          ▼                                       ▼
+----------------------+              +----------------------+
|    Auth Service      |              |   Product Service    |
|----------------------|              |----------------------|
| User Registration    |              | Product CRUD         |
| User Login           |              | Search              |
| JWT Generation       |              | Pagination          |
| Role Management      |              | Filtering           |
+----------------------+              +----------------------+
                  \                    /
                   \                  /
                    ▼                ▼
                 +----------------------+
                 |    Eureka Server     |
                 | Service Discovery    |
                 +----------------------+
```

---

# 🚀 Tech Stack

### Backend

- Java 21
- Spring Boot 3.5.5
- Spring Cloud 2025.0.0
- Spring Security
- Spring Data JPA
- Hibernate
- JWT (JJWT 0.12.7)
- MapStruct
- Lombok

### Database

- MySQL

### Build Tool

- Maven

### Architecture

- Microservices
- API Gateway
- Service Discovery (Eureka)
- REST API

---

# 📂 Current Microservices

## 1️⃣ Eureka Server

Responsible for service registration and discovery.

---

## 2️⃣ API Gateway

Responsibilities:

- Single Entry Point
- JWT Authentication
- Request Routing
- Service Discovery

---

## 3️⃣ Auth Service

Features:

- User Registration
- User Login
- Password Encryption (BCrypt)
- JWT Generation
- Role-Based Authentication
- ADMIN / CUSTOMER Roles

---

## 4️⃣ Product Service

Features:

- Product CRUD
- Search
- Filtering
- Pagination
- Sorting
- JWT Authorization
- Role-Based Access

---

# 🔐 Authentication Flow

```
Client
   │
   │ Login
   ▼
Auth Service
   │
   │ JWT Token
   ▼
Client
   │
Bearer Token
   ▼
API Gateway
   │
JWT Validation
   ▼
Product Service
```

---

# 👤 Roles

| Role | Permissions |
|------|-------------|
| ADMIN | Full Product Management |
| CUSTOMER | View Products |

---

# 📌 Product Features

- Create Product
- Update Product
- Delete Product
- Get Product By ID
- Get All Products
- Search Products
- Filter Products
- Pagination
- Sorting

---

# 🔒 Security

- Spring Security
- JWT Authentication
- Stateless Session
- Password Encryption
- Role-Based Authorization

---

# 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| Spring Boot | Backend Framework |
| Spring Security | Authentication & Authorization |
| JWT | Token Authentication |
| Spring Cloud Gateway | API Gateway |
| Eureka | Service Discovery |
| Spring Data JPA | Database Access |
| Hibernate | ORM |
| MySQL | Database |
| Maven | Build Tool |
| MapStruct | DTO Mapping |
| Lombok | Boilerplate Reduction |

---

# 🚀 Running the Project

Start the services in the following order:

1. Eureka Server
2. Auth Service
3. Product Service
4. API Gateway

---

# 📈 Upcoming Microservices

- Inventory Service
- Cart Service
- Order Service
- Payment Service
- Notification Service

---

# 👨‍💻 Author

**Ramesh Paswan**

Java Backend Developer

GitHub: https://github.com/YOUR_USERNAME

LinkedIn: https://linkedin.com/in/YOUR_PROFILE

---
⭐ If you like this project, consider giving it a star.
