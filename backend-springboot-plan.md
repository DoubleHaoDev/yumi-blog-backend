# Spring Boot Backend Implementation Plan
## GraphQL to SQL Migration - REST API Backend

---

## Project Overview

**Goal**: Create a Spring Boot REST API with PostgreSQL database to replace GraphCMS backend for the Next.js blog frontend.

**Tech Stack**:
- Java 21
- Spring Boot 4.0.5
- Spring Data JPA
- PostgreSQL 16 (Docker)
- REST API (JSON responses)
- Gradle

---

## Database Schema

### Tables & Relationships

#### 1. **authors**
```sql
- id (UUID, PK)
- name (VARCHAR)
- bio (TEXT)
- photo_url (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

#### 2. **categories**
```sql
- id (UUID, PK)
- name (VARCHAR)
- slug (VARCHAR, UNIQUE)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

#### 3. **posts**
```sql
- id (UUID, PK)
- title (VARCHAR)
- slug (VARCHAR, UNIQUE)
- excerpt (TEXT)
- content (TEXT) -- stores HTML string
- featured_image_url (VARCHAR) -- URL returned by /api/images/{imageId}
- featured_post (BOOLEAN)
- author_id (UUID, FK -> authors.id)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

#### 4. **post_categories** (Join Table)
```sql
- post_id (UUID, FK -> posts.id)
- category_id (UUID, FK -> categories.id)
- PRIMARY KEY (post_id, category_id)
```

#### 5. **comments**
```sql
- id (UUID, PK)
- post_id (UUID, FK -> posts.id)
- name (VARCHAR)
- email (VARCHAR)
- comment (TEXT)
- created_at (TIMESTAMP)
```

---

## Required REST API Endpoints

### Posts Endpoints

| Method | Endpoint | Description | Query Params |
|--------|----------|-------------|--------------|
| GET | `/api/posts` | Get all posts with authors, categories, images | `featured=true` (optional) |
| GET | `/api/posts/{slug}` | Get single post by slug with full details | - |
| GET | `/api/posts/{slug}/similar` | Get 3 similar posts by categories | - |
| GET | `/api/posts/{slug}/adjacent` | Get next/previous posts by date | - |
| GET | `/api/posts/recent` | Get 3 most recent posts | - |

### Categories Endpoints

| Method | Endpoint | Description | Query Params |
|--------|----------|-------------|--------------|
| GET | `/api/categories` | Get all categories | - |
| GET | `/api/categories/{slug}/posts` | Get all posts in category | - |

### Comments Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/posts/{slug}/comments` | Get all comments for a post | - |
| POST | `/api/comments` | Create new comment | `{ name, email, comment, slug }` |

---

## Workplan

### Phase 1: Project Setup ✅
- [x] Create Spring Boot project (Spring Initializr)
  - Dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, Validation
- [x] Set up PostgreSQL 16 database locally via Docker
- [x] Configure `application.properties`
  - Database connection (host: localhost:5432, db: yumi_blog_developments, user: yumi)
  - JPA/Hibernate settings
  - CORS configuration (allow Next.js frontend origin)
- [x] Create basic project structure (entities, repositories, services, controllers)

### Phase 2: Database & Entities ✅
- [x] Create JPA Entity classes:
  - `Author.java`
  - `Category.java`
  - `Post.java`
  - `Comment.java`
- [x] Define relationships (@ManyToOne, @ManyToMany, etc.)
- [x] Create database initialization script (`src/main/resources/sql/init.sql`)
- [x] Create seed data in `init.sql` (1 author, 3 categories, 3 posts, 2 comments)

### Phase 3: Repositories
- [ ] Create JPA Repository interfaces:
  - `AuthorRepository`
  - `CategoryRepository`
  - `PostRepository`
  - `CommentRepository`
- [ ] Add custom query methods:
  - `findBySlug(String slug)`
  - `findByFeaturedPostTrue()`
  - `findTop3ByOrderByCreatedAtDesc()`
  - `findByCategoriesSlug(String slug)`
  - `findByCreatedAtGreaterThanEqualAndSlugNotOrderByCreatedAtAsc()`
  - `findByCreatedAtLessThanEqualAndSlugNotOrderByCreatedAtDesc()`

### Phase 4: DTOs (Data Transfer Objects)
- [ ] Create response DTOs to match frontend expectations:
  - `PostListDTO` (for list views)
  - `PostDetailDTO` (for single post with full content)
  - `CategoryDTO`
  - `CommentDTO`
  - `AuthorDTO`
- [ ] Create request DTOs:
  - `CreateCommentRequest`

### Phase 5: Services
- [ ] Create service layer:
  - `PostService.java`
  - `CategoryService.java`
  - `CommentService.java`
- [ ] Implement business logic for each endpoint
- [ ] Add DTO mapping logic (Entity → DTO)

