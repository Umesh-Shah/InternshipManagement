import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import type { ColumnDef } from '@tanstack/react-table';
import { Plus } from 'lucide-react';
import { studentApi } from '@/api/student.api';
import type { StudentInfo } from '@/api/student.api';
import { DataTable } from '@/components/DataTable';

export function StudentsPage() {
  const navigate = useNavigate();

  const { data = [], isLoading } = useQuery({
    queryKey: ['students'],
    queryFn: studentApi.getAll,
  });

  const columns: ColumnDef<StudentInfo, unknown>[] = [
    { accessorKey: 'studentId', header: 'Student ID' },
    {
      id: 'name',
      header: 'Name',
      cell: ({ row }) => {
        const { fname, mname, lname } = row.original;
        return [fname, mname, lname].filter(Boolean).join(' ');
      },
    },
    { accessorKey: 'stuEmail', header: 'Email' },
    { accessorKey: 'semester', header: 'Semester' },
    { accessorKey: 'internshipStatus', header: 'Internship Status' },
    { accessorKey: 'studentStatus', header: 'Status' },
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => (
        <button
          onClick={() => navigate(`/admin/students/${row.original.studentId}/profile`)}
          className="text-sm text-primary underline hover:no-underline"
        >
          View Profile
        </button>
      ),
    },
  ];

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-foreground">Students</h1>
        <button
          onClick={() => navigate('/admin/students/new')}
          className="flex items-center gap-2 px-4 py-2 bg-primary text-primary-foreground rounded-md text-sm hover:opacity-90 transition-opacity"
        >
          <Plus size={16} />
          Add Student
        </button>
      </div>
      <DataTable columns={columns} data={data} isLoading={isLoading} emptyMessage="No students found." />
    </div>
  );
}
