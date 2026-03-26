import { test, expect } from '@playwright/test';
import { InternshipTypesPage } from '../../pages/admin/InternshipTypesPage';

test.describe('Admin — Internship Types CRUD', () => {
  test('displays internship types list', async ({ page }) => {
    const typesPage = new InternshipTypesPage(page);
    await typesPage.goto();
    await expect(typesPage.heading).toBeVisible();
    await expect(typesPage.table).toBeVisible();
  });

  test('create a new internship type', async ({ page }) => {
    const uid = Date.now().toString().slice(-6);
    const name = `E2E Type ${uid}`;
    const code = `E2E${uid}`;

    const typesPage = new InternshipTypesPage(page);
    await typesPage.goto();
    await typesPage.clickAdd();

    await expect(page.getByRole('heading', { level: 1 })).toHaveText('Add Internship Type');

    // Fill form fields
    const nameContainer = page.locator('div').filter({ hasText: /^Name/ }).first();
    await nameContainer.locator('input').fill(name);

    const typeContainer = page.locator('div').filter({ hasText: /^Type Code/ }).first();
    await typeContainer.locator('input').fill(code);

    await page.getByRole('button', { name: /Create/ }).click();
    await page.waitForURL('/admin/internship-types');
    await expect(typesPage.row(name)).toBeVisible();
  });

  test('delete an internship type', async ({ page }) => {
    const typesPage = new InternshipTypesPage(page);
    await typesPage.goto();
    await typesPage.clickAdd();

    const nameContainer = page.locator('div').filter({ hasText: /^Name/ }).first();
    await nameContainer.locator('input').fill('Temp Type');
    const typeContainer = page.locator('div').filter({ hasText: /^Type Code/ }).first();
    await typeContainer.locator('input').fill('TEMP');
    await page.getByRole('button', { name: /Create/ }).click();
    await page.waitForURL('/admin/internship-types');
    await expect(typesPage.row('Temp Type')).toBeVisible();

    page.on('dialog', dialog => dialog.accept());
    await typesPage.deleteRow('Temp Type');
    await expect(typesPage.row('Temp Type')).toBeHidden();
  });
});
