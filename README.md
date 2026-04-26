# Vettripath - Student Placement Management System

A full-stack Student Placement Management System built using Spring Boot and React, designed to streamline campus workflows for managing students, colleges, placements, and certifications.

## Overview

Vettripath is a centralized platform that simplifies placement management by providing:

- Role-protected APIs
- Structured backend architecture
- Interactive frontend dashboards
- Real-world database integration

It demonstrates end-to-end product engineering, from backend services to user-facing UI.

## Key Features

- Authentication system (Register, Login, Email Verification)
- Student Management (CRUD + Search + Filters)
- College Management
- Placement Tracking (Year and Qualification filters)
- Certificate Management
- Dashboard Analytics (entity counts)
- Role-based API protection (`role=ADMIN`)

## Screenshots

Add your UI screenshots here to improve project visibility.

```md
![Dashboard](frontend/public/screenshots/dashboard.png)
![Placements](frontend/public/screenshots/placements.png)
![Auth](frontend/public/screenshots/auth.png)
```

## Tech Stack

Backend:

- Java 17+
- Spring Boot
- Spring Data JPA
- Bean Validation
- Maven

Frontend:

- React 19
- Vite
- ESLint

Database:

- PostgreSQL (Local / Neon Cloud)

## Project Structure

```text
TNS_Assignment/
├── demo/                # Spring Boot backend
│   ├── controller/
│   ├── model/
│   ├── repository/
│   ├── service/
│   └── config/
├── frontend/            # React frontend
│   ├── src/
│   └── api.js
└── README.md
```

## Core Functionalities

Students:

- Add, update, delete
- Search by name and hall ticket
- Pagination and filtering

Colleges:

- Full CRUD operations

Placements:

- Manage placement records
- Filter by year and qualification

Certificates:

- CRUD operations

Dashboard:

- Total counts of all entities

## Architecture

- Controller Layer: Handles HTTP requests
- Service Layer: Business logic
- Repository Layer: Database interaction (JPA)
- Validation Layer: Input validation
- Frontend API Module: Centralized API handling

## API Overview

Authentication:

- `POST /auth/register`
- `POST /auth/login`
- `GET /auth/verify?token=...`

Dashboard:

- `GET /dashboard/stats?role=ADMIN`

Students:

- CRUD, search, and pagination endpoints

Colleges:

- CRUD endpoints

Placements:

- CRUD and filter endpoints

Certificates:

- CRUD endpoints

Note: Most endpoints require `?role=ADMIN`.

## Getting Started

Prerequisites:

- Java 17+
- Node.js 18+
- PostgreSQL

## Backend Setup

```bash
cd demo
cp .env.properties.example .env.properties
./mvnw spring-boot:run
```

Runs at: `http://localhost:8080`

## Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Runs at: `http://localhost:5173`

## Configuration

Environment variables:

- `NEON_JDBC_URL`
- `NEON_DB_USER`
- `NEON_DB_PASSWORD`
- `APP_MAIL_USERNAME`
- `APP_MAIL_PASSWORD`

## Validation Commands

Backend:

```bash
cd demo
./mvnw test
```

Frontend:

```bash
npm run build
npm run lint
```

## Troubleshooting

502 Bad Gateway:

- Backend is not running
- Start backend on port 8080

Java Version Errors:

- Ensure Java 17+ is active

```bash
java -version
javac -version
```

## Resume Highlights

- Built a scalable full-stack system with Spring Boot and React
- Designed modular backend architecture with clean separation of concerns
- Implemented role-based access control and authentication flows
- Developed real-world CRUD workflows with filtering and validation
- Integrated dashboard analytics and email verification system

## Future Improvements

- Replace role query parameter with JWT authentication
- Add automated testing (backend and frontend)
- Implement advanced filtering and pagination
- Deploy with Docker and CI/CD pipelines

## License

This project was developed as part of an academic assignment.
