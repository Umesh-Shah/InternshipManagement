import type { Page } from '@playwright/test';

export class SkillFormPage {
  constructor(private page: Page) {}

  get heading() {
    return this.page.getByRole('heading', { level: 1 });
  }

  async fillForm(data: { skillName: string; skillType?: string }) {
    const fill = async (label: string, value?: string) => {
      if (value == null) return;
      const container = this.page.locator('div').filter({ hasText: new RegExp(`^${label}`) }).first();
      await container.locator('input').fill(value);
    };

    await fill('Skill Name', data.skillName);
    await fill('Skill Type', data.skillType);
  }

  get submitButton() {
    return this.page.getByRole('button', { name: /Create Skill|Save Changes/ });
  }

  get cancelButton() {
    return this.page.getByRole('button', { name: 'Cancel' });
  }

  async submit() {
    await this.submitButton.click();
  }
}
