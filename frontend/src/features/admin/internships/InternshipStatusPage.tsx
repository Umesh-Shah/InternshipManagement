import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { internshipApi } from '@/api/internship.api';

export function InternshipStatusPage() {
  const { data: assignments = [], isLoading } = useQuery({
    queryKey: ['internship-status'],
    queryFn: internshipApi.getAll,
  });

  return (
    <div className="max-w-5xl">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-foreground">Internship Status</h1>
        <Link
          to="/admin/internships/assign"
          className="px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm font-medium hover:bg-primary/90 transition-colors"
        >
          Assign Internship
        </Link>
      </div>

      {isLoading && <p className="text-muted-foreground text-sm">Loading…</p>}

      {!isLoading && assignments.length === 0 && (
        <p className="text-muted-foreground text-sm">No internship assignments yet.</p>
      )}

      {assignments.length > 0 && (
        <div className="border border-border rounded-lg overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-muted text-muted-foreground">
              <tr>
                <th className="text-left px-4 py-3 font-medium">Student</th>
                <th className="text-left px-4 py-3 font-medium">Company</th>
                <th className="text-left px-4 py-3 font-medium">Job</th>
                <th className="text-left px-4 py-3 font-medium">Type</th>
                <th className="text-left px-4 py-3 font-medium">Status</th>
                <th className="text-left px-4 py-3 font-medium">Student Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border bg-card">
              {assignments.map(a => (
                <tr key={a.studentInternshipId}>
                  <td className="px-4 py-3 text-foreground">{a.studentName || `Student #${a.studentId}`}</td>
                  <td className="px-4 py-3 text-foreground">{a.companyName ?? '—'}</td>
                  <td className="px-4 py-3 text-foreground">{a.jobPosition ?? '—'}</td>
                  <td className="px-4 py-3 text-muted-foreground">{a.internshipType}</td>
                  <td className="px-4 py-3 text-muted-foreground">{a.internshipStatus ?? '—'}</td>
                  <td className="px-4 py-3 text-muted-foreground">{a.studentStatus ?? '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
