# Solid lending platform
## Table of contents
1. [Introduction](#Introduction)
2. [Setup and run](#Setup)
3. [Project details](#Project)
4. [Tests structure and naming convention](#Tests)

## Introduction <a name="Introduction"></a>
Backend application for Fintech challenge 2 - social lending platform. 
Application provides core function of lending platform. It allows platform users to create new auctions,
manage auctions and check its status, add offers to active auctions, end auctions and start a loan. Platform is wired
to provided HLTechBank API, that makes all platform bank transactions. 

Application is available here: (username: example, password: example)
* SIT - https://solid-lendig-platform.kale-team.sit.fintechchallenge.pl/
* UAT - https://solid-lendig-platform.kale-team.uat.fintechchallenge.pl/
* PROD - https://solid-lendig-platform.kale-team.fintechchallenge.pl/


## Setup <a name="Setup"></a>
Project can be managed with maven `mvn` command. Application based on Spring Framework and
uses spring profiles to manage properties for different environments. 

Project deployed on Fintech infrastructure uses default spring profile. 
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

### Concepts
The main idea was to create a "social-lending" platform, which would connect two types of users: lender and borrower.
It allows granting loans to natural persons without the participation of a bank.
Borrower can create new auction on a Platform and edit existing ones. Borrower also manages created loans,
repays the loan installments and confirms creation of the predefined loan. 
Lender can add offers to active auctions, manage offers and check all investments status. 

Offers submitted by lenders are automatically selected in the process of creating a loan. 
First, offers with the most favorable rating (the lowest one) are being selected. If the offer's sum is higher 
than the final loan, the amount of the last offer is being divided. The loan rate is calculated based on the 
average rate of all selected offers. The rate of the offers does not change, only the amount of the offer may 
be reduced when the loan is created.

Payments on the platform simulates HL Tech Banking API. Each user has two bank accounts - private one and platform one.
From the level of the application he can transfer
amounts from one account to another. The platform account is used for settlements between users. 
When a user is repaying his loan installment, the amount is automatically divided between lenders and transferred to their account.

### Project structure overview:
Project main package consists of following subpackages:
 * `domain` - it contains all business logic;
 * `infrastructure` - components used by application: database connection, REST communication with bank API
 * `interfaces` - REST endpoints for communication with application

main package tree:
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
### Principles of components naming and design
 * Each domain package contains `ApplicationService`, with package specific prefix (e.g. `AuctionApplicationService`). It is a 'domain entrance'. 
 This class takes calls from application and runs domain services or methods. It also maps application request, containing external values, to domain specific 
 objects. This service also maps given response with domain object to application common values. It is not
 a DDD application service - it is rather a service 'used by application'.
 * Domain packages contains `DomainServices` (e.g. `AuctionDomainService`), with a prefix connected to model Object. These classes contain main business
 logic. `DomainServices` manage processes specific to its domain and operates on domain objects. It is DDD application service. 
 * Domain specific objects (e.g. `Loan`, `Offer`) - represents model. Each domain class has associated `repository` class.
       
### Domain details
#### Common
This package contains objects and services common for all domains. 
* `Values` - classes representing model specific object, like `Money`.
* `Users` - user class implementations.
* `Events` - object used for communication between ApplicationServices.
#### Auction
Package responsible for managing platform auctions and auction offers.
`Auction`, `Offer` - represents model objects. `BestOffersRatePolicy` - class used to select best offer from ending auction. Application of policy pattern.
#### Loan
Manage `Loan` model linked with `Investments`. Each object has own `RepaymentSchedule`. 
`NewLoanParams` and `NewInvestmentParam` are used to map values from EndAuctionEvent for loan creation process. 
#### Payment
`PaymentApplicationService` uses `BankClient` to manage bank transaction, needed by the domain.
### Infrastructure details
Provides communication with databases and external services.
#### Database
Contains all domain `repositories` implementation. Uses `JPARepository` interface for database queries. `Entity` classes
have JPA annotation and needed mappers.
#### REST 
This package manages connection to HLTechBank API with `HltechBankApiFeignClient`. 
`HltechBankClient` implements `BankClient` interface from `Payment` domain package, to provide needed bank transactions 
service.
### Interface details
Application provides REST interface for communication. `interfaces.rest` package contains all controllers and DTOs 
with mapping methods.
To check interface details, see swagger with application endpoints: https://solid-lendig-platform.kale-team.sit.fintechchallenge.pl/swagger-ui/
## Tests structure and naming convention  <a name="Tests"></a>
Test name suffixes depends on a test category:
* `*UT` - unit tests
* `*IT` - integration tests
* `*FT` - functional tests
* `ApiT` - API test, for controllers

Test packages also contains `*Helper` classes used for test objects generation. `domain.InMemory` package contains 
in memory repositories implementation. It can be used to run basic integration tests.  

Tests created in Groovy with Spock framework. WireMock simulates bank API in infrastructure tests. Functional tests communication with
the database is based on test containers - configured in `PostgresContainerTestSpecification`.