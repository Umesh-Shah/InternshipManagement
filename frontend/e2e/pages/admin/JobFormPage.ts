import type { Page } from '@playwright/test';

export class JobFormPage {
  constructor(private page: Page) {}

  get heading() {
    return this.page.getByRole('heading', { level: 1 });
  }

  async fillForm(data: {
    jobPosition: string;
    companyName: string;
    salary?: string;
    internshipType?: string;
    jobSkill?: string;
    description?: string;
    requirements?: string;
    responsibilities?: string;
  }) {
    await this.page.locator('input[name="jobPosition"]').fill(data.jobPosition);
    await this.page.locator('select[name="companyId"]').selectOption({ label: data.companyName });
    if (data.salary != null) await this.page.locator('input[name="salary"]').fill(data.salary);
    if (data.internshipType != null) await this.page.locator('select[name="internshipType"]').selectOption({ label: data.internshipType });
    if (data.jobSkill != null) await this.page.locator('input[name="jobSkill"]').fill(data.jobSkill);
    if (data.description != null) await this.page.locator('textarea[name="description"]').fill(data.description);
    if (data.requirements != null) await this.page.locator('textarea[name="requirements"]').fill(data.requirements);
    if (data.responsibilities != null) await this.page.locator('textarea[name="responsibilities"]').fill(data.responsibilities);
  }

  get submitButton() {
    return this.page.getByRole('button', { name: /Create Job|Save Changes/ });
  }

  get cancelButton() {
    return this.page.getByRole('button', { name: 'Cancel' });
  }

  async submit() {
    await this.submitButton.click();
  }
}
