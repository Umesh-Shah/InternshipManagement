import { test, expect } from '@playwright/test';
import { MyApplicationsPage } from '../../pages/student/MyApplicationsPage';

test.describe('Student — My Applications', () => {
  test('displays my applications page', async ({ page }) => {
    const applicationsPage = new MyApplicationsPage(page);
    await applicationsPage.goto();
    await expect(applicationsPage.heading).toBeVisible();
  });

  test('shows applications with status badges', async ({ page }) => {
    const applicationsPage = new MyApplicationsPage(page);
    await applicationsPage.goto();
    await expect(applicationsPage.heading).toBeVisible();

    // Either shows applications or empty message
    const hasApplications = await page.locator('.border.rounded-lg').count();
    const hasEmpty = await applicationsPage.emptyMessage.isVisible().catch(() => false);

    if (hasApplications > 0) {
      // Check that status badges are present (Pending or Approved)
      const pendingCount = await page.getByText('Pending').count();
      const approvedCount = await page.getByText('Approved').count();
      expect(pendingCount + approvedCount).toBeGreaterThan(0);
    } else {
      expect(hasEmpty).toBeTruthy();
    }
  });
});
