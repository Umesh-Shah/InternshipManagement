import type { Page } from '@playwright/test';

export class StudentSidebar {
  constructor(private page: Page) {}

  navLink(label: string) {
    return this.page.getByRole('link', { name: label });
  }

  async navigateTo(label: string) {
    await this.navLink(label).click();
  }
}
