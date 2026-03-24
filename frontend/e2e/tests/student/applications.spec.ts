import { test, expect } from '@playwright/test';
import { MyApplicationsPage } from '../../pages/student/MyApplicationsPage';

test.describe('Student — My Applications', () => {
  test('displays my applications page', async ({ page }) => {
    const applicationsPage = new MyApplicationsPage(page);
    await applicationsPage.goto();
    await expect(applicationsPage.heading).toBeVisible();
  });

  test('shows applications or empty state', async ({ page }) => {
    const applicationsPage = new MyApplicationsPage(page);
    await applicationsPage.goto();
    await expect(applicationsPage.heading).toBeVisible();

    // Page must show either application cards or the empty message — never neither
    await expect(
      page.locator('.border.rounded-lg').first().or(applicationsPage.emptyMessage)
    ).toBeVisible();
  });
});
