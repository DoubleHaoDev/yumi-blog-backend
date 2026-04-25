# Next.js Frontend Migration Plan
## GraphQL to REST API Migration

---

## Project Overview

**Goal**: Replace GraphQL queries with REST API calls to the new Spring Boot backend.

**Current Stack**:
- Next.js 11.1.2
- React 17.0.2
- graphql-request (to be removed)
- TailwindCSS
- Moment.js

**What Changes**:
- Remove GraphQL dependencies
- Replace GraphQL queries with REST API calls (fetch/axios)
- Update data transformation logic
- Update API routes

---

## Files to Modify

### 1. Dependencies (package.json)
**Remove**:
- `graphql`
- `graphql-request`

**Add**:
- `axios` (recommended for cleaner API calls) OR use native `fetch`

### 2. Services Layer (`/services/index.js`)
Replace all GraphQL queries with REST API calls:
- `getPosts()` → `GET /api/posts`
- `getCategories()` → `GET /api/categories`
- `getPostDetails(slug)` → `GET /api/posts/{slug}`
- `getSimilarPosts(categories, slug)` → `GET /api/posts/{slug}/similar`
- `getAdjacentPosts(createdAt, slug)` → `GET /api/posts/{slug}/adjacent`
- `getCategoryPost(slug)` → `GET /api/categories/{slug}/posts`
- `getFeaturedPosts()` → `GET /api/posts?featured=true`
- `getRecentPosts()` → `GET /api/posts/recent`
- `getComments(slug)` → `GET /api/posts/{slug}/comments`
- `submitComment(obj)` → `POST /api/comments` (already uses fetch, just update URL)

### 3. API Routes (`/pages/api/comments.js`)
- Remove GraphQL mutation
- Update to proxy to Spring Boot backend OR remove entirely (call backend directly)

### 4. Environment Variables
**Remove**:
- `NEXT_PUBLIC_GRAPHCMS_ENDPOINT`
- `GRAPHCMS_TOKEN`

**Add**:
- `NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api`

### 5. Data Structure Updates
**GraphQL Response Structure**:
```javascript
// GraphQL returns:
{
  postsConnection: {
    edges: [
      {
        cursor: "...",
        node: { /* post data */ }
      }
    ]
  }
}
```

**REST API Response Structure**:
```javascript
// REST returns:
[
  { /* post data */ }
]
```

**Components that need data structure updates**:
- Any component consuming `edges.node` pattern
- Pages using `getStaticProps` and `getStaticPaths`

---

## Workplan

### Phase 1: Setup & Dependencies
- [ ] Add `NEXT_PUBLIC_API_BASE_URL` to `.env.local`
- [ ] Install axios: `npm install axios`
- [ ] Remove GraphQL packages: `npm uninstall graphql graphql-request`
- [ ] Create API utility file (`/lib/api.js` or `/utils/api.js`)

### Phase 2: Create API Utility Layer
- [ ] Create `/lib/api.js` with base configuration:
  - Base URL from env var
  - Error handling
  - Response interceptors
- [ ] Create reusable API client (axios instance)

### Phase 3: Update Services Layer (`/services/index.js`)
- [ ] Replace `getPosts()` with REST call
- [ ] Replace `getCategories()` with REST call
- [ ] Replace `getPostDetails(slug)` with REST call
- [ ] Replace `getSimilarPosts()` with REST call
- [ ] Replace `getAdjacentPosts()` with REST call
- [ ] Replace `getCategoryPost(slug)` with REST call
- [ ] Replace `getFeaturedPosts()` with REST call
- [ ] Replace `getRecentPosts()` with REST call
- [ ] Replace `getComments(slug)` with REST call
- [ ] Update `submitComment()` to call backend directly

### Phase 4: Update Data Transformations
- [ ] Remove GraphQL `edges.node` unwrapping in `getPosts()`
- [ ] Remove GraphQL `edges.node` unwrapping in `getCategoryPost()`
- [ ] Update data shape expectations in components
- [ ] Ensure date formats are compatible (ISO 8601)

