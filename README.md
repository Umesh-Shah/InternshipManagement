# Internship Management System

A modern web application for managing internships, built with React (frontend) and Spring (backend).

## Project Structure

```
internship-management/
├── frontend/                 # React frontend application
│   ├── src/
│   │   ├── api/             # API integration layer
│   │   ├── components/      # Reusable UI components
│   │   ├── hooks/           # Custom React hooks
│   │   ├── pages/           # Page components
│   │   └── routes/          # Application routing
│   └── package.json         # Frontend dependencies
└── src/                     # Java backend application
    ├── main/
    │   └── java/
    │       └── ca/
    │           └── uwindsor/
    │               └── ims/  # Backend source code
    └── test/                # Test files
```

## Technologies Used

### Frontend
- React 18 with TypeScript
- React Query for state management
- React Router for navigation
- React Toastify for notifications
- Tailwind CSS for styling
- Jest and React Testing Library for testing

### Backend
- Java 21
- Spring Framework 6.1.3
- Hibernate ORM 6.4.2
- Jakarta Persistence
- Log4j2 for logging
- JUnit 5 for testing

## Getting Started

### Prerequisites
- Node.js 18+ and npm
- Java Development Kit (JDK) 21
- Maven 3.8+

### Backend Setup

1. Clone the repository:
   ```bash
   git clone [repository-url]
   cd internship-management
   ```

2. Build the backend:
   ```bash
   mvn clean install
   ```

3. Run the backend server:
   ```bash
   mvn spring-boot:run
   ```

The backend server will start on `http://localhost:8080`.

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Create a `.env` file:
   ```
   REACT_APP_API_URL=http://localhost:8080/api
   ```

4. Start the development server:
   ```bash
   npm start
   ```

The frontend application will be available at `http://localhost:3000`.

## Features

### Authentication
- User login with username/password
- Password change functionality
- Session management
- Protected routes

### Dashboard
- User profile information
- Navigation menu
- Logout functionality

## API Documentation

### Authentication Endpoints

#### Login
- **URL**: `/api/auth/login`
- **Method**: POST
- **Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**:
  ```json
  {
    "success": boolean,
    "message": "string",
    "data": {
      "loginId": "string",
      "loginName": "string",
      "firstName": "string",
      "lastName": "string",
      "emailId": "string",
      "phoneNo": "string"
    }
  }
  ```

#### Change Password
- **URL**: `/api/auth/change-password`
- **Method**: POST
- **Body**:
  ```json
  {
    "loginId": "string",
    "oldPassword": "string",
    "newPassword": "string"
  }
  ```
- **Response**:
  ```json
  {
    "success": boolean,
    "message": "string"
  }
  ```

#### Check Login
- **URL**: `/api/auth/check-login`
- **Method**: POST
- **Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**:
  ```json
  {
    "success": boolean,
    "message": "string",
    "data": boolean
  }
  ```

## Testing

### Running Backend Tests
```bash
mvn test
```

### Running Frontend Tests
```bash
cd frontend
npm test
```

## Development Guidelines

### Frontend
- Use functional components with TypeScript
- Follow React Query patterns for data fetching
- Implement proper error handling
- Use Tailwind CSS for styling
- Write unit tests for components and hooks

### Backend
- Follow Spring best practices
- Use proper exception handling
- Implement logging with Log4j2
- Write unit tests for services and controllers
- Use Jakarta Validation for request validation

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
