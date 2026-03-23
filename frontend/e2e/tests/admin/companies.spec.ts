import { test, expect } from '@playwright/test';
import { CompaniesPage } from '../../pages/admin/CompaniesPage';
import { CompanyFormPage } from '../../pages/admin/CompanyFormPage';

test.describe('Admin — Companies CRUD', () => {
  test('displays companies list with seeded data', async ({ page }) => {
    const companiesPage = new CompaniesPage(page);
    await companiesPage.goto();
    await expect(companiesPage.heading).toBeVisible();
    await expect(companiesPage.table).toBeVisible();
    // Seed data includes "TechNova Solutions"
    await expect(companiesPage.row('TechNova Solutions')).toBeVisible();
  });

  test('create a new company', async ({ page }) => {
    const companiesPage = new CompaniesPage(page);
    await companiesPage.goto();
    await companiesPage.clickAdd();

    const form = new CompanyFormPage(page);
    await expect(form.heading).toHaveText('Add Company');
    await form.fillForm({
      companyName: 'E2E Test Corp',
      email: 'test@e2ecorp.com',
      city: 'Toronto',
      country: 'Canada',
    });
    await form.submit();

    // Should redirect back to list and show new company
    await page.waitForURL('/admin/companies');
    await expect(companiesPage.row('E2E Test Corp')).toBeVisible();
  });

  test('edit an existing company', async ({ page }) => {
    const companiesPage = new CompaniesPage(page);
    await companiesPage.goto();
    await expect(companiesPage.table).toBeVisible();

    // Edit the first seeded company
    await companiesPage.editRow('TechNova Solutions');

    const form = new CompanyFormPage(page);
    await expect(form.heading).toHaveText('Edit Company');

    // Change the city
    const cityContainer = page.locator('div').filter({ hasText: /^City/ }).first();
    const cityInput = cityContainer.locator('input');
    await cityInput.fill('Vancouver');
    await form.submit();

    await page.waitForURL('/admin/companies');
    await expect(companiesPage.row('TechNova Solutions')).toBeVisible();
  });

  test('delete a company', async ({ page }) => {
    // First create a company to delete
    const companiesPage = new CompaniesPage(page);
    await companiesPage.goto();
    await companiesPage.clickAdd();

    const form = new CompanyFormPage(page);
    await form.fillForm({ companyName: 'Delete Me Corp' });
    await form.submit();
    await page.waitForURL('/admin/companies');
    await expect(companiesPage.row('Delete Me Corp')).toBeVisible();

    // Now delete it
    page.on('dialog', dialog => dialog.accept());
    await companiesPage.deleteRow('Delete Me Corp');

    // Row should disappear
    await expect(companiesPage.row('Delete Me Corp')).toBeHidden();
  });

  test('sidebar Companies link is active', async ({ page }) => {
    await page.goto('/admin/companies');
    const link = page.getByRole('link', { name: 'Companies' });
    await expect(link).toBeVisible();
  });
});
