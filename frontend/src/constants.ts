export const ROLE_ADMIN   = 'ROLE_ADMIN'   as const;
export const ROLE_STUDENT = 'ROLE_STUDENT' as const;

export const AUTH_STORAGE_KEY = 'ims-auth';

export const SESSION_DURATION_MS = 8 * 60 * 60 * 1000; // mirrors server-side token lifetime

export const ROUTES = {
  LOGIN:   '/login',
  ADMIN:   '/admin',
  STUDENT: '/student',
} as const;

export const API_PATHS = {
  BASE:        '/api',
  AUTH_LOGIN:  '/auth/login',
  AUTH_LOGOUT: '/auth/logout',
} as const;
