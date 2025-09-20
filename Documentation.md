<h1>Documentation : </h1>
<ol>
<li>Patient Controller</li>
<li>Doctor Controller</li>
</ol>


# 1. Patient Controller API Documentation

## Base Information
- **Base URL**: `http://localhost:8080`
- **Controller Path**: `/patient`
- **Authentication**: Session-based authentication using HttpSession
- **Framework**: Spring Boot with JPA Repository

---

## Authentication Endpoints

### 1. Check Registration Availability
**GET** `/patient/register`

**Description**: Checks if a patient is already logged in before allowing registration.

**Headers**:
- `Content-Type: application/json` (auto-handled by browser)
- `Cookie: JSESSIONID={session-id}` (auto-handled by browser)

**Response**:
- **200 OK**: Registration allowed
  ```json
  "Success"
  ```
- **409 CONFLICT**: Patient already logged in
  ```json
  "Patient Already Logged In Please Logout First!"
  ```

---

### 2. Patient Registration
**POST** `/patient/register`

**Description**: Registers a new patient in the system.

**Request Body**:
```json
{
  "username": "john_doe",
  "password": "securePassword123",
  "name": "John Doe",
  "address": "patient-address",
  "email" : "Email",
  "phoneNumber": "patientPhoneNumber"
}
```

**Variable Naming Conventions**:
- `username`: Unique identifier (String, required)
- `password`: Plain text password (String, required)
- `name`: Full patient name (String, required)
- `address`:Patient Address (String, required)
- `email`:Email of the Patient (String, required)
- `phoneNumber`:PhoneNumber of the Patient (String, required)


**Response**:
- **200 OK**: Registration successful
  ```json
  "Patient registered successfully"
  ```
- **409 CONFLICT**: Username already exists
  ```json
  "Patient already exists"
  ```

---

### 3. Patient Login
**POST** `/patient/login`

**Description**: Authenticates patient and creates a session.

**Request Parameters** (form-data):
- `username`: Patient username (String, required)
- `password`: Patient password (String, required)

**Response**:
- **200 OK**: Login successful
  ```json
  "Login Successful"
  ```
- **401 UNAUTHORIZED**: Invalid credentials
  ```json
  "Invalid username or password/Patient Don't Exist!"
  ```
- **409 CONFLICT**: Already logged in
  ```json
  "Patient Already Logged in!"
  ```

---

### 4. Patient Dashboard
**GET** `/patient/dashboard`

**Description**: Retrieves complete patient profile information.

**Authentication**: Required (session-based)

**Response**:
- **200 OK**: Returns complete patient object
  ```json
  {
    "id": "patient123",
    "username": "john_doe",
    "name": "John Doe",
    "age": 30,
    "gender": "Male",
    "height": 175.5,
    "weight": 70.2,
    "bloodgroup": "O+",
    "bloodpressure": "120/80",
    "sugar": "Normal",
    "smoking": false,
    "allergies": "Peanuts, Dust",
    "pastconditions": "None",
    "diseases": [
      {
        "diseaseid": "disease001",
        "description": "Hypertension",
        "dateAdded": "20-09-2025",
        "status": true
      }
    ]
  }
  ```
- **404 NOT FOUND**: Not logged in
  ```json
  "Please Login"
  ```

---

### 5. Patient Logout
**DELETE** `/patient/logout`

**Description**: Invalidates the current patient session.

**Authentication**: Required (session-based)

**Response**:
- **200 OK**: Logout successful
  ```json
  "Logged out successfully"
  ```
- **400 BAD REQUEST**: Not logged in
  ```json
  "Please Login"
  ```

---

## Patient-Specific Operations

### 6. Add Disease
**POST** `/patient/adddisease`

**Description**: Allows patient to add a new disease/condition to their record (initially unverified).

**Authentication**: Required (session-based)

**Request Parameters** (form-data):
- `description`: Disease description (String, minimum 3 characters, required)

**Response**:
- **200 OK**: Disease added successfully
  ```json
  "Disease added successfully"
  ```
- **401 UNAUTHORIZED**: Not logged in
  ```json
  "Please login first"
  ```
- **400 BAD REQUEST**: Invalid description
  ```json
  "Invalid description"
  ```
- **404 NOT FOUND**: Patient not found
  ```json
  "Patient not found"
  ```

**Note**: Disease is automatically assigned:
- Current date in format "dd-MM-yyyy"
- Unique disease ID
- Default status: `false` (unverified)

---

### 7. Check Update Profile Availability
**GET** `/patient/update-profile`

**Description**: Checks if patient is logged in before allowing profile updates.

**Authentication**: Required (session-based)

