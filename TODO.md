# PostgreSQL Configuration Task

## Objectives:
Configure PostgreSQL database for Course Service and Progress Service in production environment

## Steps:
- [x] Examine current course-service configuration files
- [x] Create application-prod.yaml for course-service
- [x] Update application-dev.yaml for course-service (optional)
- [x] Update course-service pom.xml with PostgreSQL dependency
- [x] Mark H2 dependency as test-only in course-service pom.xml
- [x] Create application-prod.yaml for progress-service (if needed)
- [x] Update progress-service pom.xml with PostgreSQL dependency (if needed)
- [x] Verify all changes are correctly applied

## Files to be created/modified:
- course-service/src/main/resources/application-prod.yaml ✅
- course-service/src/main/resources/application-dev.yaml ✅
- course-service/pom.xml ✅
- progress-service/src/main/resources/application-prod.yaml ✅
- progress-service/pom.xml ✅

## Summary of Changes:
- **Course Service**: Configured PostgreSQL for both dev and prod environments
- **Progress Service**: Configured PostgreSQL for production environment
- **Dependencies**: Added PostgreSQL driver (v42.7.3) and marked H2 as test-only
- **Database Configuration**: Both services now use separate PostgreSQL databases (course_db and progress_db)

# PHASE 4 — Communication & Demonstrations

## Objectives:
Implement JMS messaging for async communications and distributed tracing for observability

## Steps:
- [x] Add JMS dependencies (ActiveMQ) to course-service and progress-service
- [x] Configure ActiveMQ in application.yml for course-service and progress-service
- [x] Create message producer in course-service
- [x] Create message listener in progress-service
- [x] Add distributed tracing dependencies (Sleuth, Zipkin/Jaeger) to all services
- [x] Configure Zipkin/Jaeger endpoint in application.yml for all services
- [x] Add missing tracing dependencies to eureka-server and config-server
- [x] Create/update JMS and tracing configuration files
- [x] Add tracing configuration to student-service bootstrap.yml
- [x] Add tracing configuration to gateway-service config files
- [x] Add tracing configuration to eureka-server config files
- [x] Add tracing configuration to config-server config files
- [x] Create JMS configuration classes for both course-service and progress-service
- [x] Create CourseEvent model classes for cross-service communication

## Files to be created/modified:
- course-service/pom.xml: Add JMS and tracing dependencies ✅
- progress-service/pom.xml: Add JMS and tracing dependencies ✅
- student-service/pom.xml: Add tracing dependencies ✅
- gateway-service/pom.xml: Add tracing dependencies ✅
- eureka-server/pom.xml: Add tracing dependencies ✅
- config-server/pom.xml: Add tracing dependencies ✅
- course-service/src/main/resources/application-dev.yml: Add JMS and Zipkin config ✅
- course-service/src/main/resources/application-prod.yaml: Add JMS and Zipkin config ✅
- progress-service/src/main/resources/application-prod.yaml: Add JMS and Zipkin config ✅
- student-service/src/main/resources/bootstrap.yml: Add Zipkin config ✅
- gateway-service config files: Add Zipkin config ✅
- eureka-server config files: Add Zipkin config ✅
- config-server config files: Add Zipkin config ✅
- New classes: 
  - MessageProducer in course-service ✅
  - MessageListener in progress-service ✅
  - CourseEvent model classes (both services) ✅
  - JmsConfig classes (both services) ✅

## Summary of Changes:
- **JMS Demonstration**: Async messaging between course-service (producer) and progress-service (consumer) using ActiveMQ
- **Distributed Tracing**: Sleuth integration with Zipkin for trace visualization across all services
- **Dependencies**: Added spring-boot-starter-activemq, spring-cloud-starter-sleuth, spring-cloud-starter-jaeger
- **Configuration**: ActiveMQ broker URL and Zipkin endpoint configured for all services
- **Message Classes**: Producer sends course events (CREATED, UPDATED, DELETED), consumer processes them for progress tracking
- **Architecture**: Complete microservices communication setup with event-driven architecture and observability

## Implementation Details:

### JMS Messaging Architecture:
- **Topic**: `course-events` (configured in all service config files)
- **Message Producer**: Sends course events when courses are created, updated, or deleted
- **Message Listener**: Processes course events and updates progress tracking accordingly
- **Event Types**: CREATED, UPDATED, DELETED with course metadata

### Distributed Tracing:
- **Tracer**: Spring Cloud Sleuth with Jaeger backend
- **Endpoint**: http://localhost:9411 (configurable per environment)
- **Trace IDs**: Automatically generated and propagated across service boundaries
- **Logging**: Enhanced with trace correlation IDs for debugging

### Configuration Strategy:
- **Development**: Uses localhost URLs for all services
- **Production**: Uses Kubernetes service names for inter-service communication
- **External Dependencies**: PostgreSQL, ActiveMQ, Zipkin/Jaeger containers required

## Testing Instructions:
1. Start infrastructure services (PostgreSQL, ActiveMQ, Zipkin)
2. Start microservices in order: config-server, eureka-server, gateway, course-service, progress-service, student-service
3. Trigger course events through course-service API endpoints
4. Observe JMS messages in ActiveMQ console
5. View distributed traces in Zipkin UI at http://localhost:9411
6. Verify trace correlation IDs in service logs

## Key Benefits:
- **Decoupled Architecture**: Services communicate asynchronously via JMS
- **Observability**: Full distributed tracing across all service boundaries
- **Scalability**: Event-driven patterns allow for horizontal scaling
- **Resilience**: Message queuing provides fault tolerance
- **Debugging**: Trace IDs enable end-to-end request tracking
