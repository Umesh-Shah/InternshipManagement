import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import type { ColumnDef } from '@tanstack/react-table';
import { Plus, Pencil, Trash2 } from 'lucide-react';
import { jobApi } from '@/api/job.api';
import type { Job } from '@/api/job.api';
import { companyApi } from '@/api/company.api';
import { DataTable } from '@/components/DataTable';

export function JobsPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data: jobs = [], isLoading } = useQuery({
    queryKey: ['jobs'],
    queryFn: () => jobApi.getAll(),
  });
  const { data: companies = [] } = useQuery({
    queryKey: ['companies'],
    queryFn: companyApi.getAll,
  });

  const companyMap = Object.fromEntries(companies.map(c => [c.companyId, c.companyName]));

  const deleteMutation = useMutation({
    mutationFn: jobApi.delete,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['jobs'] }),
  });

  const columns: ColumnDef<Job, unknown>[] = [
    { accessorKey: 'jobPosition', header: 'Position' },
    {
      accessorKey: 'companyId',
      header: 'Company',
      cell: ({ row }) => companyMap[row.original.companyId] ?? row.original.companyId,
    },
    { accessorKey: 'salary', header: 'Salary' },
    { accessorKey: 'internshipType', header: 'Type' },
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => (
        <div className="flex items-center gap-2">
          <button
            onClick={() => navigate(`/admin/jobs/${row.original.jobId}/edit`)}
            className="p-1 rounded hover:bg-muted transition-colors text-muted-foreground hover:text-foreground"
          >
            <Pencil size={14} />
          </button>
          <button
            onClick={() => {
              if (confirm('Delete this job?')) deleteMutation.mutate(row.original.jobId);
            }}
            className="p-1 rounded hover:bg-destructive/10 transition-colors text-muted-foreground hover:text-destructive"
          >
            <Trash2 size={14} />
          </button>
        </div>
      ),
    },
  ];

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-foreground">Jobs</h1>
        <button
          onClick={() => navigate('/admin/jobs/new')}
          className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity"
        >
          <Plus size={16} />
          Add Job
        </button>
      </div>
      <DataTable columns={columns} data={jobs} isLoading={isLoading} emptyMessage="No jobs found." />
    </div>
  );
}
