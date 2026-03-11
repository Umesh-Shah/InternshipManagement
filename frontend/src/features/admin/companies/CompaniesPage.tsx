import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import type { ColumnDef } from '@tanstack/react-table';
import { Plus, Pencil, Trash2 } from 'lucide-react';
import { companyApi } from '@/api/company.api';
import type { Company } from '@/api/company.api';
import { DataTable } from '@/components/DataTable';

export function CompaniesPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data = [], isLoading } = useQuery({
    queryKey: ['companies'],
    queryFn: companyApi.getAll,
  });

  const deleteMutation = useMutation({
    mutationFn: companyApi.delete,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['companies'] }),
  });

  const columns: ColumnDef<Company, unknown>[] = [
    { accessorKey: 'companyName', header: 'Company Name' },
    { accessorKey: 'city', header: 'City' },
    { accessorKey: 'country', header: 'Country' },
    { accessorKey: 'telephone', header: 'Phone' },
    { accessorKey: 'email', header: 'Email' },
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => (
        <div className="flex items-center gap-2">
          <button
            onClick={() => navigate(`/admin/companies/${row.original.companyId}/edit`)}
            className="p-1 rounded hover:bg-muted transition-colors text-muted-foreground hover:text-foreground"
          >
            <Pencil size={14} />
          </button>
          <button
            onClick={() => {
              if (confirm('Delete this company?')) deleteMutation.mutate(row.original.companyId);
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
        <h1 className="text-2xl font-semibold text-foreground">Companies</h1>
        <button
          onClick={() => navigate('/admin/companies/new')}
          className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity"
        >
          <Plus size={16} />
          Add Company
        </button>
      </div>
      <DataTable columns={columns} data={data} isLoading={isLoading} emptyMessage="No companies found." />
    </div>
  );
}
