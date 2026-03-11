import { Navigate, Outlet } from 'react-router-dom';
import useAuthStore from '@/features/auth/useAuthStore';

interface Props {
  requiredRole?: string;
}

export default function ProtectedRoute({ requiredRole }: Props) {
  const { token, user } = useAuthStore();

  if (!token) return <Navigate to="/login" replace />;
  if (requiredRole && user?.role !== requiredRole) return <Navigate to="/login" replace />;

  return <Outlet />;
}
