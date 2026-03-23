import { test, expect } from '@playwright/test';
import { EducationPage } from '../../pages/student/EducationPage';

test.describe('Student — Education', () => {
  test('displays education form', async ({ page }) => {
    const educationPage = new EducationPage(page);
    await educationPage.goto();
    await expect(educationPage.heading).toBeVisible();
    await expect(educationPage.saveButton).toBeVisible();
  });

  test('update education and see success message', async ({ page }) => {
    const educationPage = new EducationPage(page);
    await educationPage.goto();
    await expect(educationPage.heading).toBeVisible();

    // Update the major field
    const majorContainer = page.locator('div').filter({ hasText: /^Major$/ }).first();
    await majorContainer.locator('input').fill('Computer Science');

    await educationPage.save();
    await expect(educationPage.successMessage).toBeVisible();
  });
});
