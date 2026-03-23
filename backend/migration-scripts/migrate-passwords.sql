-- =============================================================================
-- Phase 2 Password Migration
-- =============================================================================
-- Spring Security's DelegatingPasswordEncoder requires passwords to be prefixed
-- with their encoding scheme, e.g. {noop}mypassword or {bcrypt}$2a$...
--
-- This script prefixes all existing plain-text passwords with {noop}, which
-- lets the new BCrypt-aware auth stack accept them immediately without any
-- manual re-hashing. Rows that already have an encoding prefix are skipped.
--
-- Run once against the `test` database (dev profile uses port 3307):
--   mysql -u root -p -P 3307 test < migrate-passwords.sql
--
-- To upgrade a specific user to BCrypt later:
--   UPDATE login SET PWD = '{bcrypt}<bcrypt-hash>' WHERE USERNAME = 'someuser';
-- =============================================================================

-- login table (student and admin accounts)
UPDATE login
SET PWD = CONCAT('{noop}', PWD)
WHERE PWD NOT LIKE '{%}%';

-- vbct_login table (admin/staff accounts)
UPDATE vbct_login
SET LOGIN_PASSWORD = CONCAT('{noop}', LOGIN_PASSWORD)
WHERE LOGIN_PASSWORD NOT LIKE '{%}%';
