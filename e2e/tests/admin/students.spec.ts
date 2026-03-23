import { test, expect } from '@playwright/test';
import { StudentsPage } from '../../pages/admin/StudentsPage';
import { StudentFormPage } from '../../pages/admin/StudentFormPage';

test.describe('Admin — Students Management', () => {
  test('displays students list with seeded data', async ({ page }) => {
    const studentsPage = new StudentsPage(page);
    await studentsPage.goto();
    await expect(studentsPage.heading).toBeVisible();
    await expect(studentsPage.table).toBeVisible();
    // Seed data includes Alice Nguyen
    await expect(studentsPage.row('Alice')).toBeVisible();
  });

  test('create a new student', async ({ page }) => {
    const studentsPage = new StudentsPage(page);
    await studentsPage.goto();
    await studentsPage.clickAdd();

    const form = new StudentFormPage(page);
    await expect(form.heading).toHaveText('Add Student');
    await form.fillForm({
      studentId: '9999',
      fname: 'E2E',
      lname: 'TestStudent',
      stuEmail: 'e2e@test.com',
      country: 'Canada',
    });
    await form.submit();

    await page.waitForURL('/admin/students');
    await expect(studentsPage.row('E2E')).toBeVisible();
  });

  test('view student profile', async ({ page }) => {
    const studentsPage = new StudentsPage(page);
    await studentsPage.goto();
    await expect(studentsPage.table).toBeVisible();

    await studentsPage.viewProfile('Alice');
    await expect(page.url()).toContain('/profile');
  });
});
