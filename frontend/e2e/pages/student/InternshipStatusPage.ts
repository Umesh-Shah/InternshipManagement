import type { Page } from '@playwright/test';

export class StudentInternshipStatusPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/student/internship-status');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'My Internship Status' });
  }

  get emptyMessage() {
    return this.page.getByText('No internship assigned yet.');
  }

  get cards() {
    return this.page.locator('.border.rounded-lg');
  }
}
