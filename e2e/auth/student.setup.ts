import { test as setup } from '@playwright/test';
import { LoginPage } from '../pages/LoginPage';

setup('authenticate as student', async ({ page }) => {
  const loginPage = new LoginPage(page);
  await loginPage.goto();
  await loginPage.login('alice.nguyen', 'pass1001');
  await page.waitForURL(/\/student/);
  await page.context().storageState({ path: '.auth/student.json' });
});
