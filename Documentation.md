# Patient Controller API Documentation

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
- `Content-Type: application/json`
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
  "age": 30,
  "gender": "Male",
  "height": 175.5,
  "weight": 70.2,
  "bloodgroup": "O+",
  "bloodpressure": "120/80",
  "sugar": "Normal",
  "smoking": false,
  "allergies": "Peanuts, Dust",
  "pastconditions": "None"
}
```

**Variable Naming Conventions**:
- `username`: Unique identifier (String, required)
- `password`: Plain text password (String, required)
- `name`: Full patient name (String, required)
- `age`: Patient age in years (Integer)
- `gender`: Patient gender (String: "Male"/"Female"/"Other")
- `height`: Height in centimeters (Double)
- `weight`: Weight in kilograms (Double)
- `bloodgroup`: Blood type (String: "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
- `bloodpressure`: Blood pressure reading (String format: "systolic/diastolic")
- `sugar`: Sugar level status (String)
- `smoking`: Smoking status (Boolean)
- `allergies`: Known allergies (String, comma-separated)
- `pastconditions`: Past medical conditions (String)

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
- `gender`: Patient gender
- `height`: Height in centimeters
- `weight`: Weight in kilograms
- `bloodgroup`: Blood type
- `bloodpressure`: Blood pressure reading
- `sugar`: Sugar level status
- `smoking`: Smoking status (boolean)
- `age`: Age in years
- `allergies`: Allergies list (converted to string)
- `pastconditions`: Past conditions (converted to string)

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

## Data Models

### Patient Entity Fields
```java
// Core identification
private String id;              // Auto-generated patient ID
private String username;        // Unique login identifier
private String password;        // Plain text (consider hashing)
private String name;            // Full patient name

// Demographics
private int age;                // Age in years
private String gender;          // Gender identity

// Physical measurements
private double height;          // Height in centimeters
private double weight;          // Weight in kilograms
private String bloodgroup;      // ABO blood type with Rh factor

// Medical indicators
private String bloodpressure;   // Format: "systolic/diastolic"
private String sugar;           // Sugar level description
private boolean smoking;        // Smoking status

// Medical history
private String allergies;       // Comma-separated allergy list
private String pastconditions;  // Past medical conditions
private List<Disease> diseases; // Associated diseases/conditions
```

### Disease Entity Fields
```java
private String diseaseid;       // Unique disease identifier
private String description;     // Disease/condition description
private String dateAdded;       // Date added (format: "dd-MM-yyyy")
private boolean status;         // Verification status (true = verified)
```

---

## Security Considerations
1. **Password Security**: Currently storing plain text passwords - implement hashing
2. **Input Validation**: Validate all input parameters and request bodies
3. **Session Security**: Consider session timeout and CSRF protection
4. **Data Sanitization**: Sanitize user inputs to prevent injection attacks
5. **Access Control**: Ensure patients can only access their own data

---

## Best Practices
1. Use descriptive variable names following camelCase convention
2. Implement proper exception handling for database operations
3. Add request/response logging for debugging
4. Consider implementing DTO (Data Transfer Objects) for API responses
5. Add comprehensive input validation and sanitization
6. Implement proper HTTP status codes for all scenarios