**Response**:
- **200 OK**: Update allowed
  ```json
  "success"
  ```
- **400 BAD REQUEST**: Not logged in
  ```json
  "Please login"
  ```

---

### 8. Update Patient Profile
**POST** `/patient/update-profile`

**Description**: Updates patient's medical and personal information.

**Authentication**: Required (session-based)

**Request Body**:
```json
{
  "gender": "Male",
  "height": 176.0,
  "weight": 72.0,
  "bloodgroup": "AB+",
  "bloodpressure": "125/85",
  "sugar": "Pre-diabetic",
  "smoking": true,
  "age": 31,
  "allergies": "Shellfish, Pollen",
  "pastconditions": "Broken arm in 2020"
}
```

**Updatable Fields**:
- `gender`: Patient gender (String: "Male"/"Female"/"Other")
- `height`: Height in centimeters (Double)
- `weight`: Weight in kilograms (Double)
- `bloodgroup`: Blood type (String: "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
- `bloodpressure`: Blood pressure reading (String format: "systolic/diastolic")
- `sugar`: Sugar level status (String)
- `smoking`: Smoking status (Boolean)
- `age` :Age of the patient (Integer)
- `allergies`: Known allergies (String, comma-separated)
- `pastconditions`: Past medical conditions (String)

**Response**:
- **200 OK**: Update successful
  ```json
  "Patient updated successfully"
  ```
- **400 BAD REQUEST**: Not logged in
  ```json
  "Please login"
  ```

---

### 9. Generate Digital Patient Card (DPC)
**GET** `/patient/dpc`

**Description**: Retrieves patient data for Digital Patient Card generation.

**Authentication**: Required (session-based)

**Response**:
- **200 OK**: Returns complete patient object (same format as dashboard)
- **400 BAD REQUEST**: Not logged in
  ```json
  "Please login"
  ```

---

## Error Handling

### Common HTTP Status Codes
- **200 OK**: Request successful
- **400 BAD REQUEST**: Client error (invalid input, not logged in)
- **401 UNAUTHORIZED**: Authentication failed
- **404 NOT FOUND**: Resource not found
- **409 CONFLICT**: Resource conflict (already exists, already logged in)

### Session Management
- All authenticated endpoints require active HttpSession
- Session attribute `"username"` stores logged-in patient's username
- Session invalidation occurs on logout or server restart

---

# 2. Doctor Controller API Documentation

## Base Information
- **Base URL**: `http://localhost:8080`
- **Controller Path**: `/doctor`
- **Authentication**: Session-based authentication using HttpSession
- **Framework**: Spring Boot with JPA Repository

---

## Authentication Endpoints

### 1. Check Registration Availability
**GET** `/doctor/register`

**Description**: Checks if a doctor is already logged in before allowing registration.

**Headers**:
- `Content-Type: application/json` (auto-handled by browser)
- `Cookie: JSESSIONID={session-id}` (auto-handled by browser)

**Response**:
- **200 OK**: Registration allowed
  ```json
  "Success"
  ```
- **400 BAD REQUEST**: Doctor already logged in
  ```json
  "Please Login"
  ```

---

### 2. Doctor Registration
**POST** `/doctor/register`

**Description**: Registers a new doctor in the system.

**Authentication**: Must NOT be logged in

**Request Body**:
```json
{
  "username": "dr_smith",
  "password": "doctorPassword123",
  "name": "John Smith",
  "specialization": "Cardiology",
  "certificate": "Certificate Link",
  "phoneNumber": "City General Hospital",
  "gender": "Male",
  "email": "dr.smith@hospital.com"
}
```

**Variable Naming Conventions**:
- `username`: Unique doctor identifier (String, required)
- `password`: Plain text password (String, required)
- `name`: Full doctor name with title (String, required)
- `specialization`: Medical specialization (String)
- `certificate` : Certificate Link (String)
- `phoneNumber`: Phone number (String)
- `gender`: Male/Female (dropdown String)
- `email`: Email address (String)

**Response**:
- **200 OK**: Registration successful
  ```json
  "Doctor Registered successfully!"
  ```
- **409 CONFLICT**: Doctor already logged in
  ```json
  "Doctor Already Logged In Please Logout First!"
  ```

---

### 3. Doctor Login
**POST** `/doctor/login`

**Description**: Authenticates doctor and creates a session.

**Request Parameters** (form-data):
- `username`: Doctor username (String, required)
- `password`: Doctor password (String, required)

**Response**:
- **200 OK**: Login successful
  ```json
  "Logged in successfully!"
  ```
- **404 NOT FOUND**: Invalid credentials
  ```json
  "Invalid username or password"
  ```
- **401 UNAUTHORIZED**: Already logged in
  ```json
  "Doctor Already Logged in"
  ```

---

### 4. Doctor Dashboard
**GET** `/doctor/dashboard`

**Description**: Retrieves complete doctor profile information.

**Authentication**: Required (session-based)

**Response**:
- **200 OK**: Returns complete doctor object
  ```json
  {
    "id": "doctor123",
    "username": "dr_smith",
    "name": "John Smith",
    "specialization": "Cardiology",
    "certificate": "Certificate Link",
    "phoneNumber": "City General Hospital",
    "gender": "Male",
    "email": "dr.smith@hospital.com"
  }
  ```
- **400 BAD REQUEST**: Not logged in
  ```json
  "Please Login"
  ```

---

### 5. Doctor Logout
**POST** `/doctor/logout`

**Description**: Invalidates the current doctor session.

**Authentication**: Required (session-based)

**Response**:
- **200 OK**: Logout successful
  ```json
  "Logged out successfully"
  ```
- **400 BAD REQUEST**: Not logged in
  ```json
  "Please Login"
  ```

---

## Doctor-Specific Operations

### 6. Search Patient
**GET** `/doctor/search`

**Description**: Searches for a patient by username to view their medical records.

**Authentication**: Required (session-based)

**Request Parameters** (query parameters):
- `patientusername`: Patient's username (String, required)

**Example Request**:
```
GET /doctor/search?patientusername=john_doe
```

**Response**:
- **200 OK**: Returns complete patient object with medical history
  ```json
  {
    "id": "patient123",
    "username": "john_doe",
    "name": "John Doe",
    "age": 30,
    "gender": "Male",
    "height": 175.5,
    "weight": 70.2,
    "bloodgroup": "O+",
    "bloodpressure": "120/80",
    "sugar": "Normal",
    "smoking": false,
    "allergies": "Peanuts, Dust",
    "pastconditions": "None",
    "diseases": [
      {
        "diseaseid": "disease001",
        "description": "Hypertension",
        "dateAdded": "20-09-2025",
        "status": false
      },
      {
        "diseaseid": "disease002",
        "description": "Diabetes Type 2",
        "dateAdded": "15-09-2025",
        "status": true
      }
    ]
  }
  ```
- **404 NOT FOUND**: Patient not found
  ```json
  "Patient Not Found"
  ```
- **400 BAD REQUEST**: Not logged in
  ```json
  "Please Login"
  ```

---

### 7. Verify/Unverify Disease
**POST** `/doctor/verify`

**Description**: Toggles the verification status of a specific disease in a patient's record. This is the core functionality for doctor verification workflow.

**Authentication**: Required (session-based)

**Request Parameters** (form-data):
- `patientId`: Patient's unique ID (String, required)
- `diseaseid`: Disease's unique ID (String, required)

**Business Logic**:
- If disease status is `true` (verified) → changes to `false` (unverified)
- If disease status is `false` (unverified) → changes to `true` (verified)

**Response**:
- **200 OK**: Status updated successfully
  ```json
  {
    "message": "Status updated successfully",
    "diseaseId": "disease001",
    "newStatus": true
  }
  ```
- **404 NOT FOUND**: Disease not found
  ```json
  "Disease Not Found"
  ```
- **404 NOT FOUND**: Patient not found
  ```json
  "Patient Not Found"
  ```
- **400 BAD REQUEST**: Not logged in
  ```json
  "Please Login"
  ```


## Verification Workflow

### Patient-Doctor Interaction Flow :
1. **Patient adds condition**: Patient uses `/patient/adddisease` → creates disease with `status: false`
2. **Doctor searches patient**: Doctor uses `/doctor/search` → views all patient data
3. **Doctor reviews conditions**: Doctor sees diseases with verification indicators
4. **Doctor verifies/unverifies**: Doctor uses `/doctor/verify` → toggles status
5. **Patient views updated card**: Patient uses `/patient/dpc` → sees verified/unverified tags

### Doctor Verification Workflow :
1. Doctor searches for patient using `/doctor/search?patientusername=john_doe`
2. Reviews patient's diseases list, identifies unverified entries (`status: false`)
3. Calls `/doctor/verify` with patient ID and specific disease ID
4. Disease status toggles from `false` to `true` (now verified by doctor)
5. Patient's digital card will now show this condition as "Verified"

---


### Verification States
- **Unverified (`status: false`)**:
    - Patient-added information
    - Awaiting doctor review
    - Displayed as "Unverified" on patient card

- **Verified (`status: true`)**:
    - Doctor-approved information
    - Medically validated
    - Displayed as "Verified" on patient card

---
