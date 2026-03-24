import { test, expect } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';

test.describe('Authentication', { mode: 'serial' }, () => {
  test('admin login redirects to /admin', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login('admin', 'admin123');
    await page.waitForURL(/\/admin/);
    expect(page.url()).toContain('/admin');
  });

  test('student login redirects to /student', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login('alice.nguyen', 'pass1001');
    await page.waitForURL(/\/student/);
    expect(page.url()).toContain('/student');
  });

  test('invalid credentials show error', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await loginPage.login('baduser', 'badpass');
    await expect(loginPage.errorMessage).toBeVisible();
  });

  test('empty fields show validation errors', async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.goto();
    await page.getByRole('button', { name: 'Sign in' }).click();
    await expect(loginPage.usernameError).toBeVisible();
    await expect(loginPage.passwordError).toBeVisible();
  });

  test('admin logout redirects to login', async ({ browser }) => {
    const context = await browser.newContext({ storageState: '.auth/admin.json' });
    const page = await context.newPage();
    await page.goto('/admin');
    const signOut = page.getByRole('button', { name: 'Sign out' });
    await expect(signOut).toBeVisible();
    await signOut.click();
    await page.waitForURL('/login');
    expect(page.url()).toContain('/login');
    await context.close();
  });

  test('student logout redirects to login', async ({ browser }) => {
    const context = await browser.newContext({ storageState: '.auth/student.json' });
    const page = await context.newPage();
    await page.goto('/student');
    const signOut = page.getByRole('button', { name: 'Sign out' });
    await expect(signOut).toBeVisible();
    await signOut.click();
    await page.waitForURL('/login');
    expect(page.url()).toContain('/login');
    await context.close();
  });

  test('unauthenticated access to /admin redirects to login', async ({ page }) => {
    await page.goto('/admin/companies');
    await page.waitForURL('/login');
    expect(page.url()).toContain('/login');
  });

  test('unauthenticated access to /student redirects to login', async ({ page }) => {
    await page.goto('/student/profile');
    await page.waitForURL('/login');
    expect(page.url()).toContain('/login');
  });
});
