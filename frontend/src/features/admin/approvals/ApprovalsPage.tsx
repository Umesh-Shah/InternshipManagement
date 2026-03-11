import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { jobApplicationApi } from '@/api/jobApplication.api';
import { CheckCircle } from 'lucide-react';

export function ApprovalsPage() {
  const queryClient = useQueryClient();

  const { data: pending = [], isLoading } = useQuery({
    queryKey: ['pending-applications'],
    queryFn: jobApplicationApi.getPending,
  });

  const approveMutation = useMutation({
    mutationFn: (id: number) => jobApplicationApi.approve(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['pending-applications'] });
    },
  });

  return (
    <div className="max-w-3xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">Job Application Approvals</h1>

      {isLoading && <p className="text-muted-foreground text-sm">Loading…</p>}

      {!isLoading && pending.length === 0 && (
        <div className="flex items-center gap-2 text-muted-foreground text-sm">
          <CheckCircle size={16} className="text-green-600" />
          <span>No pending applications.</span>
        </div>
      )}

      {pending.length > 0 && (
        <div className="border border-border rounded-lg overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-muted text-muted-foreground">
              <tr>
                <th className="text-left px-4 py-3 font-medium">Student</th>
                <th className="text-left px-4 py-3 font-medium">Job</th>
                <th className="text-left px-4 py-3 font-medium">Company</th>
                <th className="px-4 py-3" />
              </tr>
            </thead>
            <tbody className="divide-y divide-border bg-card">
              {pending.map(app => (
                <tr key={app.studentJobId}>
                  <td className="px-4 py-3 text-foreground">{app.studentName}</td>
                  <td className="px-4 py-3 text-foreground">{app.jobPosition ?? `Job #${app.jobId}`}</td>
                  <td className="px-4 py-3 text-muted-foreground">{app.companyName ?? '—'}</td>
                  <td className="px-4 py-3 text-right">
                    <button
                      onClick={() => approveMutation.mutate(app.studentJobId)}
                      disabled={approveMutation.isPending}
                      className="px-3 py-1.5 bg-green-600 text-white rounded-md text-xs font-medium hover:bg-green-700 disabled:opacity-50 transition-colors"
                    >
                      Approve
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {approveMutation.isError && (
        <p className="text-destructive text-sm mt-3">Failed to approve application.</p>
      )}
    </div>
  );
}
