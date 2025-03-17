# ⚡Validation Using Java Fluent Validator
This project uses java-fluent-validator to validate API request payloads dynamically, ensuring that data meets business constraints before it is processed or stored in the database.

## 🚀 Overview
This project is a Spring Boot REST API designed to manage **Netflix Shows**. It utilizes `Spring Boot 3.4.2`, `Spring Data JPA with Hibernate`, and `PostgreSQL` to perform CRUD operations on Netflix Shows.
One of the key aspects of this project is the use of `Java Fluent Validator` to enforce input validation rules **dynamically**. Instead of relying on standard `Java Bean Validation` (annotations like `@NotNull`), this approach allows greater `flexibility`, `readability`, and `maintainability` in defining complex validation rules programmatically. By using Fluent Validator, we can ensure that incoming request bodies adhere to the **expected structure and business rules** before persisting data into the database.

## 📌 Why Fluent Validator ?
### Programmatic & Flexible
Unlike annotations (`@NotNull`, `@Size`, etc.), validation rules can be dynamically modified at runtime. It means that validation rules are not fixed at compile-time but **can be adjusted based on conditions** at runtime. This allows the application to apply different validation rules dynamically depending on user input, API parameters, business logic, or external configurations.

Example scenario:
- If type = "MOVIE", then durationInMinute must be greater than 0.
- If type = "TV_SHOW", then durationInMinute should not be validated but seasons must be greater than 0.

```java
public class NetflixShowValidator extends AbstractValidator<NetflixShowDto> {
    @Override
    public void rules() {
        // Validate 'type' field (must be either MOVIE or TV_SHOW)
        ruleFor(NetflixShowDto::getType)
            .must(type -> type.equals("MOVIE") || type.equals("TV_SHOW"))
            .withMessage("Type must be either MOVIE or TV_SHOW");

        // Apply different validation rules based on the type
        ruleFor(NetflixShowDto::getDurationInMinute)
            .must(duration -> duration > 0)
            .when(dto -> dto.getType().equals("MOVIE"))  // Only for MOVIE
            .withMessage("Duration must be greater than 0 for movies");

        ruleFor(NetflixShowDto::getSeasons)
            .must(seasons -> seasons > 0)
            .when(dto -> dto.getType().equals("TV_SHOW"))  // Only for TV_SHOW
            .withMessage("Seasons must be greater than 0 for TV shows");
    }
}
```


### Better Readability
Validation logic is structured in a clear and fluent manner. The validation reads naturally like a sentence. Reads like plain English, unlike traditional `if-else` or annotation-based (`@NotNull`, `@Size`, etc.) validation.

❌ Without Fluent Validator
```java
if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
    throw new ValidationException("Title cannot be empty");
}
if (dto.getReleaseYear() < 1900 || dto.getReleaseYear() > LocalDate.now().getYear()) {
    throw new ValidationException("Invalid release year");
}
```

✅ With Fluent Validator
```java
ruleFor(NetflixShowDto::getTitle)
    .notNull().notEmpty().withMessage("Title cannot be empty");

ruleFor(NetflixShowDto::getReleaseYear)
    .must(year -> year >= 1900 && year <= LocalDate.now().getYear())
    .withMessage("Invalid release year");

```

### Maintainability
Centralizes validation logic, making updates easy. If you need to change a rule, you update it in one place instead of modifying multiple files.

### Reusable Rules
Common validation logic can be reused across multiple request DTOs.

### Extensible
Can add custom rules without modifying the existing validation structure.

### Improved Error Handling
Custom validation messages and logic can be tailored to specific business needs.

---


## ✨Tech Stack
The technology used in this project are:
- `Spring Boot 3.4.2` : Framework for building RESTful APIs
- `Spring Data JPA with Hibernate` : Simplifying database interactions
- `Spring Boot Starter Web` : Building RESTful APIs or web applications
- `PostgreSQL` : Database for persisting Netflix Shows
- `Lombok` : Reducing boilerplate code
- `Java Fluent Validator` : For advanced request validation
---

