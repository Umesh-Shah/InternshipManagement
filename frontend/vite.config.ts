import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [tailwindcss(), react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: ['./src/test/setup.ts'],
    css: false,
    include: ['tests/unit/**/*.{test,spec}.{ts,tsx}'],
    exclude: ['**/node_modules/**', 'tests/e2e/**'],
    coverage: {
      provider: 'v8',
      include: ['src/**'],
      exclude: [
        'src/main.tsx',
        'src/App.tsx',
        'src/router/**',
        'src/api/**',
        'src/features/admin/**',
        'src/features/student/**',
        'src/**/*.d.ts',
      ],
      thresholds: {
        lines: 60,
        functions: 60,
        branches: 50,
      },
    },
  },
})
