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
    // Labels don't use htmlFor, so locate by label text then the sibling input
    const fill = async (label: string, value?: string) => {
      if (value == null) return;
      const container = this.page.locator('div').filter({ hasText: new RegExp(`^${label}`) }).first();
      const input = container.locator('input, textarea');
      await input.fill(value);
    };

    await fill('Company Name', data.companyName);
    await fill('Email', data.email);
    await fill('Address', data.address);
    await fill('City', data.city);
    await fill('Postal Code', data.postalCode);
    await fill('Country', data.country);
    await fill('Contact First Name', data.contactPersonFname);
    await fill('Contact Last Name', data.contactPersonLname);
    await fill('Telephone', data.telephone);
    await fill('Website', data.companyWebsite);
    await fill('Notes', data.notes);
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
