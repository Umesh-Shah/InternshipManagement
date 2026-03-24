import { test, expect } from '@playwright/test';

test.describe('Admin — Internship Status', () => {
  test('displays internship status page', async ({ page }) => {
    await page.goto('/admin/internships');
    await expect(page.getByRole('heading', { name: 'Internship Status' })).toBeVisible();
    // Either shows assignments or empty message
    await expect(
      page.getByRole('table').or(page.getByText('No internship assignments yet.'))
    ).toBeVisible();
  });

  test('assign internship link navigates to form', async ({ page }) => {
    await page.goto('/admin/internships');
    await page.getByRole('link', { name: 'Assign Internship' }).click();
    await page.waitForURL('/admin/internships/assign');
    await expect(page.getByRole('heading', { name: 'Assign Internship' })).toBeVisible();
  });

  test('assign internship form has required dropdowns', async ({ page }) => {
    await page.goto('/admin/internships/assign');
    const main = page.getByRole('main');
    await expect(main.getByText('Student', { exact: true })).toBeVisible();
    await expect(main.getByText('Company', { exact: true })).toBeVisible();
    await expect(main.getByText('Job', { exact: true })).toBeVisible();
    await expect(main.getByText('Internship Type', { exact: true })).toBeVisible();
    await expect(main.getByText('Internship Status', { exact: true })).toBeVisible();
  });
});
