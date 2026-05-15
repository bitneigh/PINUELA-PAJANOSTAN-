# TechStore E-Commerce (Laboratory 9)



---

## 1. Security Architecture

The application implements a robust **Session-Based Authentication** model combined with global filters to secure stateful data and protect specific presentation layers.

### Mechanism Flow:
* **Session Lifecycle:** Upon successful authentication via `/api/v1/auth/login`, Spring Security creates a server-side session (`HttpSession`) if required.
* **Token Identifier:** The server returns a secure cookie identifier called `JSESSIONID` to the client container.
* **State Verification:** For every subsequent state-changing or secured operation, the client automatically passes the session cookie back to the server, matching the pointer to grant access.
* **Frontend Interceptors:** Dynamic scripts are embedded natively within the `<head>` of sensitive pages (`checkout.html`, `admin.html`) to intercept client loading, pinging session validation endpoints before rendering the DOM structure to mitigate layout-flickering leaks.

---

## 2. Validation Rules

To preserve data integrity, server-side entity validations are applied using standard constraints before saving to the persistent MySQL layer:

| Entity / Field | Validation Constraint | Description / Failure Log |
| :--- | :--- | :--- |
| **User: username** | `@NotBlank`, `@Size(min = 4)` | Cannot be empty; must contain a user-friendly identifier. |
| **User: password** | `@NotBlank`, `@Size(min = 8)` | Must meet modern entropy requirements. |
| **Product: name** | `@NotBlank` | Product name is strict and cannot be empty. |
| **Product: price** | `@DecimalMin(value = "0.01")` | Price cannot be negative or zero. |

---

## 3. API Reference & Authentication Requirements

The following matrix lists the final endpoints exposed by the backend API and their corresponding access control configurations:

| HTTP Method | API Endpoint | Auth Required? | Allowed Roles / Scope |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/v1/auth/register` | ❌ No | Public registration for all clients |
| **POST** | `/api/v1/auth/login` | ❌ No | Process form authentication |
| **POST** | `/api/v1/auth/logout` |  Yes | Invalidates active `JSESSIONID` |
| **GET** | `/api/v1/products` |  Yes | Authenticated Users (`ROLE_USER`, `ROLE_ADMIN`) |
| **POST** | `/api/v1/products` |  Yes | Restrictive Scope (`ROLE_ADMIN` Only) |
| **GET** | `/api/v1/users/me` |  Yes | Current active session verification |

---

## 4. User-Friendly Errors

* **Error Resolution:** Custom failure handlers return structured, clean JSON responses with deterministic HTTP error numbers (`401 Unauthorized`, `403 Forbidden`, `400 Bad Request`) instead of throwing stack traces directly to the client view interface.


🛒 Ecommerce API Project
=======
# TechStore E-Commerce Project (LABORATORY 8)

---

## 🚀 Task 9: Documentation & Submission

### 📊 Database Schema
Our system uses a relational database structure to manage products and order consistency.
* **Table:** `products`
   * `id` (Primary Key, Long): Unique identifier for each product.
   * `name` (String): The name of the tech product.
   * `price` (Double): The unit price in PHP.
   * `description` (Text): Detailed product information.
   * `image_url` (String): Path or URL to the product image.
   * `category` (String): Product classification (e.g., Audio, Wearables).

### 🔌 API Endpoints
The following endpoints are implemented in the Spring Boot backend:

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/api/products` | Fetches all products from the database. |
| **GET** | `/api/products/{id}` | Fetches a specific product by its ID. |

### 📸 Evidence of Success

#### Database Table
![Database Table](EcommerceApi/documentation/db_table.png)

#### Browser Console Fetch
![Console Fetch](EcommerceApi/documentation/console_fetch.png)


----

# 🛒 Ecommerce API Project (LABORATORY 7)

-----

## 📝 Project Overview
This project is a **Spring Boot REST API** developed for managing product information in an e-commerce system. It features a complete set of CRUD (Create, Read, Update, Delete) operations and specialized filtering logic. This application was built as part of the **WS101 (Web Systems and Technologies)** laboratory requirements.

## 🚀 Technologies Used
* **Java 17**
* **Spring Boot 3.x** (Web & Validation Starter)
* **Gradle** (Build Automation)
* **In-Memory Storage** (Java ArrayList)
* **Git** (Version Control)

## 🛠️ Setup & Installation
1. **Clone the Repository:**
   ```bash
   git clone <https://github.com/bitneigh/PINUELA-PAJANOSTAN->

## 📌 API Endpoints Reference

| Method | Endpoint | Description | Expected Status |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/v1/products` | Get all products | 200 OK |
| **GET** | `/api/v1/products/{id}` | Get product by ID | 200 OK / 404 |
| **GET** | `/api/v1/products/filter` | Filter by category | 200 OK |
| **POST** | `/api/v1/products` | Create new product | 201 Created |
| **PUT** | `/api/v1/products/{id}` | Update product | 200 OK / 404 |
| **DELETE** | `/api/v1/products/{id}` | Delete product | 204 No Content |


## 👥 Contributors
* **Britney Ashley Pinuela** - *Lead Developer / Documentation*
* **Stephanie Pajanostan** - *Pair Programmer / Quality Assurance*

> **Pair Programming Session:** This project was developed collaboratively.
> 
> Co-authored-by: Britney Ashley Pinuela <bitneighpinuela@gmail.com>
> 
> Co-authored-by: Stephanie Pajanostan <pajanostanstephanie15@gmail.com>


