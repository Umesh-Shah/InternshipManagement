import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { jobApi } from '@/api/job.api';
import { companyApi } from '@/api/company.api';
import { jobApplicationApi } from '@/api/jobApplication.api';
import useAuthStore from '@/features/auth/useAuthStore';
import { Briefcase } from 'lucide-react';

export function JobsPage() {
  const studentId = useAuthStore(s => s.user?.studentId) as number;
  const queryClient = useQueryClient();

  const { data: jobs = [], isLoading } = useQuery({
    queryKey: ['jobs'],
    queryFn: () => jobApi.getAll(),
  });

  const { data: companies = [] } = useQuery({
    queryKey: ['companies'],
    queryFn: companyApi.getAll,
  });

  const { data: myApplications = [] } = useQuery({
    queryKey: ['my-applications', studentId],
    queryFn: () => jobApplicationApi.getByStudent(studentId),
    enabled: !!studentId,
  });

  const appliedJobIds = new Set(myApplications.map(a => a.jobId));

  const companyMap = Object.fromEntries(companies.map(c => [c.companyId, c.companyName]));

  const applyMutation = useMutation({
    mutationFn: (jobId: number) => jobApplicationApi.apply({ studentId, jobId }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['my-applications', studentId] });
    },
  });

  if (isLoading) return <p className="text-muted-foreground text-sm">Loading jobs…</p>;

  return (
    <div className="max-w-3xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">Browse Jobs</h1>

      {jobs.length === 0 && (
        <p className="text-muted-foreground text-sm">No jobs available.</p>
      )}

      <div className="space-y-4">
        {jobs.map(job => {
          const interested = appliedJobIds.has(job.jobId);
          return (
            <div
              key={job.jobId}
              className="border border-border rounded-lg p-5 bg-card"
            >
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <Briefcase size={16} className="text-muted-foreground shrink-0" />
                    <h2 className="font-medium text-foreground truncate">{job.jobPosition}</h2>
                  </div>
                  <p className="text-sm text-muted-foreground mb-2">
                    {companyMap[job.companyId] ?? 'Unknown Company'}
                    {job.internshipType && <span> · {job.internshipType}</span>}
                    {job.salary != null && <span> · ${job.salary.toLocaleString()}</span>}
                  </p>
                  {job.description && (
                    <p className="text-sm text-foreground line-clamp-2">{job.description}</p>
                  )}
                </div>
                <button
                  onClick={() => applyMutation.mutate(job.jobId)}
                  disabled={interested || applyMutation.isPending}
                  className={`shrink-0 px-4 py-2 rounded-md text-sm font-medium transition-colors ${
                    interested
                      ? 'bg-green-100 text-green-700 border border-green-300 cursor-default'
                      : 'bg-primary text-primary-foreground hover:opacity-90 disabled:opacity-50'
                  }`}
                >
                  {interested ? 'Interested' : 'Mark Interest'}
                </button>
              </div>
            </div>
          );
        })}
      </div>

      {applyMutation.isError && (
        <p className="text-destructive text-sm mt-4">Failed to submit interest.</p>
      )}
    </div>
  );
}
