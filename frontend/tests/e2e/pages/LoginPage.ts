import type { Page } from '@playwright/test';

export class LoginPage {
  constructor(private page: Page) {}

  async goto() {
    await this.page.goto('/login');
  }

  async login(username: string, password: string) {
    await this.page.getByLabel('Username').fill(username);
    await this.page.getByLabel('Password').fill(password);
    await this.page.getByRole('button', { name: 'Sign in' }).click();
  }

  get errorMessage() {
    return this.page.getByText('Invalid username or password.');
  }

  get usernameError() {
    return this.page.getByText('Username is required');
  }

  get passwordError() {
    return this.page.getByText('Password is required');
  }
}
