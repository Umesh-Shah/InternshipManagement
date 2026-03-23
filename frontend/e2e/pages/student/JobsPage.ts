import type { Page } from '@playwright/test';

export class StudentJobsPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/student/jobs');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Browse Jobs' });
  }

  get emptyMessage() {
    return this.page.getByText('No jobs available.');
  }

  jobCard(position: string) {
    return this.page.locator('.border').filter({ hasText: position });
  }

  markInterestButton(position: string) {
    return this.jobCard(position).getByRole('button', { name: 'Mark Interest' });
  }

  interestedBadge(position: string) {
    return this.jobCard(position).getByRole('button', { name: 'Interested' });
  }
}
