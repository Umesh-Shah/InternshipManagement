import { test, expect } from '@playwright/test';
import { StudentInternshipStatusPage } from '../../pages/student/InternshipStatusPage';

test.describe('Student — Internship Status', () => {
  test('displays internship status page', async ({ page }) => {
    const statusPage = new StudentInternshipStatusPage(page);
    await statusPage.goto();
    await expect(statusPage.heading).toBeVisible();
  });

  test('shows internship cards or empty message', async ({ page }) => {
    const statusPage = new StudentInternshipStatusPage(page);
    await statusPage.goto();
    await expect(statusPage.heading).toBeVisible();

    await expect(
      statusPage.cards.first().or(statusPage.emptyMessage)
    ).toBeVisible();

    if (await statusPage.cards.count() > 0) {
      // Verify card content has expected fields
      await expect(page.getByText('Company').first()).toBeVisible();
      await expect(page.getByText('Job').first()).toBeVisible();
      await expect(page.getByText('Internship Type').first()).toBeVisible();
    }
  });
});
