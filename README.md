# OpenLineage Java Test
## Overview
This project is a Java application that uses Spring Boot, MySQL and [OpenLineage](https://openlineage.io/). It is containerized using Docker and Docker Compose. The project integrates with the [Marquez project](https://github.com/MarquezProject/marquez) to send OpenLineage data to the Marquez website. Additionally, it retains the original official demo to provide a complete example of Data Lineage.
## Prerequisites
- Docker
- Docker Compose
- Java 11 or higher
## Setup
###  Clone the repository
```bash
$ git clone https://github.com/scw0108/OpenLineage_Java_Test.git && cd OpenLineage_Java_Test
```
### Environment Variables
Create a `.env` file in the root directory and add the following environment variables:
```.env
DB_USER=xxx
ROOT_PASSWORD=xxx
DATABASE_NAME=openlineage
DB_PASSSWORD=xxx
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
### Access the Application
```angular2html
http://localhost:8080
```
### Access the Marquez Website
```angular2html
http://localhost:3000
```
## Application Introduction
The application is a simple Spring Boot application that uses MySQL to store data and create OpenLineage data.
### Function
1. Create a new owner
2. Query a onwer
3. Create onwer's detail information
4. Query onwer's detail information
### Database
The database has two tables: `owner` and `detaila`.
1. Owners Table:  
- id: INT(4) UNSIGNED, NOT NULL, AUTO_INCREMENT, PRIMARY KEY
- first_name: VARCHAR(255)
- last_name: VARCHAR(255)
- address: VARCHAR(255)
- city: VARCHAR(255)
- telephone: VARCHAR(255)

2. Details Table:
- id: INT(4) UNSIGNED, NOT NULL, PRIMARY KEY, FOREIGN KEY REFERENCES owners(id)
- first_name: VARCHAR(255)
- last_name: VARCHAR(255)
- interest: VARCHAR(255)
### Process
When SQL is executed, OpenLineage data is created and sent to the Marquez website.
#### OpenLineage Data
##### Create Owner
- Job Name: `create_owner`
- Job Namespace: `openlineage_test`
- Output Name: `openlineage_test`
- Output Namespace: `openlineage.owners`
#### Query Owner
- Job Name: `query_owner`
- Job Namespace: `openlineage_test`
- Input Name: `openlineage_test`
- Input Namespace: `openlineage.owners`
#### Create Detail
- Job Name: `create_detail`
- Job Namespace: `openlineage_test`
- Output Name: `openlineage_test`
- Output Namespace: `openlineage.details`
#### Query Detail
- Job Name: `query_detail`
- Job Namespace: `openlineage_test`
- Input Name: `openlineage_test`
- Input Namespace: `openlineage.details`
#### Demonstrate Data Lineage
<img width="1321" alt="Marquez web" src="https://github.com/user-attachments/assets/265d3055-35f7-4dbc-8796-36482eb8e6a5">


