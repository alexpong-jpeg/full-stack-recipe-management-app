# Full-Stack Recipe Management Web Application

## Overview
This project is a full-stack web application that allows users to store, manage, and retrieve personal recipes in a centralized platform. The application is being developed as a capstone project for CMSC 495.

Phase I focuses on establishing the backend foundation of the system, including project setup, database connectivity, entity modeling, service and controller layers, testing, and documentation.

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Spring Security
- Lombok
- Maven
- JUnit 5
- Mockito
- GitHub

## Phase I Features Completed
- Spring Boot project setup
- PostgreSQL datasource configuration
- JPA entity creation for User, Recipe, Ingredient, and Tag
- Repository layer implementation
- Service layer using interfaces and implementation classes
- DTOs for authentication and recipe requests/responses
- REST API endpoints for:
    - user registration
    - user login
    - recipe creation
    - get all recipes
    - get recipe by id
    - search recipes by name
    - delete recipe
- Unit tests for user service and recipe service
- Version control and incremental commits through GitHub

## Project Structure
```text
src
├── main
│   ├── java/com/alexpongchit/recipeapp
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── model
│   │   ├── repository
│   │   ├── security
│   │   ├── service
│   │   └── service/impl
│   └── resources
│       └── application.properties
└── test
    └── java/com/alexpongchit/recipeapp/service
```

## How to Run the Application

### Prerequisites
- Java 17 installed
- Maven installed, or use the included Maven wrapper
- PostgreSQL installed and running
- A PostgreSQL database named `recipeapp`

### Database Setup
Create the database in PostgreSQL:

```sql
CREATE DATABASE recipeapp;
```

Update `src/main/resources/application.properties` with your local database settings.

### Start the Application
Run the project with:

```bash
./mvnw spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

## API Endpoints

### Authentication
- `POST /api/auth/register`
- `POST /api/auth/login`

### Recipes
- `POST /api/recipes`
- `GET /api/recipes`
- `GET /api/recipes/{id}`
- `GET /api/recipes/search?name={recipeName}`
- `DELETE /api/recipes/{id}`

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

## Running Tests
Run all tests with:

```bash
./mvnw test
```

## Notes
This is currently the Phase I implementation of the project. Future phases will expand functionality by improving authentication, adding update endpoints, refining validation, implementing ingredient scaling in business logic, and integrating the frontend.

At this stage, the `/api/recipes` endpoints are temporarily accessible for development and testing purposes. Future phases will secure these endpoints using JWT-based authentication and authorization.