import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '@/features/auth/LoginPage';
import { ROLE_ADMIN, ROLE_STUDENT, ROUTES } from '@/constants';
import ProtectedRoute from './ProtectedRoute';
import { AdminRoutes } from './AdminRoutes';
import { StudentRoutes } from './StudentRoutes';
import { ErrorBoundary } from '@/components/ErrorBoundary';

export default function AppRouter() {
  return (
    <ErrorBoundary>
    <BrowserRouter>
      <Routes>
        <Route path={ROUTES.LOGIN} element={<LoginPage />} />

        <Route element={<ProtectedRoute requiredRole={ROLE_ADMIN} />}>
          <Route path={ROUTES.ADMIN + '/*'} element={<AdminRoutes />} />
        </Route>

        <Route element={<ProtectedRoute requiredRole={ROLE_STUDENT} />}>
          <Route path={ROUTES.STUDENT + '/*'} element={<StudentRoutes />} />
        </Route>

        <Route path="*" element={<Navigate to={ROUTES.LOGIN} replace />} />
      </Routes>
    </BrowserRouter>
    </ErrorBoundary>
  );
}
