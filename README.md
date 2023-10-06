# FinalProject-SpringBoot-Home-Service-Application
## Home Service Application
## Overview
The Home Service Application is a powerful Spring Boot RESTful API designed to streamline the operations of a home service platform. It leverages a stack of cutting-edge technologies, including Spring Data, Spring MVC, and Spring Security with JWT authentication. 
This README provides an insightful overview of the project's extensive features and functionalities.

## Features and Highlights

### Test-Driven Development (TDD)
The application's robustness is ensured through a Test-Driven Development (TDD) approach. Various unit tests have been created using JUnit, guaranteeing the reliability of the codebase.

### Database and API Testing
The project employs PostgreSQL as the database backend. API endpoints have been thoroughly tested using Postman to ensure seamless data interaction and reliability.

### Data Sorting and Filtering
Data management is made easy with the use of QuerydslPredicateExecutor. Administrators can effortlessly sort and filter data based on a range of criteria, including customer and expert names, expert scores, professions, order statuses, and more.

### Payment Processing with Bootstrap and AJAX
The application features a user-friendly HTML/CSS payment webpage, created using Bootstrap. This page utilizes AJAX to transmit user information seamlessly to the backend, ensuring a smooth and secure payment experience.

### Captcha Verification
To maintain the security of the platform, a server-side captcha verification mechanism has been implemented. Users are required to enter a randomly generated captcha to complete payment transactions. A timeout of 10 minutes has been set for payment pages.

### File Upload Validation
Experts are prompted to upload a profile photo during registration. Photo files are meticulously validated to ensure they meet size and format requirements (less than 300 KB in JPEG format), a task efficiently handled using jmimemagic.

### DTO and Mapping
Data Transfer Objects (DTOs) are employed to facilitate efficient data transfer between layers. Mapstruct serves as a powerful mapper for seamless conversion between DTOs and entities.

### Multilingual Support
The application offers multilingual support through a MessageSourceConfiguration class. Messages displayed in the application can be easily configured in separate properties files for different languages.

### Logging
Logging in the controller layer is managed using Slf4j, ensuring comprehensive visibility into application behavior.

### User Registration and Password Management
Users, both experts and customers, can register new accounts and change their passwords at any time, enhancing account security and management.

## Application Flow
The Home Service Application follows a logical flow to ensure a seamless user experience:

**1.Expert Registration and Service Addition:** Experts register and list their services.

**2.Email Validation:** Expert email addresses are validated through a one-time clickable link sent via email, facilitated by JavaMailSender.

**3.Administrator Approval:** Access is granted to experts upon administrator approval.

**4.Customer Registration and Ordering:** Customers register and place orders for various services, including cleaning, gardening, moving, plumbing, electrical work, and more.

**5.Offer Submission:** Experts provide offers based on relevant orders.

**6.Service Acceptance:** Customers select an offer and await the expert's arrival.

**7.Payment and Feedback:** Customers make payments for services and provide feedback, closing the service cycle.



