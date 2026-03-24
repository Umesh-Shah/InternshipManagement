import type { Page } from '@playwright/test';

export class ReportsPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/admin/reports');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Reports' });
  }

  tab(name: string) {
    return this.page.getByRole('button', { name, exact: true });
  }

  async switchTab(name: string) {
    await this.tab(name).click();
  }

  get table() {
    return this.page.getByRole('table');
  }

  get downloadPdfButton() {
    return this.page.getByRole('button', { name: 'Download PDF' });
  }
}
