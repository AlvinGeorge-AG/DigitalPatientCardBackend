# Digital Patient Card Backend

## Description
This repository contains the backend services for a Digital Patient Card system. It is designed to provide robust APIs for managing various aspects of patient healthcare data, including patient, doctor, and administrator information. A key feature is the ability to generate digital patient cards in PDF format. The backend is built to serve a dedicated frontend application.

## Features
*   **User Role Management**: Distinct functionalities and data management for Administrators, Doctors, and Patients.
*   **Patient Data Management**: APIs for creating, retrieving, updating, and deleting patient records, including disease information.
*   **Doctor Data Management**: APIs for managing doctor profiles and associated medical referrals.
*   **Administrator Management**: APIs for administrative tasks and managing other users.
*   **Disease Tracking**: Functionality to record and manage patient disease details.
*   **Referral System**: Management of patient referral information.
*   **Digital Patient Card PDF Generation**: Services to dynamically generate and retrieve patient cards in PDF format.
*   **RESTful API**: Exposes a set of well-defined RESTful endpoints for integration with client applications.
*   **CORS Configuration**: Pre-configured Cross-Origin Resource Sharing to allow secure communication with a frontend application.
*   **Database Persistence**: Utilizes an Object-Relational Mapping (ORM) framework for efficient data storage and retrieval.

## Tech Stack
*   **Language**: Java
*   **Framework**: Spring Boot
*   **Database**: PostgreSQL
*   **ORM**: Hibernate / JPA
*   **Build Tool**: Apache Maven
*   **Containerization**: Docker
*   **CI/CD**: GitHub Actions, Google Cloud Build
*   **API Standard**: RESTful
*   **Frontend (Implied)**: ReactJS (as indicated by repository topics and homepage)

## Folder Structure
The project follows a standard Maven and Spring Boot structure:

```
.
├── .github/                       # GitHub Actions workflows
│   └── workflows/
│       └── deploy.yml             # CI/CD deployment workflow
├── .mvn/                          # Maven Wrapper files
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/DPC/DigitalPatientCardBackend/
│   │   │       ├── admin/             # Admin entity
│   │   │       ├── admincontroller/   # Admin REST controller
│   │   │       ├── doctor/            # Doctor & Referral entities
│   │   │       ├── doctorcontroller/  # Doctor REST controller
│   │   │       ├── patient/           # Patient & Disease entities
│   │   │       ├── patientcontroller/ # Patient REST controller
│   │   │       ├── repository/        # Spring Data JPA repositories
│   │   │       ├── user/              # User entity
│   │   │       ├── PDF/               # PDF generation logic
│   │   │       ├── PDFcontroller/     # PDF generation REST controller
│   │   │       ├── CorsConfig.java          # CORS configuration
│   │   │       ├── DigitalPatientCardBackendApplication.java # Spring Boot entry point
│   │   │       └── IndexController.java     # Basic index controller
│   │   └── resources/
│   │       └── application.properties # Application configuration
│   └── test/                      # Unit and integration tests
├── Dockerfile                     # Docker containerization definition
├── Documentation.md               # Detailed project documentation
├── cloudbuild.yaml                # Google Cloud Build configuration
├── mvnw                           # Maven Wrapper script (Linux/macOS)
├── mvnw.cmd                       # Maven Wrapper script (Windows)
└── pom.xml                        # Maven Project Object Model (POM)
```

## Installation

### Prerequisites
Before you begin, ensure you have the following installed:
*   Java Development Kit (JDK) 17 or newer
*   Apache Maven 3.x
*   PostgreSQL database server
*   Docker (optional, for running in a container)

### Steps
1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/AlvinGeorge-AG/DigitalPatientCardBackend.git
    cd DigitalPatientCardBackend
    ```

2.  **Database Setup**:
    *   Create a new PostgreSQL database, for example: `digitalpatientcard_db`.
    *   Ensure your PostgreSQL user has appropriate permissions.

3.  **Configure Application Properties**:
    *   Open `src/main/resources/application.properties`.
    *   Update the database connection details (URL, username, password) to match your PostgreSQL setup. Example:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/digitalpatientcard_db
        spring.datasource.username=your_db_username
        spring.datasource.password=your_db_password
        spring.jpa.hibernate.ddl-auto=update # Or 'create' for first run
        ```

4.  **Build the Project**:
    ```bash
    mvn clean install
    ```
    This command compiles the source code, runs tests, and packages the application into an executable JAR file.

## Usage
The Digital Patient Card Backend provides RESTful API endpoints for managing patient, doctor, and administrative data, as well as generating patient card PDFs.
Once the application is running, you can interact with it using tools like Postman, curl, or through a connected frontend application.

Example API endpoints (inferred):
*   `/api/patients`
*   `/api/doctors`
*   `/api/admins`
*   `/api/pdf/generate/{patientId}`

Refer to `Documentation.md` for a comprehensive list of API endpoints and their usage.

## Environment Variables
The application's `application.properties` file typically references environment variables for sensitive configurations, especially in production environments. For local development, you might set these directly in `application.properties`.

Common environment variables would include:
*   `SPRING_DATASOURCE_URL`: PostgreSQL database connection URL.
*   `SPRING_DATASOURCE_USERNAME`: Username for database access.
*   `SPRING_DATASOURCE_PASSWORD`: Password for database access.

These are typically configured in deployment environments (e.g., GitHub Actions, Google Cloud Build, Docker Compose) rather than hardcoded.

## Running Locally

### Using Maven
After completing the installation steps and building the project:
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080` by default.

### Using Docker
1.  **Build the Docker Image**:
    ```bash
    docker build -t digitalpatientcard-backend .
    ```

2.  **Run the Docker Container**:
    Make sure your PostgreSQL database is accessible from the Docker container (e.g., running on `host.docker.internal` for Docker Desktop or a network alias).
    ```bash
    docker run -p 8080:8080 --name dpc-backend \
      -e SPRING_DATASOURCE_URL="jdbc:postgresql://<your-db-host>:5432/digitalpatientcard_db" \
      -e SPRING_DATASOURCE_USERNAME="your_db_username" \
      -e SPRING_DATASOURCE_PASSWORD="your_db_password" \
      digitalpatientcard-backend
    ```
    Replace `<your-db-host>`, `your_db_username`, and `your_db_password` with your actual database details.

## Build/Deploy

### Docker Image
The `Dockerfile` provides instructions to build a Docker image for the application, enabling containerized deployment.

### GitHub Actions
The repository includes a `.github/workflows/deploy.yml` file, indicating a Continuous Integration/Continuous Deployment (CI/CD) pipeline using GitHub Actions. This workflow automates building, testing, and potentially deploying the application upon code pushes or other triggers.

### Google Cloud Build
The `cloudbuild.yaml` file suggests that the project can be built and deployed using Google Cloud Build, an automation service for building, testing, and deploying applications on Google Cloud Platform. This typically handles Docker image builds and deployments to services like Google App Engine or Kubernetes Engine.

## Contributing
Contributions are welcome! If you'd like to contribute, please follow these steps:
1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes and ensure they adhere to the project's coding standards.
4.  Write appropriate tests for your changes.
5.  Commit your changes with clear, descriptive commit messages.
6.  Push your branch and open a pull request.

## License
No explicit license information found in the repository data.

## Additional Important Notes
*   This repository serves as the backend for the Digital Patient Card system. A separate frontend application (e.g., [Digital Patient Card Frontend](https://dpcfrontend.vercel.app/)) is expected to interact with these services.
*   For detailed internal workings, API documentation, and specific implementation details, please refer to the `Documentation.md` file in this repository.
