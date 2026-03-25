\# Payment Microservices System



\## Overview



This project is a microservices-based payment processing platform built using Java and Spring Boot.

It demonstrates service decomposition, service registration, inter-service communication, and payment workflow handling in a distributed architecture.



The system is designed to simulate a real payment ecosystem where multiple independent services collaborate to process transactions securely and efficiently.



---



\## Project Structure



repo-db/              → payment-database

repo-eureka/          → service-registry

repo-processing/      → payment-processing-service

repo/                 → payment-provider-service



---



\## Microservices Included



\### 1. Eureka Service Registry



Service discovery server used for registering and locating microservices dynamically.



\### 2. Payment Processing Service



Handles transaction processing, payment validation, reconciliation, and provider routing.



\### 3. Paypal Provider Service



Simulates payment provider integration for creating and managing payment orders.



\### 4. Payment Database Module



Contains database scripts and schema for payment transaction persistence.



---



\## Technology Stack



\* Java

\* Spring Boot

\* Spring Cloud Eureka

\* REST API

\* Maven

\* JPA / Hibernate

\* MySQL / SQL Scripts

\* Postman

\* Git / GitHub



---






\## Architecture Flow



Client Request

↓

Payment Processing Service

↓

Provider Service (Paypal Provider)

↓

Database Persistence

↓

Response to Client



All services are registered through Eureka Service Registry for service discovery.



---



\## API Flow



\### Payment Processing Service



\* Validate request

\* Identify payment provider

\* Route to provider service

\* Save transaction

\* Return response



\### Paypal Provider Service



\* Create payment order

\* Generate provider response

\* Handle external provider simulation



---



\## Features



\* Microservice architecture

\* Service discovery using Eureka

\* Provider integration design

\* Transaction persistence

\* Enum-based payment state management

\* Exception handling

\* Unit testing support



---



\## Key Concepts Demonstrated



\* Distributed system design

\* RESTful communication

\* Service-to-service interaction

\* Layered backend architecture

\* DTO / DAO / Entity separation

\* Converter usage

\* Environment-specific configuration



---



\## How to Run



\### Start Eureka Server



Run Eureka service first.



\### Start Payment Processing Service



Run processing service after Eureka registration.



\### Start Paypal Provider Service



Run provider service after processing service.


Test APIs in Postman.

---



\## Configuration Profiles



application-local.properties

application-dev.properties

application-qa.properties

application-prod.properties



---



\## Future Enhancements



\* API Gateway integration

\* Circuit breaker using Resilience4j

\* Centralized logging

\* Docker deployment

\* Kubernetes orchestration



---



\## Author



Muktar Islam



Java Spring Boot Backend Developer



