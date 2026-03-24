import { test, expect } from '@playwright/test';
import { JobsPage } from '../../pages/admin/JobsPage';
import { JobFormPage } from '../../pages/admin/JobFormPage';

test.describe('Admin — Jobs CRUD', () => {
  test('displays jobs list with seeded data', async ({ page }) => {
    const jobsPage = new JobsPage(page);
    await jobsPage.goto();
    await expect(jobsPage.heading).toBeVisible();
    await expect(jobsPage.table).toBeVisible();
    // Seed data includes "Full Stack Web Developer"
    await expect(jobsPage.row('Full Stack Web Developer')).toBeVisible();
  });

  test('create a new job', async ({ page }) => {
    const uid = Date.now().toString().slice(-6);
    const name = `E2E Job ${uid}`;

    const jobsPage = new JobsPage(page);
    await jobsPage.goto();
    await jobsPage.clickAdd();

    const form = new JobFormPage(page);
    await expect(form.heading).toHaveText('Add Job');
    await form.fillForm({
      jobPosition: name,
      companyName: 'TechNova Solutions',
      salary: '50000',
      description: 'A test job created by E2E tests',
    });
    await form.submit();

    await page.waitForURL('/admin/jobs');
    await expect(jobsPage.row(name)).toBeVisible();
  });

  test('edit an existing job', async ({ page }) => {
    const jobsPage = new JobsPage(page);
    await jobsPage.goto();
    await expect(jobsPage.table).toBeVisible();

    await jobsPage.editRow('Full Stack Web Developer');

    const form = new JobFormPage(page);
    await expect(form.heading).toHaveText('Edit Job');
    await form.submit();

    await page.waitForURL('/admin/jobs');
  });

  test('delete a job', async ({ page }) => {
    const uid = Date.now().toString().slice(-6);
    const name = `Del Job ${uid}`;

    // Create a job to delete
    const jobsPage = new JobsPage(page);
    await jobsPage.goto();
    await jobsPage.clickAdd();

    const form = new JobFormPage(page);
    await form.fillForm({
      jobPosition: name,
      companyName: 'TechNova Solutions',
    });
    await form.submit();
    await page.waitForURL('/admin/jobs');
    await expect(jobsPage.row(name)).toBeVisible();

    page.on('dialog', dialog => dialog.accept());
    await jobsPage.deleteRow(name);
    await expect(jobsPage.row(name)).toBeHidden();
  });
});
