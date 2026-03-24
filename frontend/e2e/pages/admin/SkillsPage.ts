import type { Page } from '@playwright/test';

export class SkillsPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/admin/skills');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Skills' });
  }

  get addButton() {
    return this.page.getByRole('button', { name: 'Add Skill' });
  }

  get table() {
    return this.page.getByRole('table');
  }

  row(skillName: string) {
    return this.page.getByRole('row').filter({ hasText: skillName });
  }

  async clickAdd() {
    await this.addButton.click();
  }

  async editRow(skillName: string) {
    await this.row(skillName).locator('button').first().click();
  }

  async deleteRow(skillName: string) {
    await this.row(skillName).locator('button').nth(1).click();
  }
}
