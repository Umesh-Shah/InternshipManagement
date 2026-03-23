import { test, expect } from '@playwright/test';
import { SkillsPage } from '../../pages/admin/SkillsPage';
import { SkillFormPage } from '../../pages/admin/SkillFormPage';

test.describe('Admin — Skills CRUD', () => {
  test('displays skills list with seeded data', async ({ page }) => {
    const skillsPage = new SkillsPage(page);
    await skillsPage.goto();
    await expect(skillsPage.heading).toBeVisible();
    await expect(skillsPage.table).toBeVisible();
  });

  test('create a new skill', async ({ page }) => {
    const skillsPage = new SkillsPage(page);
    await skillsPage.goto();
    await skillsPage.clickAdd();

    const form = new SkillFormPage(page);
    await expect(form.heading).toHaveText('Add Skill');
    await form.fillForm({ skillName: 'E2E Testing', skillType: 'Technical' });
    await form.submit();

    await page.waitForURL('/admin/skills');
    await expect(skillsPage.row('E2E Testing')).toBeVisible();
  });

  test('edit a skill', async ({ page }) => {
    const skillsPage = new SkillsPage(page);
    await skillsPage.goto();
    await expect(skillsPage.table).toBeVisible();

    // Edit the first visible skill
    const firstRow = page.getByRole('row').nth(1); // skip header
    await firstRow.locator('button').first().click();

    const form = new SkillFormPage(page);
    await expect(form.heading).toHaveText('Edit Skill');
    await form.submit();

    await page.waitForURL('/admin/skills');
  });

  test('delete a skill', async ({ page }) => {
    // Create a skill to delete
    const skillsPage = new SkillsPage(page);
    await skillsPage.goto();
    await skillsPage.clickAdd();

    const form = new SkillFormPage(page);
    await form.fillForm({ skillName: 'Delete Me Skill' });
    await form.submit();
    await page.waitForURL('/admin/skills');
    await expect(skillsPage.row('Delete Me Skill')).toBeVisible();

    page.on('dialog', dialog => dialog.accept());
    await skillsPage.deleteRow('Delete Me Skill');
    await expect(skillsPage.row('Delete Me Skill')).toBeHidden();
  });
});
