# Socialpedia (Backend)

Backend for Socialpedia

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [API Endpoints](#api-endpoints)
- 
## Installation

Follow these steps to install and set up the project:

1. Clone this repository
2. Navigate to the project directory:
3. Install dependencies and build the project:
    ```sh
    ./mvnw clean install
    ```
4. Set up the database:
    - Set up MySQL database with suitable name
    - Set up [Redis](https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/)
    - Update the `application.properties`file with your database configuration.

## Usage

To run the application, execute:
```sh
./mvnw spring-boot:run
```

## API Endpoints

Open http://localhost/swagger.html to get all the APIs in this project.
