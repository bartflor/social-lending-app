# Solid lending platform

## Table of contents
1. [Introduction](#Introduction)
2. [Setup and run](#Setup)
3. [Project details](#Project)
4. [Tests structure and naming convention](#Tests)

## Introduction <a name="Introduction"></a>
Backend application for Fintech challenge 2 - SOLID_ers team  

Application is available here: (username: example, password: example)
* SIT - https://solid-lendig-platform.kale-team.sit.fintechchallenge.pl/
* UAT - https://solid-lendig-platform.kale-team.uat.fintechchallenge.pl/
* PROD - https://solid-lendig-platform.kale-team.fintechchallenge.pl/


## Setup <a name="Setup"></a>
It is a maven project, so it can be managed with mvn command. Application based on Spring Framework and
uses spring profiles to manage project behavior on different environments. 

Project deployed on Fintech infrastructure, uses default spring profile. 
During development, I am using a `local` spring profile. It needs local PostgreSQL Server with `lendingplatform` database,
available at standard port.

### To run on a local machine in a container:
You will need: docker, docker-compose, maven. 
Docker compose will create needed PostgreSQL database in a container.
 * run tests and compile project: `mvn clean install`
 * use docker-compose with: `docker-compose up --build`

Application exposed to localhost:8080. To check application endpoints you can use: http://localhost:8080/swagger-ui/
### To run application on a local machine with a local database: 
You will need: maven, local PostgreSQL Server with `lendingplatform` database, available at standard port.
 * compile with maven
 * run using `local` spring profile

## Project details <a name="Project"></a>

### Project structure overview:
```bash
├── src 
│   ├── main
│   │   ├── java
│   │   │   └── pl
│   │   │       └── fintech
│   │   │           └── solidlending
│   │   │               └── solidlendigplatform
│   │   │                   ├── appconfig
│   │   │                   ├── Application.java 
│   │   │                   ├── domain
│   │   │                   │   ├── auction
│   │   │                   │   ├── common
│   │   │                   │   │   ├── events
│   │   │                   │   │   ├── user
│   │   │                   │   │   └── values
│   │   │                   │   ├── loan
│   │   │                   │   └── payment
│   │   │                   ├── infrastructure
│   │   │                   │   ├── database
│   │   │                   │   │   ├── auction
│   │   │                   │   │   ├── loan
│   │   │                   │   │   └── user
│   │   │                   │   └── rest
│   │   │                   └── interfaces
│   │   │                       └── rest
│   │   └── resources
│   └── test
```
### Naming convention
### Domain details
### Infrastructure details
### Interface details

## Tests structure and naming convention  <a name="Tests"></a>