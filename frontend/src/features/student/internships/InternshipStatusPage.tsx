import { useQuery } from '@tanstack/react-query';
import useAuthStore from '@/features/auth/useAuthStore';
import { internshipApi } from '@/api/internship.api';

export function InternshipStatusPage() {
  const { user } = useAuthStore();
  const studentId = user?.studentId ?? null;

  const { data: assignments = [], isLoading } = useQuery({
    queryKey: ['internship-status-student', studentId],
    queryFn: () => internshipApi.getByStudent(studentId!),
    enabled: studentId != null,
  });

  return (
    <div className="max-w-2xl">
      <h1 className="text-2xl font-semibold text-foreground mb-6">My Internship Status</h1>

      {isLoading && <p className="text-muted-foreground text-sm">Loading…</p>}

      {!isLoading && assignments.length === 0 && (
        <p className="text-muted-foreground text-sm">No internship assigned yet.</p>
      )}

      {assignments.length > 0 && (
        <div className="space-y-4">
          {assignments.map(a => (
            <div
              key={a.studentInternshipId}
              className="border border-border rounded-lg p-5 bg-card space-y-3"
            >
              <div className="grid grid-cols-2 gap-3 text-sm">
                <div>
                  <span className="text-muted-foreground font-medium">Company</span>
                  <p className="text-foreground mt-0.5">{a.companyName ?? '—'}</p>
                </div>
                <div>
                  <span className="text-muted-foreground font-medium">Job</span>
                  <p className="text-foreground mt-0.5">{a.jobPosition ?? '—'}</p>
                </div>
                <div>
                  <span className="text-muted-foreground font-medium">Internship Type</span>
                  <p className="text-foreground mt-0.5">{a.internshipType}</p>
                </div>
                <div>
                  <span className="text-muted-foreground font-medium">Internship Status</span>
                  <p className="text-foreground mt-0.5">{a.internshipStatus ?? '—'}</p>
                </div>
                <div>
                  <span className="text-muted-foreground font-medium">Student Status</span>
                  <p className="text-foreground mt-0.5">{a.studentStatus ?? '—'}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
