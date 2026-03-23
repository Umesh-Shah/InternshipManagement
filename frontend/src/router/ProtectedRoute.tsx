import { Navigate, Outlet } from 'react-router-dom';
import useAuthStore from '@/features/auth/useAuthStore';

interface Props {
  requiredRole?: string;
}

export default function ProtectedRoute({ requiredRole }: Props) {
  const { expiresAt, user, clearAuth } = useAuthStore();

  if (!user || !expiresAt || expiresAt < Date.now()) {
    if (user) clearAuth();
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && user.role !== requiredRole) {
    const fallback = user.role === 'ROLE_ADMIN' ? '/admin' : '/student';
    return <Navigate to={fallback} replace />;
  }

  return <Outlet />;
}
