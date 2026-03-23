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
    const fill = async (label: string, value?: string) => {
      if (value == null) return;
      const container = this.page.locator('div').filter({ hasText: new RegExp(`^${label}`) }).first();
      const input = container.locator('input, textarea, select');
      const tag = await input.evaluate(el => el.tagName.toLowerCase());
      if (tag === 'select') {
        await input.selectOption({ label: value });
      } else {
        await input.fill(value);
      }
    };

    await fill('Position', data.jobPosition);
    await fill('Company', data.companyName);
    await fill('Salary', data.salary);
    await fill('Internship Type', data.internshipType);
    await fill('Skills', data.jobSkill);
    await fill('Description', data.description);
    await fill('Requirements', data.requirements);
    await fill('Responsibilities', data.responsibilities);
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
