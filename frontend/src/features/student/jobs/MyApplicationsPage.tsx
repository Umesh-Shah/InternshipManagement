import { useQuery } from '@tanstack/react-query';
import { jobApplicationApi } from '@/api/jobApplication.api';
import useAuthStore from '@/features/auth/useAuthStore';
import { CheckCircle, Clock } from 'lucide-react';

const flagLabel: Record<string, { label: string; icon: React.ReactNode }> = {
  N: { label: 'Pending', icon: <Clock size={14} className="text-yellow-500" /> },
  A: { label: 'Approved', icon: <CheckCircle size={14} className="text-green-600" /> },
};

export function MyApplicationsPage() {
  const studentId = useAuthStore(s => s.user?.studentId) as number;

  const { data: applications = [], isLoading } = useQuery({
    queryKey: ['my-applications', studentId],
    queryFn: () => jobApplicationApi.getByStudent(studentId),
    enabled: !!studentId,
  });

  if (isLoading) return <p className="text-muted-foreground text-sm">Loading…</p>;

  return (
    <div className="max-w-3xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">My Applications</h1>

      {applications.length === 0 && (
        <p className="text-muted-foreground text-sm">
          You haven't expressed interest in any jobs yet.
        </p>
      )}

      <div className="space-y-3">
        {applications.map(app => {
          const status = flagLabel[app.flag] ?? { label: app.flag, icon: null };
          return (
            <div
              key={app.studentJobId}
              className="border border-border rounded-lg p-4 bg-card flex items-center justify-between gap-4"
            >
              <div>
                <p className="font-medium text-foreground">{app.jobPosition ?? `Job #${app.jobId}`}</p>
                {app.companyName && (
                  <p className="text-sm text-muted-foreground">{app.companyName}</p>
                )}
              </div>
              <div className="flex items-center gap-1.5 text-sm shrink-0">
                {status.icon}
                <span>{status.label}</span>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
