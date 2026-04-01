# Payment System API

This is a simple backend service built using Spring Boot that supports user authentication, wallet balance management, and money transfers between users.

The focus of this project was to handle things like transaction safety, duplicate requests, and basic security.

---

## Features

* User registration and login (JWT based)
* Wallet creation for each user
* Add money to wallet
* Transfer money between users
* Prevent duplicate transfers using idempotency key
* Handle concurrent transfers safely
* Basic rate limiting
* Webhook call after successful transaction
* Integration test for transfer flow

---

## Tech Stack

* Java
* Spring Boot
* Spring Security
* MsSQL
* JWT

---

## How it works

### Auth

* User registers or logs in
* A JWT token is returned
* Token is required for protected APIs

### Wallet

* Each user gets a wallet on registration
* Users can add money and check balance

### Transfer

* Transfers are handled inside a transaction
* Uses pessimistic locking to avoid race conditions
* Idempotency key ensures same request is not processed twice

## API Endpoints

### Auth

* POST /api/register
* POST /api/login

### Wallet

* POST /api/add
* GET /api/balance/{userId}

### Transactions

* POST /api/transfer
* GET /api/transactions/{userId}

## Sample Transfer Request

Headers:
Authorization: Bearer <token>
Idempotency-Key: abc123

Body:

json
{
  "receiverId": 2,
  "amount": 100
}


## Notes

* Idempotency is implemented using a unique reference ID stored in DB
* Wallet updates are done using DB locking to prevent inconsistent balances
* Transfer is atomic (either fully succeeds or fails)


This was built as part of an assessment to demonstrate backend design and handling of edge cases like concurrency and duplicate requests.
