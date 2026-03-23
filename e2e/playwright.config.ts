import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 1 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: process.env.CI ? [['github'], ['html']] : 'html',

  use: {
    baseURL: 'http://localhost:5173',
    trace: 'on-first-retry',
  },

  projects: [
    // Auth setup projects — each gets its own testDir so files in ./auth/ are discovered
    {
      name: 'admin setup',
      testDir: './auth',
      testMatch: /admin\.setup\.ts/,
    },
    {
      name: 'student setup',
      testDir: './auth',
      testMatch: /student\.setup\.ts/,
    },

    // Admin tests — depend on admin setup having written .auth/admin.json
    {
      name: 'admin',
      testDir: './tests/admin',
      use: {
        ...devices['Desktop Chrome'],
        storageState: '.auth/admin.json',
      },
      dependencies: ['admin setup'],
    },

    // Student tests — depend on student setup having written .auth/student.json
    {
      name: 'student',
      testDir: './tests/student',
      use: {
        ...devices['Desktop Chrome'],
        storageState: '.auth/student.json',
      },
      dependencies: ['student setup'],
    },

    // Auth tests — no storageState (tests login/logout flows directly)
    {
      name: 'auth',
      testDir: './tests',
      testMatch: /auth\.spec\.ts/,
      use: { ...devices['Desktop Chrome'] },
    },
  ],
});
