import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import type { ColumnDef } from '@tanstack/react-table';
import { Plus, Pencil, Trash2 } from 'lucide-react';
import { skillApi } from '@/api/skill.api';
import type { Skill } from '@/api/skill.api';
import { DataTable } from '@/components/DataTable';

export function SkillsPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data = [], isLoading } = useQuery({
    queryKey: ['skills'],
    queryFn: skillApi.getAll,
  });

  const deleteMutation = useMutation({
    mutationFn: skillApi.delete,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['skills'] }),
  });

  const columns: ColumnDef<Skill, unknown>[] = [
    { accessorKey: 'skillName', header: 'Skill Name' },
    { accessorKey: 'skillType', header: 'Type' },
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => (
        <div className="flex items-center gap-2">
          <button
            onClick={() => navigate(`/admin/skills/${row.original.skillId}/edit`)}
            className="p-1 rounded hover:bg-muted transition-colors text-muted-foreground hover:text-foreground"
          >
            <Pencil size={14} />
          </button>
          <button
            onClick={() => {
              if (confirm('Delete this skill?')) deleteMutation.mutate(row.original.skillId);
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
        <h1 className="text-2xl font-semibold text-foreground">Skills</h1>
        <button
          onClick={() => navigate('/admin/skills/new')}
          className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity"
        >
          <Plus size={16} />
          Add Skill
        </button>
      </div>
      <DataTable columns={columns} data={data} isLoading={isLoading} emptyMessage="No skills found." />
    </div>
  );
}
