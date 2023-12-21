# Talent Infusion Backend API

## Overview

The Talent Infusion Backend API is a robust and scalable solution designed to manage talent data. Leveraging Javalin as
the web framework, PostgreSQL as the database, and ActiveJDBC as the Object Relational Mapping (ORM) library, the
project provides a flexible and performant backend for handling talent-related operations.

## Technology Stack

- Javalin: A lightweight web framework for Java that simplifies the process of building RESTful APIs. It is easy to use, flexible, and well-suited for microservices architectures.

- PostgreSQL: A powerful open-source relational database management system. PostgreSQL is chosen for its reliability, extensibility, and support for complex data types.

- ActiveJDBC: An ActiveRecord ORM library for Java. ActiveJDBC simplifies database interactions by providing a simple and intuitive API for working with database records.

- Gradle: The build tool of choice for the project, allowing easy dependency management, build automation, and project configuration.

## Project Structure

The project follows a modular structure to enhance maintainability and scalability:

- src/main/java: Contains the main Java source code for the Javalin application, ActiveJDBC models, and other related components.

- src/main/resources: Houses configuration files, including the Javalin configuration file, database configuration, and other resources.

- src/test: Includes unit and integration tests to ensure the reliability and correctness of the application.

## Prerequisites

Ensure you have the following installed before starting:

- Java (21.0.1)
- Gradle (8.5)
- PostgreSQL (16)

## Getting Started

### Installation

1. Clone the github repository to a directory on your machine

    ```bash
    git clone https://github.com/your-username/your-project.git
    ```

2. Navigate to the project directory:

    ```bash
    cd your-project
    ```

3. Build the project:

    ```bash
    gradle build
    ```

### Configuration

1. Copy the `.env.sample` file to a new file named `.env`:

    ```bash
    cp .env.sample .env
    ```

2. Open the `.env` file in a text editor and fill in the necessary configuration values:

    ```properties
    # .env file

    # Database configuration
    DB_DRIVER=org.postgresql.Driver
    DB_USERNAME=your_username
    DB_PASSWORD=your_password
    DB_URL=jdbc:postgresql://localhost:5432/postgres

    # Other configurations...
    ```

   Replace the placeholder values with your actual configuration details.

## Usage

Run the project with `gradle run`