### Phase 5: Update API Routes ✅ Decided — Option A
- [x] Remove `/pages/api/comments.js` — call Spring Boot backend directly
- [ ] Update `submitComment()` in services to call `POST /api/comments` directly

### Phase 6: Update Content Rendering ✅ Decided — HTML
- Backend returns content as **HTML string**
- [ ] Remove any Rich Text JSON rendering logic
- [ ] Render content using `dangerouslySetInnerHTML={{ __html: post.content }}`
- [ ] Test rich text rendering (images, headings, links, etc.)

### Phase 7: Update Pages
- [ ] Test `/pages/index.js` (homepage)
- [ ] Test `/pages/post/[slug].js` (post detail page)
- [ ] Test `/pages/category/[slug].js` (category page)
- [ ] Verify `getStaticProps` data fetching
- [ ] Verify `getStaticPaths` path generation

### Phase 8: Update Components
- [ ] Test PostCard component
- [ ] Test PostDetail component
- [ ] Test Categories component
- [ ] Test Comments component
- [ ] Test CommentsForm component
- [ ] Test FeaturedPosts component
- [ ] Test PostWidget component (recent/similar posts)
- [ ] Test AdjacentPostCard component

### Phase 9: Testing
- [ ] Run development server: `npm run dev`
- [ ] Test homepage loads posts
- [ ] Test category filtering works
- [ ] Test post detail page renders correctly
- [ ] Test comments display and submission
- [ ] Test featured posts carousel
- [ ] Test similar/recent posts widgets
- [ ] Test next/previous post navigation
- [ ] Check console for errors
- [ ] Test build: `npm run build`

### Phase 10: Cleanup
- [ ] Remove unused GraphQL code
- [ ] Remove `NEXT_PUBLIC_GRAPHCMS_ENDPOINT` from env files
- [ ] Remove `GRAPHCMS_TOKEN` from env files
- [ ] Update `.env.example` if it exists
- [ ] Update README with new setup instructions

---

## Code Examples

### New API Utility (`/lib/api.js`)

```javascript
import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Error handling interceptor
apiClient.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

export default apiClient;
```

### Updated Service Functions (`/services/index.js`)

**BEFORE (GraphQL)**:
```javascript
import { request, gql } from 'graphql-request';

const graphqlAPI = process.env.NEXT_PUBLIC_GRAPHCMS_ENDPOINT;

export const getPosts = async () => {
  const query = gql`
    query MyQuery {
      postsConnection {
        edges {
          node {
            author { name, bio, photo { url } }
            title
            slug
            excerpt
            // ... more fields
          }
        }
      }
    }
  `;
  const result = await request(graphqlAPI, query);
  return result.postsConnection.edges; // Returns edges array
};
```

**AFTER (REST)**:
```javascript
import apiClient from '../lib/api';

export const getPosts = async () => {
  const posts = await apiClient.get('/posts');
  return posts; // Returns posts array directly
};

export const getPostDetails = async (slug) => {
  const post = await apiClient.get(`/posts/${slug}`);
  return post;
};

export const getCategories = async () => {
  const categories = await apiClient.get('/categories');
  return categories;
};

export const getCategoryPost = async (slug) => {
  const posts = await apiClient.get(`/categories/${slug}/posts`);
  return posts;
};

export const getFeaturedPosts = async () => {
  const posts = await apiClient.get('/posts?featured=true');
  return posts;
};

export const getRecentPosts = async () => {
  const posts = await apiClient.get('/posts/recent');
  return posts;
};

export const getSimilarPosts = async (categories, slug) => {
  const posts = await apiClient.get(`/posts/${slug}/similar`);
  return posts;
};

export const getAdjacentPosts = async (createdAt, slug) => {
  const result = await apiClient.get(`/posts/${slug}/adjacent`);
  return result; // { next: {...}, previous: {...} }
};

export const getComments = async (slug) => {
  const comments = await apiClient.get(`/posts/${slug}/comments`);
  return comments;
};

export const submitComment = async (obj) => {
  const result = await apiClient.post('/comments', obj);
  return result;
};
```

### Alternative: Using Native Fetch

```javascript
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080/api';

export const getPosts = async () => {
  const response = await fetch(`${API_BASE_URL}/posts`);
  if (!response.ok) throw new Error('Failed to fetch posts');
  return response.json();
};
```

