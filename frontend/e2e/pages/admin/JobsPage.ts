import type { Page } from '@playwright/test';

export class JobsPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/admin/jobs');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Jobs' });
  }

  get addButton() {
    return this.page.getByRole('button', { name: 'Add Job' });
  }

  get table() {
    return this.page.getByRole('table');
  }

  row(jobPosition: string) {
    return this.page.getByRole('row').filter({ hasText: jobPosition });
  }

  async clickAdd() {
    await this.addButton.click();
  }

  async editRow(jobPosition: string) {
    await this.row(jobPosition).locator('button').first().click();
  }

  async deleteRow(jobPosition: string) {
    await this.row(jobPosition).locator('button').nth(1).click();
  }
}
