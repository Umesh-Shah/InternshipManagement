import { Outlet, useNavigate } from 'react-router-dom';
import { LogOut } from 'lucide-react';
import { AdminSidebar } from './AdminSidebar';
import useAuthStore from '@/features/auth/useAuthStore';

export function AdminShell() {
  const clearAuth = useAuthStore(s => s.clearAuth);
  const navigate = useNavigate();

  function handleSignOut() {
    clearAuth();
    navigate('/login');
  }

  return (
    <div className="flex h-screen bg-background overflow-hidden">
      <AdminSidebar />
      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="h-14 border-b border-border flex items-center justify-end px-6 shrink-0">
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
