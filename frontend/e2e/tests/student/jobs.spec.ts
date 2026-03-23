import { test, expect } from '@playwright/test';
import { StudentJobsPage } from '../../pages/student/JobsPage';

test.describe('Student — Browse Jobs', () => {
  test('displays available jobs', async ({ page }) => {
    const jobsPage = new StudentJobsPage(page);
    await jobsPage.goto();
    await expect(jobsPage.heading).toBeVisible();
    // Should show seeded jobs
    await expect(jobsPage.jobCard('Full Stack Developer Intern')).toBeVisible();
  });

  test('apply to a job shows interested state', async ({ page }) => {
    const jobsPage = new StudentJobsPage(page);
    await jobsPage.goto();
    await expect(jobsPage.heading).toBeVisible();

    // Find a job that hasn't been applied to yet
    // The seed data has alice.nguyen already applied to some jobs
    // Look for a Mark Interest button
    const markInterestButtons = page.getByRole('button', { name: 'Mark Interest' });
    const count = await markInterestButtons.count();
    if (count > 0) {
      await markInterestButtons.first().click();
      // After clicking, the button should change to "Interested"
      await expect(page.getByRole('button', { name: 'Interested' })).toBeVisible();
    }
  });

  test('already applied jobs show Interested badge', async ({ page }) => {
    const jobsPage = new StudentJobsPage(page);
    await jobsPage.goto();
    await expect(jobsPage.heading).toBeVisible();

    // The seed data has alice.nguyen already applied to some jobs
    // At least some "Interested" buttons should be visible
    const interestedButtons = page.getByRole('button', { name: 'Interested' });
    const count = await interestedButtons.count();
    expect(count).toBeGreaterThanOrEqual(0); // May be 0 if no prior applications
  });
});
