import { test, expect } from '@playwright/test';
import { ReportsPage } from '../../pages/admin/ReportsPage';

test.describe('Admin — Reports', () => {
  test('displays reports page with tabs', async ({ page }) => {
    const reportsPage = new ReportsPage(page);
    await reportsPage.goto();
    await expect(reportsPage.heading).toBeVisible();
    // All tabs should be visible
    await expect(reportsPage.tab('Students')).toBeVisible();
    await expect(reportsPage.tab('Companies')).toBeVisible();
    await expect(reportsPage.tab('Internship Types')).toBeVisible();
    await expect(reportsPage.tab('GPA')).toBeVisible();
    await expect(reportsPage.tab('Jobs')).toBeVisible();
  });

  test('students report shows table data', async ({ page }) => {
    const reportsPage = new ReportsPage(page);
    await reportsPage.goto();
    // Students tab is active by default
    await expect(reportsPage.table).toBeVisible();
    await expect(reportsPage.downloadPdfButton).toBeVisible();
  });

  test('companies report shows table data', async ({ page }) => {
    const reportsPage = new ReportsPage(page);
    await reportsPage.goto();
    await reportsPage.switchTab('Companies');
    await expect(reportsPage.table).toBeVisible();
    await expect(reportsPage.downloadPdfButton).toBeVisible();
  });

  test('jobs report shows table data', async ({ page }) => {
    const reportsPage = new ReportsPage(page);
    await reportsPage.goto();
    await reportsPage.switchTab('Jobs');
    await expect(reportsPage.table).toBeVisible();
    await expect(reportsPage.downloadPdfButton).toBeVisible();
  });

  test('GPA report shows table data', async ({ page }) => {
    const reportsPage = new ReportsPage(page);
    await reportsPage.goto();
    await reportsPage.switchTab('GPA');
    await expect(reportsPage.table).toBeVisible();
    await expect(reportsPage.downloadPdfButton).toBeVisible();
  });

  test('PDF download triggers download', async ({ page }) => {
    const reportsPage = new ReportsPage(page);
    await reportsPage.goto();
    await reportsPage.switchTab('Jobs');
    await expect(reportsPage.table).toBeVisible();

    const downloadPromise = page.waitForEvent('download');
    await reportsPage.downloadPdfButton.click();
    const download = await downloadPromise;
    expect(download.suggestedFilename()).toContain('.pdf');
  });
});
