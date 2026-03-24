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
    await this.page.locator('input[name="studentId"]').fill(data.studentId);
    await this.page.locator('input[name="fname"]').fill(data.fname);
    await this.page.locator('input[name="lname"]').fill(data.lname);
    await this.page.locator('input[name="stuEmail"]').fill(data.stuEmail);
    if (data.mname != null) await this.page.locator('input[name="mname"]').fill(data.mname);
    if (data.year != null) await this.page.locator('input[name="year"]').fill(data.year);
    if (data.gender != null) await this.page.locator('select[name="gender"]').selectOption(data.gender);
    if (data.semester != null) await this.page.locator('input[name="semester"]').fill(data.semester);
    if (data.country != null) await this.page.locator('input[name="country"]').fill(data.country);
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
