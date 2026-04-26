-- ============================================
-- Schema: Create Tables
-- ============================================

CREATE TABLE IF NOT EXISTS images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content_type VARCHAR(100) NOT NULL,
    data BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS authors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    bio TEXT,
    photo_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS posts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    excerpt TEXT,
    content TEXT,
    featured_image_url VARCHAR(255),
    featured_post BOOLEAN DEFAULT FALSE,
    author_id UUID NOT NULL REFERENCES authors(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS post_categories (
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, category_id)
);

CREATE TABLE IF NOT EXISTS comments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    comment TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================
-- Seed Data
-- ============================================

-- Authors
INSERT INTO authors (id, name, bio, photo_url) VALUES
    ('a1000000-0000-0000-0000-000000000001', 'Yumi Sensei', 'A passionate blogger who loves writing about technology and web development.', null);

-- Categories
INSERT INTO categories (id, name, slug) VALUES
    ('ca000000-0000-0000-0000-000000000001', 'Technology', 'technology'),
    ('ca000000-0000-0000-0000-000000000002', 'Web Development', 'web-development'),
    ('ca000000-0000-0000-0000-000000000003', 'Lifestyle', 'lifestyle');

-- Posts
INSERT INTO posts (id, title, slug, excerpt, content, featured_post, author_id) VALUES
    (
        'b0000000-0000-0000-0000-000000000001',
        'Getting Started with Spring Boot',
        'getting-started-with-spring-boot',
        'A beginner''s guide to building REST APIs with Spring Boot and PostgreSQL.',
        '<h2>Introduction</h2><p>Spring Boot makes it easy to create stand-alone, production-grade Spring applications.</p><h2>Prerequisites</h2><p>You will need Java 21 and Docker installed on your machine.</p>',
        TRUE,
        'a1000000-0000-0000-0000-000000000001'
    ),
    (
        'b0000000-0000-0000-0000-000000000002',
        'Why I Love Next.js',
        'why-i-love-nextjs',
        'Next.js is a powerful React framework that makes building web apps a breeze.',
        '<h2>What is Next.js?</h2><p>Next.js is a React framework that provides server-side rendering and static site generation out of the box.</p>',
        FALSE,
        'a1000000-0000-0000-0000-000000000001'
    ),
    (
        'b0000000-0000-0000-0000-000000000003',
        'My Daily Routine as a Developer',
        'my-daily-routine-as-a-developer',
        'A peek into how I structure my day to stay productive and healthy.',
        '<h2>Morning Routine</h2><p>I start every day with a cup of coffee and a 30-minute review of my tasks for the day.</p>',
        FALSE,
        'a1000000-0000-0000-0000-000000000001'
    );

-- Post Categories
INSERT INTO post_categories (post_id, category_id) VALUES
    ('b0000000-0000-0000-0000-000000000001', 'ca000000-0000-0000-0000-000000000001'),
    ('b0000000-0000-0000-0000-000000000001', 'ca000000-0000-0000-0000-000000000002'),
    ('b0000000-0000-0000-0000-000000000002', 'ca000000-0000-0000-0000-000000000002'),
    ('b0000000-0000-0000-0000-000000000003', 'ca000000-0000-0000-0000-000000000003');

-- Comments
INSERT INTO comments (post_id, name, email, comment) VALUES
    ('b0000000-0000-0000-0000-000000000001', 'Alice', 'alice@example.com', 'Great post! Really helped me get started.'),
    ('b0000000-0000-0000-0000-000000000001', 'Bob', 'bob@example.com', 'Very clear explanation, thanks!');