## 📋 Project Structure
The project follows a layered architecture with the following structure:
```bash
api-with-fluent-validator/
│── src/main/java/com/yoanesber/spring/rest/api_with_fluent_validator/
│   ├── config/                # Contains configurations for the application
│   ├── controller/            # Exposes REST API endpoints for handling requests and responses
│   ├── dto/                   # Data Transfer Objects (DTOs) for request/response payloads
│   ├── entity/                # Entity classes representing database tables
│   ├── repository/            # JPA repositories for database access
│   ├── service/               # Business logic layer
│   │   ├── impl/              # Implementation of services
│   ├── validator/             # Contains custom validation logic using Fluent Validator to enforce constraints on API request payloads
``` 
---

## 📂 Environment Configuration
Configuration values are stored in `.env.development` and referenced in `application.properties`.

Example `.env.development` file content:
```properties
# application
APP_PORT=8081
SPRING_PROFILES_ACTIVE=development
 
# postgres
SPRING_DATASOURCE_PORT=5432
SPRING_DATASOURCE_USERNAME=myusername
SPRING_DATASOURCE_PASSWORD=mypassword
SPRING_DATASOURCE_DB=netflix_development
```

Example `application.properties` file content:
```properties
spring.application.name=api-with-fluent-validator
server.port=${APP_PORT}
spring.profiles.active=${SPRING_PROFILES_ACTIVE}

## datasource
spring.datasource.url=jdbc:postgresql://localhost:${SPRING_DATASOURCE_PORT}/${SPRING_DATASOURCE_DB}?currentSchema=netflix
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```
---

## 💾 Database Schema (DDL – PostgreSQL)
The project uses PostgreSQL as its database, with a structured schema to store Netflix show data efficiently. Below is the DDL (Data Definition Language) used to create the database schema.

```sql
CREATE SCHEMA netflix;

CREATE SEQUENCE netflix.id_netflix_shows_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

CREATE TABLE netflix.netflix_shows (
	id int8 NOT NULL DEFAULT nextval('netflix.id_netflix_shows_seq'::regclass),
	"type" varchar(7) NOT NULL,
	title text NOT NULL,
	director text NULL,
	cast_members text NULL,
	country varchar(60) NOT NULL,
	date_added date NOT NULL,
	release_year int4 NOT NULL,
	rating int4 NULL,
	duration_in_minute int4 NULL,
	listed_in text NULL,
	description text NULL,
	CONSTRAINT netflix_shows_pkey PRIMARY KEY (id),
	CONSTRAINT netflix_shows_type_check CHECK (((type)::text = ANY (ARRAY[('MOVIE'::character varying)::text, ('TV_SHOW'::character varying)::text])))
);
```
---

## 🛠 Installation & Setup
A step by step series of examples that tell you how to get a development env running.
1. Clone the repository
```bash
git clone https://github.com/yoanesber/Spring-Boot-Validation-Using-Java-Fluent-Validator.git
```

2. Set up PostgreSQL
- Run DDL PostgreSQL to create Database Schema
- Configure the PostgreSQL database connection in application.properties

3. Run the application locally
- Make sure PostgreSQL is running, then execute: 
```bash
mvn spring-boot:run
```
- The API will be available at http://localhost:8081/ 
---

## 🔗 API Endpoints
The REST API provides a set of endpoints to manage Netflix shows, allowing clients to perform CRUD operations (Create, Read, Update, Delete). Each endpoint follows RESTful principles and accepts/returns JSON data. Below is a list of available endpoints along with sample requests.

