# Microservices Project

This project is a comprehensive microservices-based application built using Spring Boot. It includes various services and infrastructure components, designed to provide a scalable and robust platform for developing and deploying microservices.

## Technology Stack

- **Spring Boot**: The core framework used for building all microservices
- **Spring Cloud**: For microservices patterns like service discovery and API gateway
- **Docker**: For containerization and easy deployment
- Various databases and messaging systems (MongoDB, PostgreSQL, Elasticsearch, Kafka)

## Services

The project includes the following Spring Boot services:

1. **Auth**: Authentication service
2. **App**: Main application service
3. **SearchService**: Elasticsearch-based search functionality
4. **MailService**: Email handling service
5. **NamingServer**: Service discovery (Eureka)
6. **API Gateway**: Entry point for client requests

## Infrastructure Components

- **MongoDB**: NoSQL database
- **MongoDB Express**: Web-based MongoDB admin interface
- **PostgreSQL**: Relational database
- **Elasticsearch**: Search and analytics engine
- **Zookeeper**: Distributed coordination service
- **Kafka**: Message broker
- **OpenLDAP**: Lightweight Directory Access Protocol server

## Prerequisites

- Docker
- Docker Compose

## Getting Started

1. Clone this repository
2. Navigate to the project directory
3. Run the following command to start all services:

   ```
   docker-compose up -d
   ```

4. To stop all services:

   ```
   docker-compose down
   ```

## Service Details

### MentoAuth (Port: 8081)
Spring Boot authentication service integrated with OpenLDAP.

### MentoApp (Port: 8080)
Main Spring Boot application service connected to PostgreSQL and Elasticsearch.

### SearchService (Port: 8084)
Spring Boot service that provides search functionality using Elasticsearch.

### MailService (Port: 8083)
Spring Boot service that handles email operations and uses MongoDB for storage.

### NamingServer (Port: 8761)
Spring Cloud Eureka server for service discovery.

### API Gateway (Port: 8765)
Spring Cloud Gateway serving as the entry point for client requests, routing to appropriate services.

## Infrastructure Access

- MongoDB Express: http://localhost:9091
- Elasticsearch: http://localhost:9200
- Kafka: localhost:9092
- OpenLDAP: localhost:1389 (LDAP), localhost:1636 (LDAPS)

## Configuration

Environment variables and service configurations are defined in the `docker-compose.yml` file. Modify this file to change service configurations, database credentials, or other settings. Spring Boot application properties can be adjusted in their respective `application.properties` or `application.yml` files within each service.

## Networking

All services are connected to a custom Docker network named `spring-cloud-network`.

## Data Persistence

Docker volumes are used for data persistence:
- `pgdata`: PostgreSQL data
- `openldap_data`: OpenLDAP data
- `elastic_data`: Elasticsearch data
- `kafka_data`: Kafka data

## Resource Limits

Some services have memory limits set. Adjust these in the `docker-compose.yml` file if needed.

## Contributing

Please read CONTRIBUTING.md for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the [LICENSE NAME] - see the LICENSE.md file for details.
