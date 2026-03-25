import { Outlet, useNavigate } from 'react-router-dom';
import { LogOut } from 'lucide-react';
import { StudentSidebar } from './StudentSidebar';
import useAuthStore from '@/features/auth/useAuthStore';
import { logout } from '@/api/auth.api';
import { ROUTES } from '@/constants';

export function StudentShell() {
  const clearAuth = useAuthStore(s => s.clearAuth);
  const user = useAuthStore(s => s.user);
  const navigate = useNavigate();

  async function handleSignOut() {
    await logout().catch(() => {});
    clearAuth();
    navigate(ROUTES.LOGIN);
  }

  return (
    <div className="flex h-screen bg-background overflow-hidden">
      <StudentSidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="h-14 border-b border-border flex items-center justify-between px-6 shrink-0">
          <span className="text-sm text-muted-foreground">
            {user?.username}
          </span>
          <button
            onClick={handleSignOut}
            className="flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground transition-colors"
          >
            <LogOut size={16} />
            Sign out
          </button>
        </header>
        <main className="flex-1 overflow-y-auto p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
