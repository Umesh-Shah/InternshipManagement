import type { Page } from '@playwright/test';

export class ProfilePage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/student/profile');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'My Profile' });
  }

  field(label: string) {
    const container = this.page.locator('div').filter({ hasText: new RegExp(`^${label}$`) }).first();
    return container.locator('input, select');
  }

  get saveButton() {
    return this.page.getByRole('button', { name: 'Save Changes' });
  }

  get successMessage() {
    return this.page.getByText('Saved.');
  }

  get errorMessage() {
    return this.page.getByText('Save failed.');
  }

  async save() {
    await this.saveButton.click();
  }
}
