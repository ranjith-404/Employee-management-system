# Employee Management System - Testing Guide

## Prerequisites

1. **Java 17+** installed
2. **Maven** installed
3. **MySQL** running on localhost:3306
4. **MySQL credentials**: root/root (or update application.properties)

---

## Step 1: Setup Database

Open MySQL Workbench or terminal and run:

```sql
SOURCE database/init.sql;
```

Or copy-paste the contents of `database/init.sql` into MySQL.

This creates:
- 5 departments
- 8 employees
- 5 users
- Sample attendance, leaves, and payroll data

---

## Step 2: Start Backend (Spring Boot)

Open terminal in the project root:

```bash
cd employee-management
mvn spring-boot:run
```

Wait for:
```
Started EmployeeManagementApplication in X.XX seconds
```

Backend is now running at: `http://localhost:8080`

---

## Step 3: Test APIs with cURL

### Auth - Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### Get All Employees
```bash
curl http://localhost:8080/api/employees
```

### Get All Departments
```bash
curl http://localhost:8080/api/departments
```

### Create Employee
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@company.com",
    "phone": "555-0109",
    "hireDate": "2026-07-16",
    "deptId": 2
  }'
```

### Check In Attendance
```bash
curl -X POST http://localhost:8080/api/attendance/check-in/2
```

### Apply for Leave
```bash
curl -X POST http://localhost:8080/api/leaves \
  -H "Content-Type: application/json" \
  -d '{
    "empId": 2,
    "leaveType": "CASUAL",
    "startDate": "2026-07-20",
    "endDate": "2026-07-21",
    "reason": "Personal work"
  }'
```

---

## Step 4: Start Frontend

Open `frontend/index.html` in your browser:

```
file:///C:/Users/Admin/OneDrive/Documents/Default%20Project/employee-management/frontend/index.html
```

Or use VS Code Live Server extension.

---

## Step 5: Test Frontend

### Login Credentials

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| mchen | emp123 | EMPLOYEE |
| erodriguez | emp123 | EMPLOYEE |

### Test Workflow

1. **Login** as admin
2. **Dashboard** - Verify stats show correct counts
3. **Employees** - Add, edit, search, toggle status
4. **Departments** - Add, delete
5. **Attendance** - Check in/out (login as employee first)
6. **Leaves** - Apply, approve/reject (as admin)
7. **Payroll** - Generate, mark as paid
8. **Users** - Add new user

---

## API Endpoints Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/login | Login |
| POST | /api/auth/register | Register user |
| GET | /api/employees | List all |
| POST | /api/employees | Create |
| PUT | /api/employees/{id} | Update |
| DELETE | /api/employees/{id} | Delete |
| PATCH | /api/employees/{id}/toggle-status | Toggle active |
| GET | /api/employees/search?keyword= | Search |
| GET | /api/departments | List all |
| POST | /api/departments | Create |
| POST | /api/attendance/check-in/{empId} | Check in |
| POST | /api/attendance/check-out/{empId} | Check out |
| POST | /api/leaves | Apply leave |
| PUT | /api/leaves/{id}/approve | Approve |
| PUT | /api/leaves/{id}/reject | Reject |
| POST | /api/payroll | Generate |
| PUT | /api/payroll/{id}/mark-paid | Mark paid |

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Port 8080 in use | Change `server.port` in application.properties |
| MySQL connection failed | Check MySQL is running, credentials match |
| CORS error | Verify `@CrossOrigin` annotation on controllers |
| Frontend can't connect | Ensure backend is running on port 8080 |
