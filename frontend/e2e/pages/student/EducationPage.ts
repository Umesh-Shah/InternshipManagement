import type { Page } from '@playwright/test';

export class EducationPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/student/education');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Education' });
  }

  field(label: string) {
    const container = this.page.locator('div').filter({ hasText: new RegExp(`^${label}$`) }).first();
    return container.locator('input');
  }

  get saveButton() {
    return this.page.getByRole('button', { name: 'Save Changes' });
  }

  get successMessage() {
    return this.page.getByText('Saved.');
  }

  async save() {
    await this.saveButton.click();
  }
}