- `GET` http://localhost:8081/api/v1/netflix-shows - Retrieve all Netflix Shows with response:
```json
{
    "statusCode": 200,
    "timestamp": "2025-02-27T21:31:04.7743558",
    "message": "NetflixShows retrieved successfully",
    "data": [
        {
            "id": 1,
            "showType": "MOVIE",
            "title": "Sankofa",
            "director": "Haile Gerima",
            "castMembers": "Kofi Ghanaba, Oyafunmike Ogunlano, Alexandra Duah, Nick Medley, Mutabaruka, Afemo Omilami, Reggie Carter, Mzuri, Oliver",
            "country": "United States",
            "dateAdded": "2021-09-24",
            "releaseYear": 2024,
            "rating": 10,
            "durationInMinute": 90,
            "listedIn": "Comedies",
            "description": "A woman adjusting to life after a loss contends with a feisty bird that's taken over her garden — and a husband who's struggling to find a way forward."
        },
        {
            "id": 2,
            "showType": "TV_SHOW",
            "title": "The Smart Money Woman",
            "director": "Bunmi Ajakaiye",
            "castMembers": "Osas Ighodaro, Ini Dima-Okojie, Kemi Lala Akindoju, Toni Tones, Ebenezer Eno, Eso Okolocha DIke, Patrick Diabuah, Karibi Fubara, Temisan Emmanuel, Timini Egbuson",
            "country": "India",
            "dateAdded": "2021-09-16",
            "releaseYear": 2020,
            "rating": 5,
            "durationInMinute": 90,
            "listedIn": "International TV Shows, Romantic TV Shows, TV Comedies",
            "description": "Five glamorous millennials strive for success as they juggle careers, finances, love and friendships. Based on Arese Ugwu's 2016 best-selling novel."
        }
    ]
}
```
- `GET` http://localhost:8081/api/v1/netflix-shows/1 - Retrieve a specific Netflix Show by ID with response:
```json
{
    "statusCode": 200,
    "timestamp": "2025-02-27T21:31:56.0479465",
    "message": "NetflixShows retrieved successfully",
    "data": {
        "id": 1,
        "showType": "MOVIE",
        "title": "Sankofa",
        "director": "Haile Gerima",
        "castMembers": "Kofi Ghanaba, Oyafunmike Ogunlano, Alexandra Duah, Nick Medley, Mutabaruka, Afemo Omilami, Reggie Carter, Mzuri, Oliver",
        "country": "United States",
        "dateAdded": "2021-09-24",
        "releaseYear": 2024,
        "rating": 10,
        "durationInMinute": 90,
        "listedIn": "Comedies",
        "description": "A woman adjusting to life after a loss contends with a feisty bird that's taken over her garden — and a husband who's struggling to find a way forward."
    }
}
```
- `POST` http://localhost:8081/api/v1/netflix-shows - Create a new Netflix Show with body request:
```json
{
    "showType":"TV_SHOW",
    "title":"The Smart Money Woman",
    "director":"Bunmi Ajakaiye",
    "castMembers":"Osas Ighodaro, Ini Dima-Okojie, Kemi Lala Akindoju, Toni Tones, Ebenezer Eno, Eso Okolocha DIke, Patrick Diabuah, Karibi Fubara, Temisan Emmanuel, Timini Egbuson",
    "country":"India",
    "dateAdded":"2021-09-16",
    "releaseYear":2020,
    "rating":5,
    "durationInMinute":90,
    "listedIn":"International TV Shows, Romantic TV Shows, TV Comedies",
    "description":"Five glamorous millennials strive for success as they juggle careers, finances, love and friendships. Based on Arese Ugwu's 2016 best-selling novel."
}
```

Response:
```json
{
    "statusCode": 201,
    "timestamp": "2025-02-27T21:32:43.3093492",
    "message": "NetflixShows created successfully",
    "data": {
        "id": 2,
        "showType": "TV_SHOW",
        "title": "The Smart Money Woman",
        "director": "Bunmi Ajakaiye",
        "castMembers": "Osas Ighodaro, Ini Dima-Okojie, Kemi Lala Akindoju, Toni Tones, Ebenezer Eno, Eso Okolocha DIke, Patrick Diabuah, Karibi Fubara, Temisan Emmanuel, Timini Egbuson",
        "country": "India",
        "dateAdded": "2021-09-16",
        "releaseYear": 2020,
        "rating": 5,
        "durationInMinute": 90,
        "listedIn": "International TV Shows, Romantic TV Shows, TV Comedies",
        "description": "Five glamorous millennials strive for success as they juggle careers, finances, love and friendships. Based on Arese Ugwu's 2016 best-selling novel."
    }
}
```

