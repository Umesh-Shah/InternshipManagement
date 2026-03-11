import { NavLink } from 'react-router-dom';
import { Building2, Briefcase, Wrench, GraduationCap, Users, BarChart2, ClipboardCheck, Activity } from 'lucide-react';
import { cn } from '@/lib/utils';

const links = [
  { to: '/admin/companies', label: 'Companies', icon: Building2 },
  { to: '/admin/jobs', label: 'Jobs', icon: Briefcase },
  { to: '/admin/skills', label: 'Skills', icon: Wrench },
  { to: '/admin/internship-types', label: 'Internship Types', icon: GraduationCap },
  { to: '/admin/students', label: 'Students', icon: Users },
  { to: '/admin/approvals', label: 'Approvals', icon: ClipboardCheck },
  { to: '/admin/internships', label: 'Internship Status', icon: Activity },
  { to: '/admin/reports', label: 'Reports', icon: BarChart2 },
];

export function AdminSidebar() {
  return (
    <aside className="w-56 shrink-0 bg-sidebar border-r border-sidebar-border h-full flex flex-col">
      <div className="px-6 py-5 border-b border-sidebar-border">
        <span className="font-semibold text-sidebar-foreground text-sm tracking-wide uppercase">
          IMS Admin
        </span>
      </div>
      <nav className="flex-1 p-3 space-y-1">
        {links.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) =>
              cn(
                'flex items-center gap-3 px-3 py-2 rounded-md text-sm transition-colors',
                isActive
                  ? 'bg-sidebar-primary text-sidebar-primary-foreground'
                  : 'text-sidebar-foreground hover:bg-sidebar-accent hover:text-sidebar-accent-foreground'
              )
            }
          >
            <Icon size={16} />
            {label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
