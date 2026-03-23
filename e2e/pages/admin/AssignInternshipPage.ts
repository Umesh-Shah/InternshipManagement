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
    await this.page.locator('select[name="studentId"]').selectOption({ label: name });
  }

  async selectCompany(name: string) {
    await this.page.locator('select[name="companyId"]').selectOption({ label: name });
  }

  async selectJob(name: string) {
    await this.page.locator('select[name="jobId"]').selectOption({ label: name });
  }

  async selectInternshipType(type: string) {
    await this.page.locator('select[name="internshipTypeId"]').selectOption({ label: type });
  }

  get submitButton() {
    return this.page.getByRole('button', { name: 'Assign Internship' });
  }

  async submit() {
    await this.submitButton.click();
  }
}
