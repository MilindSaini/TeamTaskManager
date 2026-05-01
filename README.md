# Team Task Manager

Full-stack collaborative task management system with Spring Boot 3, React 18, JWT auth, project RBAC, Supabase PostgreSQL, Flyway migrations, dashboard analytics, task comments, search, filters, and a Kanban board.

## Stack

- Backend: Spring Boot 3, Java 17, Spring Security 6, JWT, Spring Data JPA, MapStruct, Flyway
- Frontend: React 18, Vite, Tailwind CSS, shadcn-style local UI primitives, Zustand, React Router v6, Axios, React Query
- Database: Supabase PostgreSQL
- AWS deployment: App Runner or ECS for backend, Amplify Hosting for frontend

## Local Development

Backend environment variables are listed in `backend/.env.example`.

Frontend environment variables are listed in `frontend/.env.example`.

Run with Docker Compose:

```bash
docker compose up --build
```

Build the backend jar directly if you want to deploy it without Docker:

```bash
cd backend
mvn -DskipTests package
```

Run without Docker:

```bash
cd backend
mvn spring-boot:run
```

```bash
cd frontend
npm install
npm run dev
```

## Supabase Schema

Use either option:

1. Let Flyway create the schema when the backend starts by pointing `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` to Supabase.
2. Paste `supabase/schema.sql` into the Supabase SQL editor and run it.

For Supabase pooler connections, use SSL in the JDBC URL, for example:

```text
jdbc:postgresql://aws-0-region.pooler.supabase.com:6543/postgres?sslmode=require
```

Set `APP_DB_SCHEMA=teamtaskmanager` to keep this app in its own schema. If you already ran `supabase/schema.sql` manually for that schema before starting the backend, set `SPRING_FLYWAY_BASELINE_ON_MIGRATE=true` once so Flyway can create its schema history table for the existing schema.

## RBAC

- `ADMIN`: create/update/delete projects, manage members, create/assign/delete tasks, update any task status.
- `MEMBER`: sees assigned tasks only, can update assigned task status and comments.

## API Overview

Auth:
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

Projects and members:
- `GET /api/projects`
- `POST /api/projects`
- `GET /api/projects/{id}`
- `PUT /api/projects/{id}`
- `DELETE /api/projects/{id}`
- `GET /api/projects/{id}/members`
- `POST /api/projects/{id}/members`
- `PUT /api/projects/{id}/members/{userId}`
- `DELETE /api/projects/{id}/members/{userId}`

Tasks:
- `GET /api/projects/{id}/tasks`
- `POST /api/projects/{id}/tasks`
- `GET /api/tasks/{id}`
- `PUT /api/tasks/{id}`
- `PATCH /api/tasks/{id}/status`
- `DELETE /api/tasks/{id}`
- `GET /api/tasks/{id}/comments`
- `POST /api/tasks/{id}/comments`

Dashboard:
- `GET /api/dashboard/summary`
- `GET /api/dashboard/per-user`

Swagger UI:
- `/swagger-ui/index.html`

## AWS Deployment

Backend on AWS App Runner:

- Build from `backend/Dockerfile` with the repository root as the Docker build context, or connect the repository directly.
- Set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, `JWT_EXP_MINUTES`, and `CORS_ALLOWED_ORIGINS`.
- Set `CORS_ALLOWED_ORIGINS` to your Amplify frontend URL.

Frontend on AWS Amplify:

- Use `frontend/amplify.yml`.
- Add `VITE_API_URL` in Amplify environment variables and point it to the App Runner backend URL.

Docker frontend builds need the API URL at build time:

```bash
docker build --build-arg VITE_API_URL=https://your-backend.awsapprunner.com -t team-task-manager-frontend ./frontend
```

## Verified

- Backend compiles with `mvn test`.
- Frontend production bundle compiles with `npm run build`.
