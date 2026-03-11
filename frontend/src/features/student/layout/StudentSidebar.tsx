import { NavLink } from 'react-router-dom';
import { User, GraduationCap, Award, Briefcase, Wrench, Search, ClipboardList, Activity } from 'lucide-react';
import { cn } from '@/lib/utils';

const links = [
  { to: '/student/profile', label: 'Profile', icon: User },
  { to: '/student/education', label: 'Education', icon: GraduationCap },
  { to: '/student/certificates', label: 'Certificates', icon: Award },
  { to: '/student/work', label: 'Work Experience', icon: Briefcase },
  { to: '/student/skills', label: 'Skills', icon: Wrench },
  { to: '/student/jobs', label: 'Browse Jobs', icon: Search },
  { to: '/student/my-applications', label: 'My Applications', icon: ClipboardList },
  { to: '/student/internship-status', label: 'Internship Status', icon: Activity },
];

export function StudentSidebar() {
  return (
    <aside className="w-56 shrink-0 bg-sidebar border-r border-sidebar-border h-full flex flex-col">
      <div className="px-6 py-5 border-b border-sidebar-border">
        <span className="font-semibold text-sidebar-foreground text-sm tracking-wide uppercase">
          IMS Student
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
