import { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { companyApi } from '@/api/company.api';
import { jobApi } from '@/api/job.api';
import { internshipTypeApi } from '@/api/internshipType.api';
import { studentApi } from '@/api/student.api';
import { internshipApi, type AssignInternshipRequest } from '@/api/internship.api';

export function AssignInternshipPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [studentId, setStudentId] = useState<number | undefined>();
  const [companyId, setCompanyId] = useState<number | undefined>();
  const [jobId, setJobId] = useState<number | undefined>();
  const [internshipId, setInternshipId] = useState<number | undefined>();
  const [internshipType, setInternshipType] = useState<string>('');
  const [internshipStatus, setInternshipStatus] = useState<string>('Hired');

  const { data: students = [] } = useQuery({ queryKey: ['students'], queryFn: studentApi.getAll });
  const { data: companies = [] } = useQuery({ queryKey: ['companies'], queryFn: companyApi.getAll });
  const { data: jobs = [] } = useQuery({
    queryKey: ['jobs', companyId],
    queryFn: () => jobApi.getAll(companyId),
    enabled: companyId != null,
  });
  const { data: internshipTypes = [] } = useQuery({
    queryKey: ['internship-types'],
    queryFn: internshipTypeApi.getAll,
  });

  const mutation = useMutation({
    mutationFn: (data: AssignInternshipRequest) => internshipApi.assign(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['internship-status'] });
      navigate('/admin/internships');
    },
  });

  const handleCompanyChange = (value: string) => {
    setCompanyId(value ? Number(value) : undefined);
    setJobId(undefined);
  };

  const handleInternshipTypeChange = (value: string) => {
    const id = value ? Number(value) : undefined;
    setInternshipId(id);
    if (id) {
      const found = internshipTypes.find(it => it.internshipId === id);
      setInternshipType(found?.internshipType ?? '');
    } else {
      setInternshipType('');
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (studentId == null || companyId == null || jobId == null || internshipId == null || !internshipType || !internshipStatus) {
      return;
    }
    mutation.mutate({ studentId, companyId, jobId, internshipId, internshipType, internshipStatus });
  };

  const selectClass = 'w-full border border-border rounded-md px-3 py-2 bg-background text-foreground text-sm disabled:opacity-50';

  return (
    <div className="max-w-lg">
      <h1 className="text-2xl font-semibold text-foreground mb-6">Assign Internship</h1>

      <form onSubmit={handleSubmit}>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Student</label>
            <select
              className={selectClass}
              value={studentId ?? ''}
              onChange={e => setStudentId(e.target.value ? Number(e.target.value) : undefined)}
              required
            >
              <option value="">Select a student…</option>
              {students.map(s => (
                <option key={s.studentId} value={s.studentId}>
                  {s.fname} {s.lname}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Company</label>
            <select
              className={selectClass}
              value={companyId ?? ''}
              onChange={e => handleCompanyChange(e.target.value)}
              required
            >
              <option value="">Select a company…</option>
              {companies.map(c => (
                <option key={c.companyId} value={c.companyId}>
                  {c.companyName}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Job</label>
            <select
              className={selectClass}
              value={jobId ?? ''}
              onChange={e => setJobId(e.target.value ? Number(e.target.value) : undefined)}
              disabled={companyId == null}
              required
            >
              <option value="">
                {companyId == null ? 'Select a company first…' : 'Select a job…'}
              </option>
              {jobs.map(j => (
                <option key={j.jobId} value={j.jobId}>
                  {j.jobPosition}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Internship Type</label>
            <select
              className={selectClass}
              value={internshipId ?? ''}
              onChange={e => handleInternshipTypeChange(e.target.value)}
              required
            >
              <option value="">Select an internship type…</option>
              {internshipTypes.map(it => (
                <option key={it.internshipId} value={it.internshipId}>
                  {it.internshipType}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-1">Internship Status</label>
            <input
              type="text"
              className={selectClass}
              value={internshipStatus}
              onChange={e => setInternshipStatus(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            disabled={mutation.isPending}
            className="w-full px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm font-medium hover:bg-primary/90 disabled:opacity-50 transition-colors"
          >
            {mutation.isPending ? 'Assigning…' : 'Assign Internship'}
          </button>

          {mutation.isError && (
            <p className="text-destructive text-sm">Failed to assign internship.</p>
          )}
        </div>
      </form>
    </div>
  );
}
