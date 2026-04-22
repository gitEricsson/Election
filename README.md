# Election Management System

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0--M3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0+-green.svg)](https://www.mongodb.com/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-orange.svg)](https://maven.apache.org/)

A comprehensive RESTful API for managing elections, built with Spring Boot and MongoDB. This system enables organizations to create, manage, and conduct elections with user registration, voting, and result tracking capabilities.

## Features

- **User Management**: Register users and retrieve user information with email validation
- **Election Management**: Create, start, end, and delete elections with date validation
- **Voting System**: Secure vote casting with duplicate vote prevention
- **Results & Analytics**: View election results and determine winners with tie handling
- **Error Handling**: Comprehensive exception handling for various scenarios (invalid dates, unauthorized access, etc.)
- **RESTful API**: Well-structured REST endpoints with proper HTTP status codes
- **Data Persistence**: MongoDB integration for scalable data storage

## Technologies Used

- **Java 17+**
- **Spring Boot 4.1.0-M3**
  - Spring Web (REST controllers)
  - Spring Data MongoDB (data persistence)
- **MongoDB** (NoSQL database)
- **Lombok** (boilerplate code reduction)
- **JUnit 5** and **Mockito** (unit testing)
- **Maven** (build tool)

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB 7.0+ (running locally or accessible via connection string)

## Installation and Setup

1. **Clone the repository**:

   ```bash
   git clone <repository-url>
   cd Election
   ```

2. **Build the project**:

   ```bash
   mvn clean compile
   ```

3. **Ensure MongoDB is running**:

   ```bash
   # For local MongoDB installation
   mongod
   ```

   Or configure connection to a remote MongoDB instance.

4. **Run the application**:

   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080` by default.

## API Endpoints

### Users

- `POST /api/users` - Register a new user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email

### Elections

- `POST /api/elections` - Create a new election
- `PATCH /api/elections/{id}/start?userId={userId}` - Start an election
- `PATCH /api/elections/{id}/end?userId={userId}` - End an election
- `DELETE /api/elections/{id}?userId={userId}` - Delete an election
- `GET /api/elections` - Get all elections

### Votes

- `POST /api/votes` - Cast a vote

### Results

- `GET /api/results/{electionId}` - Get election results
- `GET /api/results/{electionId}/winner` - Get election winner

## Testing

Run the tests using Maven:

```bash
mvn test
```

## Project Structure

```
src/
├── main/java/election/
│   ├── controllers/          # REST controllers (@RestController)
│   ├── data/
│   │   ├── models/           # Domain entities (@Document)
│   │   └── repositories/     # Data access (@Repository)
│   ├── dtos/
│   │   ├── requests/         # Request DTOs
│   │   └── responses/        # Response DTOs
│   ├── exceptions/           # Custom exceptions
│   ├── services/             # Business logic (@Service)
│   └── utils/                # Utility classes
├── test/java/services/       # Unit tests
└── main/resources/           # Configuration files (optional)
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Run tests and ensure they pass
6. Submit a pull request

## License

This project is licensed under the MIT License.
