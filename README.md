# Full-Stack Recipe Management Web Application — Backend

## Overview
This backend powers a full-stack recipe management web application developed for CMSC 495. It provides REST APIs for user authentication, recipe management, recipe search, ingredient scaling, and current-user lookup for the Angular frontend.

The backend is built with Spring Boot and PostgreSQL and now reflects the Phase II implementation of the project, including JWT-based authentication, protected recipe endpoints, validation, global exception handling, and service/controller test coverage.

## Tech Stack
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Spring Security**
- **JWT (JJWT)**
- **Lombok**
- **Maven**
- **JUnit 5**
- **Mockito**
- **GitHub**

## Phase II Features Completed
- Spring Boot backend setup and project structure
- PostgreSQL database integration
- JPA entity modeling for:
  - User
  - Recipe
  - Ingredient
  - Tag
- Repository layer for persistence and lookup operations
- Service layer using interfaces and implementation classes
- DTO layer for request and response payloads
- BCrypt password hashing for user registration
- JWT-based authentication for login and protected API access
- Spring Security configuration with stateless session handling
- CORS configuration to support the Angular frontend
- Global exception handling with structured error responses
- Recipe API features including:
  - create recipe
  - get all recipes
  - get recipe by ID
  - search recipes by name
  - update recipe
  - delete recipe
  - scale recipe ingredients
- Current authenticated user lookup endpoint for frontend recipe ownership
- Service and controller test coverage for:
  - authentication
  - recipe CRUD behavior
  - ingredient scaling
  - validation and not-found scenarios

## Project Structure
```text
src
├── main
│   ├── java/com/alexpongchit/recipeapp
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── exception
│   │   ├── model
│   │   ├── repository
│   │   ├── security
│   │   ├── service
│   │   └── service/impl
│   └── resources
│       └── application.properties
└── test
    └── java/com/alexpongchit/recipeapp
        ├── controller
        └── service
```

## How to Run the Application

### Prerequisites
* Java 17 installed
* Maven installed, or use the included Maven wrapper
* PostgreSQL installed and running
* A PostgreSQL database named `recipeapp`

### Database Setup
Create the database in PostgreSQL:

```sql
CREATE DATABASE recipeapp;
```

Update `src/main/resources/application.properties` with your local database settings.
You also need JWT configuration values in `application.properties`, such as:
* `jwt.secret`
* `jwt.expiration`

### Start the Application
Run the backend with:

```bash
./mvnw spring-boot:run
```

The backend starts on: `http://localhost:8080`

## Security Notes
* Passwords are encoded with BCrypt before being saved.
* Login returns a JWT token.
* Recipe endpoints require authentication.
* The Angular frontend sends the JWT in the `Authorization: Bearer <token>` header.
* CORS is configured to allow requests from `http://localhost:4200`.

## API Endpoints

### Authentication
* `POST /api/auth/register`
* `POST /api/auth/login`

### Current User
* `GET /api/users/me`

### Recipes
* `POST /api/recipes`
* `GET /api/recipes`
* `GET /api/recipes/{id}`
* `GET /api/recipes/search?name={recipeName}`
* `PUT /api/recipes/{id}`
* `DELETE /api/recipes/{id}`
* `POST /api/recipes/{id}/scale`

## Example Request Payloads

### Register User
```json
{
  "username": "alex",
  "email": "alex@example.com",
  "password": "password123"
}
```

### Login User
```json
{
  "username": "alex",
  "password": "password123"
}
```

### Create Recipe
```json
{
  "name": "Chicken and Rice",
  "instructions": "Cook chicken, cook rice, combine.",
  "userId": 1,
  "ingredients": [
    { "name": "Chicken Breast", "quantity": 2, "unit": "pieces" },
    { "name": "Rice", "quantity": 1, "unit": "cup" }
  ],
  "tags": ["high-protein", "meal prep"]
}
```

### Update Recipe
```json
{
  "name": "Updated Chicken and Rice",
  "instructions": "Season chicken, cook rice, and combine everything.",
  "userId": 1,
  "ingredients": [
    { "name": "Chicken Breast", "quantity": 3, "unit": "pieces" },
    { "name": "Rice", "quantity": 2, "unit": "cups" },
    { "name": "Olive Oil", "quantity": 1, "unit": "tbsp" }
  ],
  "tags": ["high-protein", "bulk prep", "dinner"]
}
```

### Scale Recipe
```json
{
  "originalServings": 2,
  "desiredServings": 4
}
```

## Running Tests
Run all tests with:

```bash
./mvnw test
```

## Frontend Integration
This backend is designed to work with the Angular frontend for the same project. During local development:

* **Backend:** `http://localhost:8080`
* **Frontend:** `http://localhost:4200`

The frontend uses:
* `/api/auth/register` for user registration
* `/api/auth/login` for authentication
* `/api/users/me` for current-user lookup
* `/api/recipes/**` for authenticated recipe operations

## Notes
This backend now represents the Phase II implementation of the project. It supports secure authentication, recipe ownership, CRUD operations, ingredient scaling, structured error handling, and frontend integration needed for the full-stack capstone workflow.