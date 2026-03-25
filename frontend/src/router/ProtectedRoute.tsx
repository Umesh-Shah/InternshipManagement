import { Navigate, Outlet } from 'react-router-dom';
import useAuthStore from '@/features/auth/useAuthStore';
import { ROLE_ADMIN, ROUTES } from '@/constants';

interface Props {
  requiredRole?: string;
}

export default function ProtectedRoute({ requiredRole }: Props) {
  const { user, clearAuth, isExpired } = useAuthStore();

  if (!user || isExpired()) {
    if (user) clearAuth();
    return <Navigate to={ROUTES.LOGIN} replace />;
  }

  if (requiredRole && user.role !== requiredRole) {
    const fallback = user.role === ROLE_ADMIN ? ROUTES.ADMIN : ROUTES.STUDENT;
    return <Navigate to={fallback} replace />;
  }

  return <Outlet />;
}
