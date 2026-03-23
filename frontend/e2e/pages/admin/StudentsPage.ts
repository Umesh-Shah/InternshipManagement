import type { Page } from '@playwright/test';

export class StudentsPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/admin/students');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Students' });
  }

  get addButton() {
    return this.page.getByRole('button', { name: 'Add Student' });
  }

  get table() {
    return this.page.getByRole('table');
  }

  row(studentName: string) {
    return this.page.getByRole('row').filter({ hasText: studentName });
  }

  async clickAdd() {
    await this.addButton.click();
  }

  async viewProfile(studentName: string) {
    await this.row(studentName).getByRole('button', { name: 'View Profile' }).click();
  }
}
