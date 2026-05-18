================================================================================
                            TEAM TASK MANAGER
================================================================================

Team Task Manager is a full-stack, collaborative task management system designed 
to help teams organize projects, track tasks via a Kanban board, and communicate 
effectively using comments. 

================================================================================
1. PROJECT OVERVIEW
================================================================================
The application features a robust backend built with Java and Spring Boot, and 
a modern, responsive frontend built with React and Vite. The system utilizes 
Role-Based Access Control (RBAC), JWT authentication, and a Supabase PostgreSQL 
database.

Key Features:
- User Authentication & Authorization (JWT based)
- Project & Workspace Management
- Role-Based Access Control (Admin vs Member)
  * ADMIN: Create/update/delete projects, manage members, manage all tasks.
  * MEMBER: View assigned tasks, update task status and comments.
- Kanban Board Interface for Task Tracking
- Task Comments & Collaboration
- Dashboard Analytics (Summary & Per-User Metrics)
- Swagger UI for API Documentation

================================================================================
2. TECHNOLOGY STACK
================================================================================

BACKEND:
- Framework: Spring Boot 3.2.5
- Language: Java 17
- Security: Spring Security 6 with JWT (jjwt 0.11.5)
- Database ORM: Spring Data JPA
- Database Migrations: Flyway
- API Documentation: Springdoc OpenAPI (Swagger UI 2.5.0)
- Utilities: Lombok, MapStruct (1.5.5.Final) for DTO mapping
- Build Tool: Maven

FRONTEND:
- Library: React 18.2.0
- Build Tool: Vite 8.0.10
- Styling: Tailwind CSS 3.4.3 & shadcn-style local UI primitives
- Routing: React Router v6 (6.22.3)
- State Management: Zustand 4.5.2
- Data Fetching: Axios 1.7.4 & TanStack React Query 5.37.1

DATABASE:
- Provider: Supabase
- Engine: PostgreSQL

DEPLOYMENT & DEVOPS:
- Containerization: Docker & Docker Compose
- Frontend Hosting: AWS Amplify
- Backend Hosting: AWS App Runner or ECS

================================================================================
3. DIRECTORY STRUCTURE
================================================================================
/
|-- backend/              # Spring Boot Java application
|   |-- src/main/java/    # Java source code (Controllers, Services, Models)
|   |-- src/main/resources# Application properties and Flyway migrations
|   |-- pom.xml           # Maven dependencies
|   |-- Dockerfile        # Backend container configuration
|   |-- .env.example      # Backend environment variables template
|
|-- frontend/             # React application
|   |-- src/              # React components, pages, stores, api services
|   |-- package.json      # NPM dependencies
|   |-- vite.config.js    # Vite configuration
|   |-- tailwind.config.js# Tailwind configuration
|   |-- Dockerfile        # Frontend container configuration
|   |-- amplify.yml       # AWS Amplify deployment settings
|   |-- .env.example      # Frontend environment variables template
|
|-- supabase/             # Database schemas
|   |-- schema.sql        # Supabase PostgreSQL schema dump
|
|-- docker-compose.yml    # Docker Compose for local development
|-- README.md             # Original Markdown documentation

================================================================================
4. API OVERVIEW
================================================================================
The backend exposes the following RESTful API endpoints:

Auth:
- POST /api/auth/register
- POST /api/auth/login
- GET  /api/auth/me

Projects & Members:
- GET    /api/projects
- POST   /api/projects
- GET    /api/projects/{id}
- PUT    /api/projects/{id}
- DELETE /api/projects/{id}
- GET    /api/projects/{id}/members
- POST   /api/projects/{id}/members
- PUT    /api/projects/{id}/members/{userId}
- DELETE /api/projects/{id}/members/{userId}

Tasks:
- GET    /api/projects/{id}/tasks
- POST   /api/projects/{id}/tasks
- GET    /api/tasks/{id}
- PUT    /api/tasks/{id}
- PATCH  /api/tasks/{id}/status
- DELETE /api/tasks/{id}
- GET    /api/tasks/{id}/comments
- POST   /api/tasks/{id}/comments

Dashboard:
- GET /api/dashboard/summary
- GET /api/dashboard/per-user

Swagger UI is available at: /swagger-ui/index.html (when the backend is running)

================================================================================
5. LOCAL DEVELOPMENT SETUP
================================================================================

PREREQUISITES:
- Docker Desktop (Optional but recommended)
- Java 17 & Maven (If running backend natively)
- Node.js & npm (If running frontend natively)

A. USING DOCKER COMPOSE (Recommended)
1. Configure environment variables in `backend/.env.example` and rename to `.env`
2. Configure environment variables in `frontend/.env.example` and rename to `.env`
3. Run the application stack:
   $ docker compose up --build

B. RUNNING NATIVELY (Without Docker)

Backend:
1. Navigate to the backend directory:
   $ cd backend
2. Configure your Supabase/PostgreSQL connection in `application.yml` or `.env`
3. Run the Spring Boot application:
   $ mvn spring-boot:run

Frontend:
1. Navigate to the frontend directory:
   $ cd frontend
2. Install dependencies:
   $ npm install
3. Start the Vite development server:
   $ npm run dev

================================================================================
6. DATABASE SETUP (Supabase)
================================================================================
Option 1: Automatic Migration
Let Flyway create the schema automatically when the backend starts. Point 
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and 
`SPRING_DATASOURCE_PASSWORD` to your Supabase instance.

Option 2: Manual Setup
Paste the contents of `supabase/schema.sql` into the Supabase SQL editor and 
run it.

================================================================================
7. DEPLOYMENT (AWS)
================================================================================
Backend (AWS App Runner):
- Deploy using the `backend/Dockerfile`.
- Set necessary environment variables: SPRING_DATASOURCE_URL, 
  SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD, JWT_SECRET, 
  JWT_EXP_MINUTES, and CORS_ALLOWED_ORIGINS.

Frontend (AWS Amplify):
- Connect the repository and use `frontend/amplify.yml`.
- Set `VITE_API_URL` environment variable pointing to your App Runner backend URL.
