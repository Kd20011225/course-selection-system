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

## Pushing to GitHub

1. **Initialize the repo (if not already):**
   ```bash
   cd selection
   git init
   git branch -M main
   ```

2. **.gitignore**  
   The project includes a `.gitignore`. Ensure you do **NOT** commit real secrets.

3. **Commit your changes:**
   ```bash
   git add .
   git commit -m "Fix StudentsMapper SQL; parameterize DB creds; add README and .env.example"
   ```

4. **Create a new GitHub repo** (via UI), then add it as remote:
   ```bash
   git remote add origin https://github.com/<your-username>/<your-repo>.git
   ```

5. **Push:**
   ```bash
   git push -u origin main
   ```

### If you already have a remote and branch
```bash
git checkout -b fix/mapper-and-config
git commit -am "Fix StudentsMapper SQL; parameterize DB creds"
git push -u origin fix/mapper-and-config
```

Open a PR on GitHub.

---

**Note:** If MyBatis mapping breaks due to schema differences, adjust column names in `StudentsMapper.java` to match your DB schema.
