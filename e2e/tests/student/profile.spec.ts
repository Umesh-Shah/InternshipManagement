import { test, expect } from '@playwright/test';
import { ProfilePage } from '../../pages/student/ProfilePage';

test.describe('Student — Profile', () => {
  test('displays profile with pre-filled data', async ({ page }) => {
    const profilePage = new ProfilePage(page);
    await profilePage.goto();
    await expect(profilePage.heading).toBeVisible();
    await expect(profilePage.saveButton).toBeVisible();
  });

  test('update profile and see success message', async ({ page }) => {
    const profilePage = new ProfilePage(page);
    await profilePage.goto();
    await expect(profilePage.heading).toBeVisible();

    // Update the telephone field
    const telephoneContainer = page.locator('div').filter({ hasText: /^Telephone$/ }).first();
    await telephoneContainer.locator('input').fill('555-0199');

    await profilePage.save();
    await expect(profilePage.successMessage).toBeVisible();
  });
});
