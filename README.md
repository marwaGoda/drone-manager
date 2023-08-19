# Drone Manager Service

The Drone Manager Service is a RESTful API designed to manage drone details and calculate drone boundaries.

## Table of Contents

- [Features](#features)
- [Setup & Installation](#setup--installation)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)


## Features

- **CRUD Operations**: Manage drone details with Create, Read, Update, and Delete operations.
- **Boundary Calculation**: Calculate the boundary that encompasses all drones.

## Setup & Installation

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/marwaGoda/drone-manager.git
    ```

2. **Navigate to the project directory**:
    ```bash
    cd drone-manager
    ```

3. **Install Dependencies**:
    ```bash
    gradle build
    ```

4. **Run the Service**:
    ```bash
    gradle bootRun
    ```

## API Endpoints

- `GET /api/drones`: Retrieve all drones.
- `POST /api/drones`: Create a new drone.
- `PUT /api/drones/{id}`: Update a drone's details.
- `DELETE /api/drones/{id}`: Delete a drone.
- `GET /api/drones/boundary`: Calculate the boundary of all drones.

## Testing

- **Unit Tests**:
    ```bash
    gradle test
    ```
