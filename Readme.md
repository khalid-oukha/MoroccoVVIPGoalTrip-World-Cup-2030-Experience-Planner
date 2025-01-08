# MoroccoVVIPGoalTrip Backend

World Cup 2030 travel planning platform backend - Built with Spring Boot.

## Overview

Backend system for MoroccoVVIPGoalTrip platform that helps football fans plan their travel and activities during the 2030 World Cup in Morocco. The system provides personalized activity recommendations, budget management, and real-time trip planning features.

## Tech Stack

- Java 17.
- Spring Boot 3.2.0
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL 15
- Maven
- Docker
- Swagger/OpenAPI
- WebSocket (for real-time updates)
- Redis (caching)
- Apache Kafka (event streaming)
- ELK Stack (logging)
- Prometheus & Grafana (monitoring)

## Architecture

### Key Components

```
com.vvipgoaltrip
├── config/                 # Configuration classes
├── controller/            # REST API endpoints
├── model/
│   ├── entity/           # Database entities
│   ├── dto/              # Data transfer objects
│   └── enums/            # Enumerations
├── repository/           # Data access layer
├── service/              # Business logic
├── security/             # Security configurations
├── exception/            # Custom exceptions
└── util/                 # Utility classes
```

### Database Schema

```sql
-- Core Entities
User (id, email, password, role, preferences)
City (id, name, description, coordinates)
Activity (id, name, description, type, cost, city_id)
Trip (id, user_id, start_date, end_date, budget)
TripActivity (id, trip_id, activity_id, date, status)
Notification (id, user_id, message, type, read_status)

-- Supporting Entities
UserPreference (id, user_id, activity_type, budget_range)
ActivityCategory (id, name, description)
Budget (id, trip_id, allocated, spent, remaining)
```

## Features

1. User Management
    - Registration/Authentication
    - Profile management
    - Preference settings
    - JWT-based security

2. Trip Planning
    - City selection
    - Activity recommendations
    - Budget tracking
    - Schedule management

3. Activity Management
    - Custom recommendations
    - Real-time availability
    - Budget optimization
    - Category filtering

4. Notifications
    - Email notifications
    - Push notifications
    - WebSocket real-time updates
    - Trip reminders

## API Endpoints

### Authentication
```
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh-token
```

### Users
```
GET /api/users/{id}
PUT /api/users/{id}
GET /api/users/{id}/preferences
PUT /api/users/{id}/preferences
```

### Trips
```
POST /api/trips
GET /api/trips/{id}
PUT /api/trips/{id}
GET /api/trips/{id}/activities
POST /api/trips/{id}/activities
```

### Activities
```
GET /api/activities
GET /api/activities/{id}
GET /api/activities/recommendations
GET /api/activities/search
```

### Cities
```
GET /api/cities
GET /api/cities/{id}
GET /api/cities/{id}/activities
```

## Setup Instructions

1. Prerequisites
```bash
# Required installations
java 17
maven 3.8+
docker
postgresql 15
redis
```

2. Clone Repository
```bash
git clone https://github.com/yourusername/morocco-vvip-goal-trip-backend.git
cd morocco-vvip-goal-trip-backend
```

3. Environment Configuration
```bash
# Create .env file
cp .env.example .env

# Configure database connection
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DB=vvipgoaltrip
POSTGRES_USER=your_username
POSTGRES_PASSWORD=your_password
```

4. Database Setup
```bash
# Create database
psql -U postgres
CREATE DATABASE vvipgoaltrip;

# Run migrations
mvn flyway:migrate
```

5. Build & Run
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

## Docker Deployment

```bash
# Build docker image
docker build -t vvipgoaltrip-backend .

# Run container
docker-compose up -d
```

## Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn jacoco:report
```

## Monitoring & Logging

- Prometheus metrics: `/actuator/prometheus`
- Health check: `/actuator/health`
- Grafana dashboards available at port 3000
- ELK Stack for centralized logging
- Application logs in `/logs` directory

## Security Measures

1. Authentication
    - JWT-based authentication
    - Token refresh mechanism
    - Role-based access control

2. Data Protection
    - HTTPS/TLS encryption
    - Password hashing with BCrypt
    - Input validation
    - XSS protection
    - CORS configuration

3. Rate Limiting
    - API rate limiting
    - Brute force protection
    - DDoS protection

## Performance Optimization

1. Caching
    - Redis for application caching
    - Query result caching
    - Static resource caching

2. Database
    - Connection pooling
    - Query optimization
    - Indexed searches
    - Database partitioning

3. API
    - Response compression
    - Pagination
    - Async processing
    - WebSocket for real-time updates

## Contributing

1. Fork repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push branch (`git push origin feature/amazing-feature`)
5. Open pull request

## Development Guidelines

1. Code Style
    - Follow Google Java Style Guide
    - Use meaningful variable/method names
    - Add JavaDoc comments for public methods
    - Maximum method length: 30 lines

2. Testing
    - Write unit tests for all services
    - Maintain 80% code coverage
    - Mock external dependencies
    - Test edge cases

3. Git Workflow
    - Feature branch workflow
    - Meaningful commit messages
    - Regular pulls from main
    - Code review before merge

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.

## Support

For support, please email support@vvipgoaltrip.com or join our Slack channel.


I've created a comprehensive README.md covering all aspects of the backend project, including setup instructions, architecture details, security measures, and development guidelines. The format is ready to be copied directly into a README.md file. Would you like me to explain or elaborate on any specific section?