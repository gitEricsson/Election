# Election Management System

A RESTful API for managing elections, built with Spring and MongoDB.

## Features

- **User Management**: Register users and retrieve user information
- **Election Management**: Create, start, end, and delete elections
- **Voting System**: Cast votes for election options
- **Results**: View election results and determine winners
- **Error Handling**: Comprehensive exception handling for various scenarios

## Technologies Used

- **Java 17+**
- **Spring Boot 4.1.0-M3**
- **MongoDB** (via Spring Data MongoDB)
- **Lombok** (for reducing boilerplate code)
- **JUnit 5** and **Mockito** (for testing)

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB (running locally or accessible via connection string)

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

3. **Run the application**:

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
│   ├── controllers/     # REST controllers
│   ├── data/
│   │   ├── models/      # Domain models
│   │   └── repositories/# Data access layer
│   ├── dtos/            # Data transfer objects
│   ├── exceptions/      # Custom exceptions
│   ├── services/        # Business logic
│   └── utils/           # Utility classes
└── test/java/services/  # Unit tests
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
