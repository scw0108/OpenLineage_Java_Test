# OpenLineage Java Test
## Overview
This project is a Java application that uses Spring Boot and MySQL. It is containerized using Docker and Docker Compose. The project integrates with the Marquez project to send OpenLineage data to the Marquez website. Additionally, it retains the original official demo to provide a complete example of Data Lineage.
## Prerequisites
- Docker
- Docker Compose
- Java 11 or higher
## Setup
###  Clone the repository
```bash
$ git https://github.com/scw0108/OpenLineage_Java_Test.git && cd OpenLineage_Java_Test
```
### Environment Variables
Create a `.env` file in the root directory and add the following environment variables:
```bash
DB_USER
ROOT_PASSWORD
DATABASE_NAME
DB_PASSSWORD
````

### Build the Spring Boot Image
1. Create a JAR file
```bash
$ cd OpenLineage_App && ./gradlew build
```
2. Build the Docker image
```bash
$ docker build -t openlineage_java_app:latest .
```
### Start the Application
#### Without official metadata
```bash
$ cd .. $$ ./docker/up.sh
```
#### With official metadata
```bash
$ cd .. $$ ./docker/up.sh --seed
```


