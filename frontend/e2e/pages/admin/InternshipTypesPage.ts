import type { Page } from '@playwright/test';

export class InternshipTypesPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/admin/internship-types');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Internship Types' });
  }

  get addButton() {
    return this.page.getByRole('button', { name: 'Add Type' });
  }

  get table() {
    return this.page.getByRole('table');
  }

  row(typeName: string) {
    return this.page.getByRole('row').filter({ hasText: typeName });
  }

  async clickAdd() {
    await this.addButton.click();
  }

  async editRow(typeName: string) {
    await this.row(typeName).locator('button').first().click();
  }

  async deleteRow(typeName: string) {
    await this.row(typeName).locator('button').nth(1).click();
  }
}
