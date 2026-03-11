import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '@/features/auth/LoginPage';
import ProtectedRoute from './ProtectedRoute';
import { AdminRoutes } from './AdminRoutes';
import { StudentRoutes } from './StudentRoutes';

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route element={<ProtectedRoute requiredRole="ROLE_ADMIN" />}>
          <Route path="/admin/*" element={<AdminRoutes />} />
        </Route>

        <Route element={<ProtectedRoute requiredRole="ROLE_STUDENT" />}>
          <Route path="/student/*" element={<StudentRoutes />} />
        </Route>

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
