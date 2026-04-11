# LeaveScheduler

LeaveScheduler is a leave management system with:

- Spring Boot backend
- React frontend
- MySQL database
- JWT auth
- Docker support

## Ports

- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080`
- Docker MySQL: `localhost:3307`

## Run Locally

### 1. Requirements

- Java 21
- Maven
- Node.js
- MySQL 8

### 2. Create database

```sql
CREATE DATABASE IF NOT EXISTS leave_scheduler;
```

### 3. Run seed SQL once

```powershell
mysql -u root -p leave_scheduler < database/init.sql
```

### 4. Start backend

```powershell
cd backend
mvn spring-boot:run
```

### 5. Start frontend

Open another terminal:

```powershell
cd frontend
npm install
npm start
```

### 6. Open app

```text
http://localhost:3000
```

## Run With Docker

### 1. Stop local frontend/backend first

### 2. Start containers

```powershell
docker compose up --build
```

### 3. Open app

```text
http://localhost:3000
```

### 4. Stop containers

```powershell
docker compose down
```

## First Setup Flow

Use this order:

1. Start project
2. Create admin
3. Login as admin
4. Create manager and employee
5. Assign manager to employee
6. Login as employee and apply leave
7. Login as admin or manager and approve leave

## Create First Admin

Use Postman:

```http
POST http://localhost:8080/api/auth/bootstrap-admin
```

Headers:

```http
Content-Type: application/json
X-Admin-Setup-Key: SubbuAdminSetup2026
```

Body:

```json
{
  "username": "subashadmin",
  "password": "Subbu@895",
  "email": "subashadmin@company.com",
  "firstName": "Subash",
  "lastName": "Admin",
  "department": "MANAGEMENT",
  "designation": "System Administrator"
}
```

## Login

```http
POST http://localhost:8080/api/auth/login
```

```json
{
  "username": "subashadmin",
  "password": "Subbu@895"
}
```

## Create Manager

Login as admin first and use the returned token.

```http
POST http://localhost:8080/api/admin/users
Authorization: Bearer <admin_token>
Content-Type: application/json
```

```json
{
  "username": "john.manager",
  "password": "Manager@123",
  "email": "john.manager@company.com",
  "firstName": "John",
  "lastName": "Manager",
  "role": "MANAGER",
  "department": "ENGINEERING",
  "designation": "Engineering Manager",
  "managerId": null
}
```

## Create Employee

### Option A: Employee self-register

Frontend:

```text
http://localhost:3000/register
```

API:

```http
POST http://localhost:8080/api/auth/register
```

```json
{
  "username": "jane.employee",
  "password": "Emp@12345",
  "email": "jane.employee@company.com",
  "firstName": "Jane",
  "lastName": "Employee",
  "department": "ENGINEERING",
  "designation": "Software Engineer"
}
```

### Option B: Admin creates employee

```http
POST http://localhost:8080/api/admin/users
Authorization: Bearer <admin_token>
Content-Type: application/json
```

```json
{
  "username": "jane.employee",
  "password": "Emp@12345",
  "email": "jane.employee@company.com",
  "firstName": "Jane",
  "lastName": "Employee",
  "role": "EMPLOYEE",
  "department": "ENGINEERING",
  "designation": "Software Engineer",
  "managerId": null
}
```

## Assign Manager To Employee

First get user IDs:

```http
GET http://localhost:8080/api/admin/users
Authorization: Bearer <admin_token>
```

Then update employee:

```http
PUT http://localhost:8080/api/admin/users/{employeeId}
Authorization: Bearer <admin_token>
Content-Type: application/json
```

```json
{
  "managerId": 2
}
```

## Apply Leave

Login as employee and use frontend `Apply Leave`, or use Postman:

```http
POST http://localhost:8080/api/leaves/apply
Authorization: Bearer <employee_token>
Content-Type: application/json
```

```json
{
  "leaveType": "ANNUAL",
  "fromDate": "2026-04-10",
  "toDate": "2026-04-11",
  "reason": "Personal work",
  "addressDuringLeave": "Bengaluru",
  "superiorEmail": "john.manager@company.com"
}
```

## Approve Leave

Login as admin or manager and use frontend `Approvals`, or use Postman:

```http
PUT http://localhost:8080/api/manager/leaves/{leaveId}/action
Authorization: Bearer <admin_or_manager_token>
Content-Type: application/json
```

Approve:

```json
{
  "action": "APPROVE",
  "remarks": "Approved"
}
```

Reject:

```json
{
  "action": "REJECT",
  "remarks": "Insufficient notice"
}
```

## Important API Endpoints

- `POST /api/auth/login`
- `POST /api/auth/register`
- `POST /api/auth/bootstrap-admin`
- `POST /api/auth/change-password`
- `POST /api/leaves/apply`
- `GET /api/leaves/my`
- `GET /api/leaves/balance`
- `PUT /api/leaves/{id}/withdraw`
- `GET /api/manager/pending`
- `GET /api/manager/team-leaves`
- `PUT /api/manager/leaves/{id}/action`
- `GET /api/admin/users`
- `POST /api/admin/users`
- `PUT /api/admin/users/{id}`
- `DELETE /api/admin/users/{id}`
- `GET /api/holidays`
- `POST /api/holidays`
- `DELETE /api/holidays/{id}`
- `GET /api/reports/leave-summary`

## Valid Roles

- `EMPLOYEE`
- `MANAGER`
- `BUSINESS_MANAGER`
- `MANAGING_DIRECTOR`
- `ADMIN`

## Valid Departments

- `ENGINEERING`
- `MANAGEMENT`
- `HR`
- `FINANCE`
- `OPERATIONS`

## Docker Push

If your Docker Hub username is `subashbr`:

```powershell
docker tag subashleavescheduler/backend:latest subashbr/leavescheduler-backend:latest
docker tag subashleavescheduler/frontend:latest subashbr/leavescheduler-frontend:latest
docker push subashbr/leavescheduler-backend:latest
docker push subashbr/leavescheduler-frontend:latest
```

## Quick Troubleshooting

### Port already in use

Check:

```powershell
Get-NetTCPConnection -LocalPort 8080,3000
```

### Login returns 403

- Use plain password, not DB hash
- Use `No Auth` in Postman for login
- Make sure username/password are correct

### Bootstrap admin says header missing

Send:

```http
X-Admin-Setup-Key: SubbuAdminSetup2026
```

### Bootstrap admin says username already exists

Use a different username or delete the existing user from MySQL.

## Note Before Public Git Push

This project still contains local passwords and setup keys in config files. Replace them with environment variables before pushing publicly.