---

## Data Structure Changes

### Posts List

**GraphQL Structure (BEFORE)**:
```javascript
// In components:
posts.map((post) => {
  const { node } = post;
  return (
    <PostCard
      title={node.title}
      slug={node.slug}
      author={node.author}
      featuredImage={node.featuredImage.url}
      categories={node.categories}
    />
  );
});
```

**REST Structure (AFTER)**:
```javascript
// In components:
posts.map((post) => {
  return (
    <PostCard
      title={post.title}
      slug={post.slug}
      author={post.author}
      featuredImage={post.featuredImage} // Already a URL string
      categories={post.categories}
    />
  );
});
```

### Post Content

**GraphQL (BEFORE)**:
```javascript
// content.raw is Rich Text JSON
const contentHTML = getRichTextContent(post.content.raw);
```

**REST (AFTER)**:
```javascript
// Backend returns HTML directly
const contentHTML = post.content; // Already HTML string
```

---

## Component Updates Example

### PostCard Component

**BEFORE**:
```javascript
// Expects post.node structure
const PostCard = ({ post }) => (
  <div>
    <img src={post.node.featuredImage.url} />
    <h3>{post.node.title}</h3>
    <p>{post.node.excerpt}</p>
  </div>
)
```

**AFTER**:
```javascript
// Direct post structure
const PostCard = ({ post }) => (
  <div>
    <img src={post.featuredImage} />
    <h3>{post.title}</h3>
    <p>{post.excerpt}</p>
  </div>
)
```

### Homepage (`/pages/index.js`)

**BEFORE**:
```javascript
export async function getStaticProps() {
  const posts = (await getPosts()) || [];
  return {
    props: { posts }
  };
}

// In component:
{posts.map((post) => (
  <PostCard key={post.node.slug} post={post.node} />
))}
```

**AFTER**:
```javascript
export async function getStaticProps() {
  const posts = (await getPosts()) || [];
  return {
    props: { posts }
  };
}

// In component:
{posts.map((post) => (
  <PostCard key={post.slug} post={post} />
))}
```

---

## Environment Variables

### `.env.local` (Development)
```bash
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
```

### `.env.production` (Production)
```bash
NEXT_PUBLIC_API_BASE_URL=https://your-backend-domain.com/api
```

---

## Testing Checklist

### Before Backend is Ready
- [ ] Create mock API responses
- [ ] Test with mock data to verify data flow
- [ ] Ensure components render correctly with new structure

### After Backend is Ready
- [ ] Start Spring Boot backend (port 8080)
- [ ] Start Next.js frontend (port 3000)
- [ ] Verify CORS is configured correctly
- [ ] Test all pages and features
- [ ] Check network tab for API calls
- [ ] Verify error handling (404, 500, etc.)

---

## Common Issues & Solutions

### Issue 1: CORS Errors
**Solution**: Ensure Spring Boot allows origin `http://localhost:3000`

### Issue 2: Data Structure Mismatches
**Solution**: Console.log API responses and verify structure matches component expectations

### Issue 3: Image URLs
**Solution**: Ensure backend returns full URLs, not relative paths

### Issue 4: Content Rendering
**Solution**: Verify content format (HTML/Markdown/JSON) matches what frontend expects

### Issue 5: Date Formatting
**Solution**: Ensure backend returns ISO 8601 dates, use moment.js for display

---

## Migration Strategy ✅ Decided — Big Bang

Complete the full backend first, then migrate the entire frontend at once.

---

## Post-Migration Optimizations

- [ ] Add API response caching (SWR already included)
- [ ] Add loading states for API calls
- [ ] Add error boundaries for failed API calls
- [ ] Optimize image loading (next/image)
- [ ] Add request retries for failed calls
- [ ] Consider API route proxying for security

---

## Decisions Made ✅

| Question | Decision |
|---|---|
| `/pages/api/comments.js` proxy | **Removed — call backend directly (Option A)** |
| Content format from backend | **HTML string** |
| Backend URL in production | TBD |
| Authentication for comments | **Future feature (post-launch)** |
