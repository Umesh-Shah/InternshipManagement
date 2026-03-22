import { Navigate, Outlet } from 'react-router-dom';
import useAuthStore from '@/features/auth/useAuthStore';

interface Props {
  requiredRole?: string;
}

function isTokenExpired(token: string): boolean {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return typeof payload.exp === 'number' && payload.exp * 1000 < Date.now();
  } catch {
    return true;
  }
}

export default function ProtectedRoute({ requiredRole }: Props) {
  const { token, user, clearAuth } = useAuthStore();

  if (!token || isTokenExpired(token)) {
    if (token) clearAuth();
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && user?.role !== requiredRole) {
    const fallback = user?.role === 'ROLE_ADMIN' ? '/admin' : '/student';
    return <Navigate to={fallback} replace />;
  }

  return <Outlet />;
}
