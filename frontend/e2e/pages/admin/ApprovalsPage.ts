import type { Page } from '@playwright/test';

export class ApprovalsPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/admin/approvals');
  }

  get heading() {
    return this.page.getByRole('heading', { name: 'Job Application Approvals' });
  }

  get emptyMessage() {
    return this.page.getByText('No pending applications.');
  }

  get table() {
    return this.page.getByRole('table');
  }

  row(studentName: string) {
    return this.page.getByRole('row').filter({ hasText: studentName });
  }

  async approveRow(studentName: string) {
    await this.row(studentName).getByRole('button', { name: 'Approve' }).click();
  }
}
