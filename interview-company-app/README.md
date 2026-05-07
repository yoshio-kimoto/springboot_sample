# Coding interview - Company App

This is an application that provides a REST API to manage companies and their employees.

## Uses

- Spring Boot Web
- Java 21
- Maven
- H2 database. Tables are cleared when the application is restarted. See `schema.sql` for schema. 
  - Can use H2 console to view data - `http://localhost:8080/h2-console`. JDBC URL: `jdbc:h2:file:./data/companyApp`

## Interview

- Clone repo
- Use any IDE
- Run the integration tests -> `IntegrationTest.java`

1. Set up the project in your IDE. Take time to explore the code, and feel free to run the code and test endpoints manually.
2. Fix the bug for the `testDelete` failing test.
3. Fix the bug for the `testParallelUpdates` failing test. Note: this test may fail intermittently. But there is an underlying issue to be solved in the code.
4. Add a new endpoint that returns a map of company names and the number of employees in each company. Response example:

```json
{
  "Company A": 2,
  "Company B": 1
}
```

5. Write an integration test for the new endpoint.

## Guidelines

- Share your screen on Zoom for a collaborative interview. 
- Feel free to use Internet resources for reference for help with syntax, frameworks, etc.
- Feel free to ask questions at any time for clarification.
- Encouraged to discuss your thought process while looking through the code and working through the problems.


