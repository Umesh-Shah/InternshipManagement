import { test as base } from '@playwright/test';

/**
 * Extend the base test with any shared fixtures needed across test suites.
 * Currently just re-exports the base test — ready for future extensions
 * like custom page objects or test data helpers.
 */
export const test = base;
export { expect } from '@playwright/test';
