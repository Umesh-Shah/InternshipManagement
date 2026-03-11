import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import type { ColumnDef } from '@tanstack/react-table';
import { Plus, Pencil, Trash2 } from 'lucide-react';
import { internshipTypeApi } from '@/api/internshipType.api';
import type { InternshipType } from '@/api/internshipType.api';
import { DataTable } from '@/components/DataTable';

export function InternshipTypesPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data = [], isLoading } = useQuery({
    queryKey: ['internshipTypes'],
    queryFn: internshipTypeApi.getAll,
  });

  const deleteMutation = useMutation({
    mutationFn: internshipTypeApi.delete,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['internshipTypes'] }),
  });

  const columns: ColumnDef<InternshipType, unknown>[] = [
    { accessorKey: 'internshipName', header: 'Name' },
    { accessorKey: 'internshipType', header: 'Type Code' },
    { accessorKey: 'description', header: 'Description' },
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => (
        <div className="flex items-center gap-2">
          <button
            onClick={() => navigate(`/admin/internship-types/${row.original.internshipId}/edit`)}
            className="p-1 rounded hover:bg-muted transition-colors text-muted-foreground hover:text-foreground"
          >
            <Pencil size={14} />
          </button>
          <button
            onClick={() => {
              if (confirm('Delete this internship type?'))
                deleteMutation.mutate(row.original.internshipId);
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
        <h1 className="text-2xl font-semibold text-foreground">Internship Types</h1>
        <button
          onClick={() => navigate('/admin/internship-types/new')}
          className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity"
        >
          <Plus size={16} />
          Add Type
        </button>
      </div>
      <DataTable
        columns={columns}
        data={data}
        isLoading={isLoading}
        emptyMessage="No internship types found."
      />
    </div>
  );
}
