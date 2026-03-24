import type { Page } from '@playwright/test';

export class CompaniesPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/admin/companies');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Companies' });
  }

  get addButton() {
    return this.page.getByRole('button', { name: 'Add Company' });
  }

  get table() {
    return this.page.getByRole('table');
  }

  row(companyName: string) {
    return this.page.getByRole('row').filter({ hasText: companyName });
  }

  async clickAdd() {
    await this.addButton.click();
  }

  async editRow(companyName: string) {
    const row = this.row(companyName);
    // The edit button is the first icon button in the actions column
    await row.locator('button').first().click();
  }

  async deleteRow(companyName: string) {
    const row = this.row(companyName);
    // The delete button is the second icon button in the actions column
    await row.locator('button').nth(1).click();
  }
}
