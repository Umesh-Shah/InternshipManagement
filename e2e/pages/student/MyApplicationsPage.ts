import type { Page } from '@playwright/test';

export class MyApplicationsPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/student/my-applications');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'My Applications' });
  }

  get emptyMessage() {
    return this.page.getByText("You haven't expressed interest in any jobs yet.");
  }

  applicationCard(jobPosition: string) {
    return this.page.locator('.border').filter({ hasText: jobPosition });
  }

  statusOf(jobPosition: string) {
    return this.applicationCard(jobPosition).locator('.text-sm').last();
  }
}
