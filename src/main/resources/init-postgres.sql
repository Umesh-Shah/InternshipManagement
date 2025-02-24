-- Create database if it doesn't exist
CREATE DATABASE internship_management WITH OWNER = postgres ENCODING = 'UTF8' CONNECTION LIMIT = -1;

-- Connect to the database
\c internship_management;

-- Create extension for UUID generation if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create schema if needed
-- CREATE SCHEMA IF NOT EXISTS public;

-- Grant privileges
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public; 