import { test, expect } from '@playwright/test';
import { JobsPage } from '../../pages/admin/JobsPage';
import { JobFormPage } from '../../pages/admin/JobFormPage';

test.describe('Admin — Jobs CRUD', () => {
  test('displays jobs list with seeded data', async ({ page }) => {
    const jobsPage = new JobsPage(page);
    await jobsPage.goto();
    await expect(jobsPage.heading).toBeVisible();
    await expect(jobsPage.table).toBeVisible();
    // Seed data includes "Full Stack Developer Intern"
    await expect(jobsPage.row('Full Stack Developer Intern')).toBeVisible();
  });

  test('create a new job', async ({ page }) => {
    const jobsPage = new JobsPage(page);
    await jobsPage.goto();
    await jobsPage.clickAdd();

    const form = new JobFormPage(page);
    await expect(form.heading).toHaveText('Add Job');
    await form.fillForm({
      jobPosition: 'E2E Test Position',
      companyName: 'TechNova Solutions',
      salary: '50000',
      description: 'A test job created by E2E tests',
    });
    await form.submit();

    await page.waitForURL('/admin/jobs');
    await expect(jobsPage.row('E2E Test Position')).toBeVisible();
  });

  test('edit an existing job', async ({ page }) => {
    const jobsPage = new JobsPage(page);
    await jobsPage.goto();
    await expect(jobsPage.table).toBeVisible();

    await jobsPage.editRow('Full Stack Developer Intern');

    const form = new JobFormPage(page);
    await expect(form.heading).toHaveText('Edit Job');
    await form.submit();

    await page.waitForURL('/admin/jobs');
  });

  test('delete a job', async ({ page }) => {
    // Create a job to delete
    const jobsPage = new JobsPage(page);
    await jobsPage.goto();
    await jobsPage.clickAdd();

    const form = new JobFormPage(page);
    await form.fillForm({
      jobPosition: 'Delete Me Job',
      companyName: 'TechNova Solutions',
    });
    await form.submit();
    await page.waitForURL('/admin/jobs');
    await expect(jobsPage.row('Delete Me Job')).toBeVisible();

    page.on('dialog', dialog => dialog.accept());
    await jobsPage.deleteRow('Delete Me Job');
    await expect(jobsPage.row('Delete Me Job')).toBeHidden();
  });
});
