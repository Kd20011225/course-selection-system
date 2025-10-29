# Selection (Spring Boot)

A Spring Boot 3.3.x project for managing students, courses, and selection history.

## Quick start

### Prereqs
- Java 17+
- Maven 3.9+
- MySQL (or compatible)
- Git

### Configure environment
Copy `.env.example` to `.env` and update values:

```bash
cp .env.example .env
```

Spring Boot will read environment variables; ensure your shell exports them, e.g.:

```bash
export DB_URL=jdbc:mysql://localhost:3306/myDatabase
export DB_USERNAME=admin
export DB_PASSWORD=your_local_password
```

### Build & run

```bash
./mvnw spring-boot:run
```

or

```bash
mvn clean package
java -jar target/selection-*.jar
```

### Notable fixes in this commit
- **Fixed** `StudentsMapper.updateStudent` SQL to update all columns without truncation.
- **Secured** `application.properties` by parameterizing DB credentials; added `.env.example`.

## API (selected)
- `GET /students` — list students
- `GET /students/{id}` — student by ID
- `PUT /students` — update student
- `PUT /students/{id}/status?status=<int>` — update status
- `PUT /students/{id}/score` — recompute average score

> See `src/main/java/com/example/demo/controller/*` for full endpoints.