When sending an **invalid JSON body**, the API will return a `400 Bad Request` response with validation error details.
Request:
```json
{
    "showType":"TV Show",
    "title":"King of Boys: The Return of the King ¶",
    "director":"Kemi Adetiba",
    "castMembers":"Sola Sobowale, Toni Tones, Richard Mofe-Damijo, Efa Iwara, Titi Kuti, Tobechukwu \"iLLbliss\" Ejiofor, Remilekun \"Reminisce\" Safaru, Charles  \"Charly Boy\" Oputa, Nse Ikpe-Etim, Keppy Ekpenyong Bassey, Bimbo Manuel, Akin Lewis, Lord Frank, Osas Ighodaro, Taiwo Ajai-Lycett, Paul Sambo",
    "country":"United States, The United Kingdom of Great Britain and Northern Ireland ¶",
    "dateAdded":"2021-08-27",
    "releaseYear":2021,
    "rating":70,
    "durationInMinute":90,
    "listedIn":"Crime TV Shows, International TV Shows, TV Dramas",
    "description":"Alhaja Eniola Salami starts anew and sets her sights on a different position of power, fueled by revenge, regret and ruthlessness."
}
```

For the request body above, the response obtained is as follows:
```json
{
    "statusCode": 400,
    "timestamp": "2025-02-27T21:46:23.6795329",
    "message": "Validation failed. Please check your input.",
    "data": {
        "Country": [
            "Country must contain only printable ASCII characters",
            "Country must be less than or equal to 60 character length"
        ],
        "Rating": [
            "Rating must be between 1 and 10"
        ],
        "ShowType": [
            "ShowType must be either MOVIE or TV_SHOW"
        ],
        "Title": [
            "Title must contain only printable ASCII characters"
        ]
    }
}
```
This response clearly indicates which fields failed validation and provides meaningful error messages for better debugging and user experience

- `PUT` http://localhost:8081/api/v1/netflix-shows/1 - Update an existing Netflix Show with body request:
```json
{
    "showType": "MOVIE",
    "title": "Sankofa",
    "director": "Haile Gerima",
    "castMembers": "Kofi Ghanaba, Oyafunmike Ogunlano, Alexandra Duah, Nick Medley, Mutabaruka, Afemo Omilami, Reggie Carter, Mzuri, Oliver",
    "country": "United States",
    "dateAdded": "2021-09-24",
    "releaseYear": 2024,
    "rating": 10,
    "durationInMinute": 90,
    "listedIn": "Drama",
    "description": "A woman adjusting to life after a loss contends with a feisty bird that's taken over her garden — and a husband who's struggling to find a way forward."
}
```

and response:
```json
{
    "statusCode": 200,
    "timestamp": "2025-02-27T21:34:43.8536126",
    "message": "NetflixShows updated successfully",
    "data": {
        "id": 2,
        "showType": "MOVIE",
        "title": "Sankofa",
        "director": "Haile Gerima",
        "castMembers": "Kofi Ghanaba, Oyafunmike Ogunlano, Alexandra Duah, Nick Medley, Mutabaruka, Afemo Omilami, Reggie Carter, Mzuri, Oliver",
        "country": "United States",
        "dateAdded": "2021-09-24",
        "releaseYear": 2024,
        "rating": 10,
        "durationInMinute": 90,
        "listedIn": "Drama",
        "description": "A woman adjusting to life after a loss contends with a feisty bird that's taken over her garden — and a husband who's struggling to find a way forward."
    }
}
```

- `DELETE` http://localhost:8081/api/v1/netflix-shows/1 - Delete a Netflix Show with response:
```json
{
    "statusCode": 200,
    "timestamp": "2025-02-27T21:35:17.0110773",
    "message": "NetflixShows deleted successfully",
    "data": null
}
```
---

This project follows best practices in Spring Boot development, ensuring efficiency and maintainability.