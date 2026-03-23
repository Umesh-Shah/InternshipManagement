import { test, expect } from '@playwright/test';
import { ApprovalsPage } from '../../pages/admin/ApprovalsPage';

test.describe('Admin — Job Application Approvals', () => {
  test('displays approvals page', async ({ page }) => {
    const approvalsPage = new ApprovalsPage(page);
    await approvalsPage.goto();
    await expect(approvalsPage.heading).toBeVisible();
    // Either shows pending applications table or empty message
    const hasTable = await approvalsPage.table.isVisible().catch(() => false);
    const hasEmpty = await approvalsPage.emptyMessage.isVisible().catch(() => false);
    expect(hasTable || hasEmpty).toBeTruthy();
  });

  test('approve a pending application if available', async ({ page }) => {
    const approvalsPage = new ApprovalsPage(page);
    await approvalsPage.goto();
    await expect(approvalsPage.heading).toBeVisible();

    const hasTable = await approvalsPage.table.isVisible().catch(() => false);
    if (!hasTable) {
      test.skip();
      return;
    }

    // Click approve on the first pending row
    const firstApproveButton = page.getByRole('button', { name: 'Approve' }).first();
    if (await firstApproveButton.isVisible()) {
      await firstApproveButton.click();
      // After approval, the row should disappear or the table should update
      await page.waitForTimeout(500);
    }
  });
});
