import type { Page } from '@playwright/test';

export class AssignInternshipPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/admin/internships/assign');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Assign Internship' });
  }

  async selectStudent(name: string) {
    const container = this.page.locator('div').filter({ hasText: /^Student/ }).first();
    await container.locator('select').selectOption({ label: name });
  }

  async selectCompany(name: string) {
    const container = this.page.locator('div').filter({ hasText: /^Company/ }).first();
    await container.locator('select').selectOption({ label: name });
  }

  async selectJob(name: string) {
    const container = this.page.locator('div').filter({ hasText: /^Job/ }).first();
    await container.locator('select').selectOption({ label: name });
  }

  async selectInternshipType(type: string) {
    const container = this.page.locator('div').filter({ hasText: /^Internship Type/ }).first();
    await container.locator('select').selectOption({ label: type });
  }

  get submitButton() {
    return this.page.getByRole('button', { name: 'Assign Internship' });
  }

  async submit() {
    await this.submitButton.click();
  }
}
