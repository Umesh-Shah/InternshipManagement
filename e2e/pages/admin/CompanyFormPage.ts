import type { Page } from '@playwright/test';

export class CompanyFormPage {
  constructor(private page: Page) {}

  get heading() {
    return this.page.getByRole('heading', { level: 1 });
  }

  field(label: string) {
    return this.page.getByLabel(label);
  }

  async fillForm(data: {
    companyName: string;
    email?: string;
    address?: string;
    city?: string;
    postalCode?: string;
    country?: string;
    contactPersonFname?: string;
    contactPersonLname?: string;
    telephone?: string;
    companyWebsite?: string;
    notes?: string;
  }) {
    const fill = async (selector: string, value?: string) => {
      if (value == null) return;
      await this.page.locator(selector).fill(value);
    };

    await fill('input[name="companyName"]', data.companyName);
    await fill('input[name="email"]', data.email);
    await fill('input[name="address"]', data.address);
    await fill('input[name="city"]', data.city);
    await fill('input[name="postalCode"]', data.postalCode);
    await fill('input[name="country"]', data.country);
    await fill('input[name="contactPersonFname"]', data.contactPersonFname);
    await fill('input[name="contactPersonLname"]', data.contactPersonLname);
    await fill('input[name="telephone"]', data.telephone);
    await fill('input[name="companyWebsite"]', data.companyWebsite);
    await fill('textarea[name="notes"]', data.notes);
  }

  get submitButton() {
    return this.page.getByRole('button', { name: /Create Company|Save Changes/ });
  }

  get cancelButton() {
    return this.page.getByRole('button', { name: 'Cancel' });
  }

  async submit() {
    await this.submitButton.click();
  }
}
