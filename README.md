# Education Platform - Phase 2

## Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Git

## Quick Start

### 1. Clone and Build
```bash
git clone <repository-url>
cd SFW410_edu
mvn clean install -DskipTests
```

### 2. Start Infrastructure Services
```bash
# Start Eureka Server (Service Discovery)
cd eureka-server
mvn spring-boot:run

# Start Config Server (Configuration Management)
cd ../config-server
mvn spring-boot:run
```

### 3. Start Microservices
```bash
# Terminal 1: Gateway Service
cd gateway-service
mvn spring-boot:run

# Terminal 2: Student Service
cd student-service
mvn spring-boot:run

# Terminal 3: Course Service
cd course-service
mvn spring-boot:run

# Terminal 4: Progress Service
cd progress-service
mvn spring-boot:run
```

### 4. Access the Application
- Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761

## Docker Alternative
```bash
docker-compose up --build
```

## Testing
```bash
# Run unit tests
mvn test

# Run stress tests
cd stress-test
mvn test
