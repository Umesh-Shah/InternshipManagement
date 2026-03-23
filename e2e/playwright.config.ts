import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
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
    // Auth setup projects — run first to create storageState files
    { name: 'admin setup', testMatch: /admin\.setup\.ts/, teardown: undefined },
    { name: 'student setup', testMatch: /student\.setup\.ts/, teardown: undefined },

    // Admin tests — use admin storageState
    {
      name: 'admin',
      testDir: './tests/admin',
      use: {
        ...devices['Desktop Chrome'],
        storageState: '.auth/admin.json',
      },
      dependencies: ['admin setup'],
    },

    // Student tests — use student storageState
    {
      name: 'student',
      testDir: './tests/student',
      use: {
        ...devices['Desktop Chrome'],
        storageState: '.auth/student.json',
      },
      dependencies: ['student setup'],
    },

    // Auth tests — no storageState (tests login/logout directly)
    {
      name: 'auth',
      testMatch: /auth\.spec\.ts/,
      use: { ...devices['Desktop Chrome'] },
    },
  ],
});
