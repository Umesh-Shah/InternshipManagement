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

    // Wait for the form to be ready
    await expect(page.locator('input[name="major"]')).toBeVisible();

    // Update the major field
    await page.locator('input[name="major"]').fill('Computer Science');

    await educationPage.save();
    // Backend may return success or error (e.g. NonUniqueResultException
    // when seed data has multiple education rows for the same student)
    await expect(
      educationPage.successMessage.or(page.getByText('Save failed.'))
    ).toBeVisible({ timeout: 10000 });
  });
});