### Phase 6: REST Controllers
- [ ] Create controllers:
  - `PostController.java`
  - `CategoryController.java`
  - `CommentController.java`
- [ ] Implement all REST endpoints listed above
- [ ] Add proper error handling (@ControllerAdvice)
- [ ] Add request validation (@Valid)

### Phase 7: CORS & Security
- [ ] Configure CORS to allow Next.js frontend (http://localhost:3000)
- [ ] Add security headers
- [ ] (Optional) Add rate limiting for comment submissions

### Phase 8: Testing & Documentation
- [ ] Test all endpoints with Postman/cURL
- [ ] Add sample data to database
- [ ] Create API documentation (README with endpoint details)
- [ ] (Optional) Add Swagger/OpenAPI documentation

### Phase 9: Deployment Ready
- [ ] Environment-based configuration (dev, prod)
- [ ] Docker setup (optional)
- [ ] Deployment instructions (Heroku, AWS, Railway, etc.)

---

## Data Response Format Examples

### GET /api/posts Response
```json
[
  {
    "id": "uuid",
    "title": "Post Title",
    "slug": "post-slug",
    "excerpt": "Short description...",
    "featuredImage": "https://image-url.com/image.jpg",
    "createdAt": "2024-01-15T10:30:00Z",
    "author": {
      "id": "uuid",
      "name": "John Doe",
      "bio": "Author bio...",
      "photo": "https://photo-url.com/photo.jpg"
    },
    "categories": [
      { "name": "Technology", "slug": "technology" },
      { "name": "Web Dev", "slug": "web-dev" }
    ]
  }
]
```

### GET /api/posts/{slug} Response
```json
{
  "id": "uuid",
  "title": "Post Title",
  "slug": "post-slug",
  "excerpt": "Short description...",
  "content": "<p>Full HTML content...</p>",
  "featuredImage": "https://image-url.com/image.jpg",
  "createdAt": "2024-01-15T10:30:00Z",
  "author": {
    "id": "uuid",
    "name": "John Doe",
    "bio": "Author bio...",
    "photo": "https://photo-url.com/photo.jpg"
  },
  "categories": [
    { "name": "Technology", "slug": "technology" }
  ]
}
```

### POST /api/comments Request
```json
{
  "name": "Jane Smith",
  "email": "jane@example.com",
  "comment": "Great article!",
  "slug": "post-slug"
}
```

---

## Important Notes

### Content Storage ✅ Decided
- Store as **HTML string** in PostgreSQL TEXT column
- Backend returns HTML directly, frontend renders with `dangerouslySetInnerHTML`

### Image Hosting ✅ Decided
- Images stored as **BYTEA** in PostgreSQL
- Backend exposes `GET /api/images/{imageId}` endpoint
- API responses return full image URL (e.g. `http://localhost:8080/api/images/abc123`)
- Frontend uses URL directly in `<img src={post.featuredImage} />`

### Slug Generation
- Ensure slugs are unique and URL-friendly
- Auto-generate from title if not provided
- Example: "My Blog Post" → "my-blog-post"

### CORS Configuration
```java
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

---

## Environment Variables (application.properties)

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/yumi_blog_developments
spring.datasource.username=yumi
spring.datasource.password=yumiblog

# JPA/Hibernate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server
server.port=8080
```

---

## Recommended Project Structure

```
src/main/java/com/yourname/blogapi/
├── BlogApiApplication.java
├── config/
│   └── WebConfig.java (CORS)
├── controller/
│   ├── PostController.java
│   ├── CategoryController.java
│   └── CommentController.java
├── dto/
│   ├── request/
│   │   └── CreateCommentRequest.java
│   └── response/
│       ├── PostListDTO.java
│       ├── PostDetailDTO.java
│       ├── CategoryDTO.java
│       ├── CommentDTO.java
│       └── AuthorDTO.java
├── entity/
│   ├── Author.java
│   ├── Category.java
│   ├── Post.java
│   └── Comment.java
├── exception/
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── AuthorRepository.java
│   ├── CategoryRepository.java
│   ├── PostRepository.java
│   └── CommentRepository.java
└── service/
    ├── PostService.java
    ├── CategoryService.java
    └── CommentService.java
```

---

## Next Steps After Backend is Ready

1. Run the Spring Boot application
2. Verify PostgreSQL connection
3. Test all endpoints with Postman
4. Populate database with sample data
5. Provide API base URL to frontend team (e.g., `http://localhost:8080`)

---

## Decisions Made ✅

| Question | Decision |
|---|---|
| Build tool | **Gradle** |
| Java version | **Java 21** |
| PostgreSQL setup | **Docker (PostgreSQL 16)** |
| Image hosting | **SQL DB (BYTEA) + `/api/images/{id}` URL endpoint** |
| Content format | **HTML string** |
