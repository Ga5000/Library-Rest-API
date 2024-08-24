# LibraryAPI
<a href="https://github.com/Ga5000">![Made with](https://img.shields.io/badge/Spring-green)</a>
<a href="https://github.com/Ga5000">![Made with](https://img.shields.io/badge/Java-red)</a>
<a href="https://github.com/Ga5000">![Made with](https://img.shields.io/badge/Maven-red)</a>
<a href="https://github.com/Ga5000">![Made with](https://img.shields.io/badge/MySQL-blue)</a>
<a href="https://github.com/Ga5000">![Made with](https://img.shields.io/badge/JWT-gray)</a>

## Book Endpoints

### Create Book
- **Method:** POST
- **URL:** `http://localhost:8080/books`
- **Request Body:**
    ```json
    {
      "title": "Effective Java",
      "isbn": "9780134685991",
      "author": "Joshua Bloch",
      "genres": ["Programming", "Software Engineering"],
      "availableCopies": 5,
      "totalCopies": 10,
      "publishedDate": "2018-01-06T00:00:00"
    }
    ```

### Update Book
- **Method:** PUT
- **URL:** `http://localhost:8080/books/1`
- **Request Body:**
    ```json
    {
      "title": "Clean Code",
      "author": "Robert C. Martin",
      "genres": ["Programming", "Software Development"],
      "availableCopies": 3,
      "totalCopies": 7,
      "publishedDate": "2008-08-01T00:00:00"
    }
    ```

### Get Books by Genre
- **Method:** GET
- **URL:** `http://localhost:8080/books/genre/Programming`

### Get Books by Author
- **Method:** GET
- **URL:** `http://localhost:8080/books/author/Robert C. Martin`
- **Auth:** Bearer Token
    ```text
    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsaWJyYXJ5LWFwaSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzI0NDU3NzgwfQ.iyWt_ltBpDGnnIehep25QdhqDA0wpmok7-pRJdsGJfg
    ```

### Get All Book Info
- **Method:** GET
- **URL:** `http://localhost:8080/books/1`

### Delete Book
- **Method:** DELETE
- **URL:** `http://localhost:8080/books/1`

## Auth Endpoints

### Register
- **Method:** POST
- **URL:** `http://localhost:8080/auth/register`
- **Request Body:**
    ```json
    {
      "username": "john_doe",
      "email": "john.doe@example.com",
      "password": "securepassword123",
      "phoneNumber": "123-456-7890"
    }
    ```

### Login
- **Method:** POST
- **URL:** `http://localhost:8080/auth/login`
- **Request Body:**
    ```json
    {
      "username": "admin",
      "password": "a"
    }
    ```

### Logout
- **Method:** POST
- **URL:** `http://localhost:8080/auth/logout`

## Member Endpoints

### Update Member
- **Method:** PUT
- **URL:** `http://localhost:8080/members/2`

### Get Member Details
- **Method:** GET
- **URL:** `http://localhost:8080/members/2`
- **Auth:** Bearer Token
    ```text
    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJsaWJyYXJ5LWFwaSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNzI0NDU3NzgwfQ.iyWt_ltBpDGnnIehep25QdhqDA0wpmok7-pRJdsGJfg
    ```

### Get All Members
- **Method:** GET
- **URL:** `http://localhost:8080/members`

### Get Member by Email
- **Method:** GET
- **URL:** `http://localhost:8080/members/find/email/gbr.lisboa@gmail.com`

### Get Member by Phone Number
- **Method:** GET
- **URL:** `http://localhost:8080/members/find/phone/11994470237`

### Renew Membership
- **Method:** PUT
- **URL:** `http://localhost:8080/members/2/renewMembership`

### Delete Member
- **Method:** DELETE
- **URL:** `http://localhost:8080/members/2`

## Transaction Endpoints

### Create Transaction
- **Method:** POST
- **URL:** `http://localhost:8080/transactions`
- **Request Body:**
    ```json
    {
      "bookId": 1,
      "memberId": 2,
      "transactionType": "BORROW"
    }
    ```

### Get Transaction
- **Method:** GET
- **URL:** `http://localhost:8080/transactions/1`

### Get All Transactions for Member
- **Method:** GET
- **URL:** `http://localhost:8080/transactions/member/2`

### Get All Transactions for Book
- **Method:** GET
- **URL:** `http://localhost:8080/transactions/book/1`

### Renew Transaction
- **Method:** PUT
- **URL:** `http://localhost:8080/transactions/renew/1`
- **Request Body:**
    ```json
    {
      "bookId": 1,
      "memberId": 2,
      "transactionType": "RENEW"
    }
    ```

### Return Transaction
- **Method:** PUT
- **URL:** `http://localhost:8080/transactions/return/1`
- **Request Body:**
    ```json
    {
      "bookId": 1,
      "memberId": 2,
      "transactionType": "RETURN"
    }
    ```

### Delete Transaction
- **Method:** DELETE
- **URL:** `http://localhost:8080/transactions/1`

### Get All Transactions
- **Method:** GET
- **URL:** `http://localhost:8080/transactions`

## Comment Endpoints

### Create Comment
- **Method:** POST
- **URL:** `http://localhost:8080/comments`
- **Request Body:**
    ```json
    {
      "content": "babababa"
    }
    ```

### Get Comment
- **Method:** GET
- **URL:** `http://localhost:8080/comments/1`

### Delete Comment
- **Method:** DELETE
- **URL:** `http://localhost:8080/comments/1`

```bash 
git clone https://github.com/Ga5000/Library-Rest-API.git

cd Library-Rest-API
