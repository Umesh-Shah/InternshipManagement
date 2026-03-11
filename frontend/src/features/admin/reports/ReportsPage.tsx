import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { reportsApi } from '@/api/reports.api';
import type {
  StudentReportParams,
  GpaReportParams,
} from '@/api/reports.api';
import { usePdfDownload } from '@/hooks/usePdfDownload';
import { Button } from '@/components/ui/button';

type Tab = 'students' | 'companies' | 'internship-types' | 'gpa' | 'jobs';

const TABS: { id: Tab; label: string }[] = [
  { id: 'students', label: 'Students' },
  { id: 'companies', label: 'Companies' },
  { id: 'internship-types', label: 'Internship Types' },
  { id: 'gpa', label: 'GPA' },
  { id: 'jobs', label: 'Jobs' },
];

export function ReportsPage() {
  const [activeTab, setActiveTab] = useState<Tab>('students');
  const { download, isDownloading } = usePdfDownload();

  const { data: filters } = useQuery({
    queryKey: ['report-filters'],
    queryFn: reportsApi.getFilters,
  });

  return (
    <div className="p-6 space-y-4">
      <h1 className="text-2xl font-semibold">Reports</h1>

      {/* Tab bar */}
      <div className="flex gap-1 border-b border-border">
        {TABS.map(tab => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            className={`px-4 py-2 text-sm font-medium transition-colors border-b-2 -mb-px ${
              activeTab === tab.id
                ? 'border-primary text-primary'
                : 'border-transparent text-muted-foreground hover:text-foreground'
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Panels */}
      {activeTab === 'students' && (
        <StudentsPanel filters={filters ?? null} download={download} isDownloading={isDownloading} />
      )}
      {activeTab === 'companies' && (
        <CompaniesPanel download={download} isDownloading={isDownloading} />
      )}
      {activeTab === 'internship-types' && (
        <InternshipTypesPanel filters={filters ?? null} download={download} isDownloading={isDownloading} />
      )}
      {activeTab === 'gpa' && (
        <GpaPanel filters={filters ?? null} download={download} isDownloading={isDownloading} />
      )}
      {activeTab === 'jobs' && (
        <JobsPanel download={download} isDownloading={isDownloading} />
      )}
    </div>
  );
}

// ── Shared helpers ──────────────────────────────────────────────────────────

interface DownloadProps {
  download: ReturnType<typeof usePdfDownload>['download'];
  isDownloading: boolean;
}

function Select({ label, value, onChange, options }: {
  label: string;
  value: string;
  onChange: (v: string) => void;
  options: (string | number)[];
}) {
  return (
    <label className="flex flex-col gap-1 text-sm">
      <span className="text-muted-foreground">{label}</span>
      <select
        value={value}
        onChange={e => onChange(e.target.value)}
        className="border border-border rounded px-2 py-1.5 bg-background text-sm"
      >
        <option value="">All</option>
        {options.map(o => (
          <option key={o} value={o}>{o}</option>
        ))}
      </select>
    </label>
  );
}

function TableWrapper({ children }: { children: React.ReactNode }) {
  return (
    <div className="overflow-x-auto rounded border border-border">
      <table className="w-full text-sm">{children}</table>
    </div>
  );
}

function Th({ children }: { children: React.ReactNode }) {
  return (
    <th className="bg-muted px-3 py-2 text-left font-medium text-muted-foreground whitespace-nowrap">
      {children}
    </th>
  );
}

function Td({ children }: { children: React.ReactNode }) {
  return <td className="px-3 py-2 border-t border-border">{children ?? '—'}</td>;
}

function EmptyRow({ cols }: { cols: number }) {
  return (
    <tr>
      <td colSpan={cols} className="px-3 py-6 text-center text-muted-foreground">
        No results found.
      </td>
    </tr>
  );
}

// ── Students Panel ──────────────────────────────────────────────────────────

function StudentsPanel({ filters, ...dl }: { filters: import('@/api/reports.api').ReportFilters | null } & DownloadProps) {
  const [params, setParams] = useState<StudentReportParams>({});

  const { data = [], isFetching } = useQuery({
    queryKey: ['report-students', params],
    queryFn: () => reportsApi.getStudents(params),
  });

  function set(key: keyof StudentReportParams, value: string) {
    setParams(prev => ({ ...prev, [key]: value || undefined }));
  }

  return (
    <div className="space-y-4">
      <div className="flex flex-wrap gap-4 items-end">
        <Select label="Year" value={String(params.year ?? '')} onChange={v => setParams(p => ({ ...p, year: v ? Number(v) : undefined }))} options={(filters as any)?.years ?? []} />
        <Select label="Country" value={params.country ?? ''} onChange={v => set('country', v)} options={(filters as any)?.countries ?? []} />
        <Select label="Semester" value={params.semester ?? ''} onChange={v => set('semester', v)} options={['Fall', 'Winter', 'Summer']} />
        <Select label="Internship Status" value={params.internshipStatus ?? ''} onChange={v => set('internshipStatus', v)} options={['Yes', 'No', 'Pending', 'Completed']} />
        <Select label="Student Status" value={params.studentStatus ?? ''} onChange={v => set('studentStatus', v)} options={['Active', 'Inactive', 'Graduated']} />
        <Button
          variant="outline"
          size="sm"
          disabled={dl.isDownloading}
          onClick={() => dl.download(() => reportsApi.downloadStudentsPdf(params), 'students-report.pdf')}
        >
          {dl.isDownloading ? 'Downloading…' : 'Download PDF'}
        </Button>
      </div>

      {isFetching ? (
        <p className="text-muted-foreground text-sm">Loading…</p>
      ) : (
        <TableWrapper>
          <thead>
            <tr>
              <Th>ID</Th><Th>First Name</Th><Th>Last Name</Th>
              <Th>Year</Th><Th>Country</Th><Th>Semester</Th>
              <Th>Internship Status</Th><Th>Student Status</Th>
            </tr>
          </thead>
          <tbody>
            {data.length === 0 ? <EmptyRow cols={8} /> : data.map(r => (
              <tr key={r.studentId} className="hover:bg-muted/40">
                <Td>{r.studentId}</Td><Td>{r.fname}</Td><Td>{r.lname}</Td>
                <Td>{r.year}</Td><Td>{r.country}</Td><Td>{r.semester}</Td>
                <Td>{r.internshipStatus}</Td><Td>{r.studentStatus}</Td>
              </tr>
            ))}
          </tbody>
        </TableWrapper>
      )}
    </div>
  );
}

// ── Companies Panel ─────────────────────────────────────────────────────────

function CompaniesPanel({ ...dl }: DownloadProps) {
  const [city, setCity] = useState('');

  const { data = [], isFetching } = useQuery({
    queryKey: ['report-companies', city],
    queryFn: () => reportsApi.getCompanies(city || undefined),
  });

  return (
    <div className="space-y-4">
      <div className="flex flex-wrap gap-4 items-end">
        <label className="flex flex-col gap-1 text-sm">
          <span className="text-muted-foreground">City</span>
          <input
            value={city}
            onChange={e => setCity(e.target.value)}
            placeholder="Filter by city…"
            className="border border-border rounded px-2 py-1.5 bg-background text-sm w-48"
          />
        </label>
        <Button
          variant="outline"
          size="sm"
          disabled={dl.isDownloading}
          onClick={() => dl.download(() => reportsApi.downloadCompaniesPdf(city || undefined), 'companies-report.pdf')}
        >
          {dl.isDownloading ? 'Downloading…' : 'Download PDF'}
        </Button>
      </div>

      {isFetching ? (
        <p className="text-muted-foreground text-sm">Loading…</p>
      ) : (
        <TableWrapper>
          <thead>
            <tr>
              <Th>ID</Th><Th>Company Name</Th><Th>City</Th>
              <Th>Country</Th><Th>Email</Th><Th>Telephone</Th>
            </tr>
          </thead>
          <tbody>
            {data.length === 0 ? <EmptyRow cols={6} /> : data.map(r => (
              <tr key={r.companyId} className="hover:bg-muted/40">
                <Td>{r.companyId}</Td><Td>{r.companyName}</Td><Td>{r.city}</Td>
                <Td>{r.country}</Td><Td>{r.email}</Td><Td>{r.telephone}</Td>
              </tr>
            ))}
          </tbody>
        </TableWrapper>
      )}
    </div>
  );
}

// ── Internship Types Panel ──────────────────────────────────────────────────

function InternshipTypesPanel({ filters, ...dl }: { filters: any } & DownloadProps) {
  const [internshipType, setInternshipType] = useState('');

  const { data = [], isFetching } = useQuery({
    queryKey: ['report-internship-types', internshipType],
    queryFn: () => reportsApi.getInternshipTypes(internshipType || undefined),
  });

  return (
    <div className="space-y-4">
      <div className="flex flex-wrap gap-4 items-end">
        <Select
          label="Internship Type"
          value={internshipType}
          onChange={setInternshipType}
          options={filters?.internshipTypes ?? []}
        />
        <Button
          variant="outline"
          size="sm"
          disabled={dl.isDownloading}
          onClick={() => dl.download(
            () => reportsApi.downloadInternshipTypesPdf(internshipType || undefined),
            'internship-types-report.pdf'
          )}
        >
          {dl.isDownloading ? 'Downloading…' : 'Download PDF'}
        </Button>
      </div>

      {isFetching ? (
        <p className="text-muted-foreground text-sm">Loading…</p>
      ) : (
        <TableWrapper>
          <thead>
            <tr><Th>Internship Type</Th><Th>Student Count</Th></tr>
          </thead>
          <tbody>
            {data.length === 0 ? <EmptyRow cols={2} /> : data.map(r => (
              <tr key={r.internshipType} className="hover:bg-muted/40">
                <Td>{r.internshipType}</Td><Td>{r.studentCount}</Td>
              </tr>
            ))}
          </tbody>
        </TableWrapper>
      )}
    </div>
  );
}

// ── GPA Panel ───────────────────────────────────────────────────────────────

function GpaPanel({ filters, ...dl }: { filters: any } & DownloadProps) {
  const [params, setParams] = useState<GpaReportParams>({});

  const { data = [], isFetching } = useQuery({
    queryKey: ['report-gpa', params],
    queryFn: () => reportsApi.getGpa(params),
  });

  function set(key: keyof GpaReportParams, value: string) {
    setParams(prev => ({ ...prev, [key]: value || undefined }));
  }

  return (
    <div className="space-y-4">
      <div className="flex flex-wrap gap-4 items-end">
        <Select label="Year" value={String(params.year ?? '')} onChange={v => setParams(p => ({ ...p, year: v ? Number(v) : undefined }))} options={filters?.years ?? []} />
        <Select label="Country" value={params.country ?? ''} onChange={v => set('country', v)} options={filters?.countries ?? []} />
        <Select label="University" value={params.university ?? ''} onChange={v => set('university', v)} options={filters?.universities ?? []} />
        <Select label="Location" value={params.universityLocation ?? ''} onChange={v => set('universityLocation', v)} options={filters?.universityLocations ?? []} />
        <Select label="Degree Type" value={params.degreeType ?? ''} onChange={v => set('degreeType', v)} options={['Bachelor', 'Master', 'PhD', 'Diploma']} />
        <Button
          variant="outline"
          size="sm"
          disabled={dl.isDownloading}
          onClick={() => dl.download(() => reportsApi.downloadGpaPdf(params), 'gpa-report.pdf')}
        >
          {dl.isDownloading ? 'Downloading…' : 'Download PDF'}
        </Button>
      </div>

      {isFetching ? (
        <p className="text-muted-foreground text-sm">Loading…</p>
      ) : (
        <TableWrapper>
          <thead>
            <tr>
              <Th>ID</Th><Th>First Name</Th><Th>Last Name</Th>
              <Th>University</Th><Th>Location</Th><Th>Degree Type</Th><Th>GPA</Th>
            </tr>
          </thead>
          <tbody>
            {data.length === 0 ? <EmptyRow cols={7} /> : data.map((r, i) => (
              <tr key={`${r.studentId}-${i}`} className="hover:bg-muted/40">
                <Td>{r.studentId}</Td><Td>{r.fname}</Td><Td>{r.lname}</Td>
                <Td>{r.university}</Td><Td>{r.universityLocation}</Td>
                <Td>{r.degreeType}</Td><Td>{r.degreeGpa}</Td>
              </tr>
            ))}
          </tbody>
        </TableWrapper>
      )}
    </div>
  );
}

// ── Jobs Panel ──────────────────────────────────────────────────────────────

function JobsPanel({ ...dl }: DownloadProps) {
  const { data = [], isFetching } = useQuery({
    queryKey: ['report-jobs'],
    queryFn: () => reportsApi.getJobs(),
  });

  return (
    <div className="space-y-4">
      <div className="flex gap-4 items-end">
        <Button
          variant="outline"
          size="sm"
          disabled={dl.isDownloading}
          onClick={() => dl.download(() => reportsApi.downloadJobsPdf(), 'jobs-report.pdf')}
        >
          {dl.isDownloading ? 'Downloading…' : 'Download PDF'}
        </Button>
      </div>

      {isFetching ? (
        <p className="text-muted-foreground text-sm">Loading…</p>
      ) : (
        <TableWrapper>
          <thead>
            <tr>
              <Th>ID</Th><Th>Job Position</Th><Th>Company</Th><Th>Applicants</Th>
            </tr>
          </thead>
          <tbody>
            {data.length === 0 ? <EmptyRow cols={4} /> : data.map(r => (
              <tr key={r.jobId} className="hover:bg-muted/40">
                <Td>{r.jobId}</Td><Td>{r.jobPosition}</Td>
                <Td>{r.companyName}</Td><Td>{r.applicantCount}</Td>
              </tr>
            ))}
          </tbody>
        </TableWrapper>
      )}
    </div>
  );
}
