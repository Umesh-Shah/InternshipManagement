import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import type { ColumnDef } from '@tanstack/react-table';
import { DataTable } from '@/components/DataTable';

interface Row {
  id: number;
  name: string;
}

const columns: ColumnDef<Row, unknown>[] = [
  { accessorKey: 'id', header: 'ID' },
  { accessorKey: 'name', header: 'Name' },
];

const rows: Row[] = [
  { id: 1, name: 'Alice' },
  { id: 2, name: 'Bob' },
];

describe('DataTable', () => {
  it('renders column headers', () => {
    render(<DataTable columns={columns} data={rows} />);
    expect(screen.getByText('ID')).toBeInTheDocument();
    expect(screen.getByText('Name')).toBeInTheDocument();
  });

  it('renders row data', () => {
    render(<DataTable columns={columns} data={rows} />);
    expect(screen.getByText('Alice')).toBeInTheDocument();
    expect(screen.getByText('Bob')).toBeInTheDocument();
  });

  it('shows default empty message when data is empty', () => {
    render(<DataTable columns={columns} data={[]} />);
    expect(screen.getByText('No data.')).toBeInTheDocument();
  });

  it('shows custom empty message', () => {
    render(<DataTable columns={columns} data={[]} emptyMessage="Nothing here." />);
    expect(screen.getByText('Nothing here.')).toBeInTheDocument();
  });

  it('shows loading indicator when isLoading is true', () => {
    render(<DataTable columns={columns} data={[]} isLoading />);
    expect(screen.getByText('Loading…')).toBeInTheDocument();
  });

  it('does not show row data while loading', () => {
    render(<DataTable columns={columns} data={rows} isLoading />);
    expect(screen.queryByText('Alice')).not.toBeInTheDocument();
  });
});
