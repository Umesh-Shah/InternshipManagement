import type { Page } from '@playwright/test';

export class StudentFormPage {
  constructor(private page: Page) {}

  get heading() {
    return this.page.getByRole('heading', { level: 1 });
  }

  async fillForm(data: {
    studentId: string;
    fname: string;
    lname: string;
    stuEmail: string;
    mname?: string;
    year?: string;
    gender?: string;
    semester?: string;
    country?: string;
  }) {
    const fill = async (label: string, value?: string) => {
      if (value == null) return;
      const container = this.page.locator('div').filter({ hasText: new RegExp(`^${label}`) }).first();
      const input = container.locator('input, select');
      const tag = await input.evaluate(el => el.tagName.toLowerCase());
      if (tag === 'select') {
        await input.selectOption(value);
      } else {
        await input.fill(value);
      }
    };

    await fill('Student ID', data.studentId);
    await fill('First Name', data.fname);
    await fill('Last Name', data.lname);
    await fill('Email', data.stuEmail);
    await fill('Middle Name', data.mname);
    await fill('Year', data.year);
    await fill('Gender', data.gender);
    await fill('Semester', data.semester);
    await fill('Country', data.country);
  }

  get submitButton() {
    return this.page.getByRole('button', { name: 'Create Student' });
  }

  get cancelButton() {
    return this.page.getByRole('button', { name: 'Cancel' });
  }

  async submit() {
    await this.submitButton.click();
  }
}
