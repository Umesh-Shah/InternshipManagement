import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { studentApi } from '@/api/student.api';
import { ArrowLeft } from 'lucide-react';

function Field({ label, value }: { label: string; value: string | number | null | undefined }) {
  return (
    <div>
      <dt className="text-xs text-muted-foreground uppercase tracking-wide">{label}</dt>
      <dd className="text-sm text-foreground mt-0.5">{value ?? '—'}</dd>
    </div>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="rounded-lg border border-border p-5 space-y-4">
      <h2 className="text-base font-semibold text-foreground">{title}</h2>
      <dl className="grid grid-cols-2 gap-x-6 gap-y-3">{children}</dl>
    </div>
  );
}

export function StudentProfilePage() {
  const { studentId } = useParams<{ studentId: string }>();
  const id = Number(studentId);
  const navigate = useNavigate();

  const { data: info } = useQuery({ queryKey: ['student-info', id], queryFn: () => studentApi.getInfo(id) });
  const { data: edu }  = useQuery({ queryKey: ['student-edu', id],  queryFn: () => studentApi.getEducation(id) });
  const { data: cert } = useQuery({ queryKey: ['student-cert', id], queryFn: () => studentApi.getCertificate(id) });
  const { data: work } = useQuery({ queryKey: ['student-work', id], queryFn: () => studentApi.getWork(id) });
  const { data: skills = [] } = useQuery({ queryKey: ['student-skills', id], queryFn: () => studentApi.getSkills(id) });

  if (!info) return <p className="text-muted-foreground text-sm">Loading…</p>;

  return (
    <div className="max-w-3xl space-y-5">
      <div className="flex items-center gap-3">
        <button onClick={() => navigate('/admin/students')} className="text-muted-foreground hover:text-foreground">
          <ArrowLeft size={18} />
        </button>
        <h1 className="text-2xl font-semibold text-foreground">
          {info.fname} {info.lname}
          <span className="ml-2 text-base font-normal text-muted-foreground">#{info.studentId}</span>
        </h1>
      </div>

      <Section title="Personal Info">
        <Field label="Student ID" value={info.studentId} />
        <Field label="Year" value={info.year} />
        <Field label="Email" value={info.stuEmail} />
        <Field label="Telephone" value={info.stuTelephone} />
        <Field label="Gender" value={info.gender} />
        <Field label="Canada Status" value={info.canadaStatus} />
        <Field label="Semester" value={info.semester} />
        <Field label="Country" value={info.country} />
        <Field label="Internship Status" value={info.internshipStatus} />
        <Field label="Student Status" value={info.studentStatus} />
      </Section>

      <Section title="Education">
        <Field label="Degree Type" value={edu?.degreeType} />
        <Field label="Major" value={edu?.major} />
        <Field label="GPA" value={edu?.degreeGpa} />
        <Field label="University" value={edu?.university} />
        <Field label="Location" value={edu?.universityLocation} />
      </Section>

      <Section title="Certificate">
        <Field label="Title" value={cert?.certificateTitle} />
        <Field label="Body" value={cert?.certificateBody} />
      </Section>

      <Section title="Work Experience">
        <Field label="Company" value={work?.company} />
        <Field label="Position" value={work?.position} />
        <Field label="Location" value={work?.companyLocation} />
        <Field label="Start Date" value={work?.startDate} />
        <Field label="End Date" value={work?.endDate} />
      </Section>

      <div className="rounded-lg border border-border p-5">
        <h2 className="text-base font-semibold text-foreground mb-3">Skills</h2>
        {skills.length === 0 ? (
          <p className="text-sm text-muted-foreground">No skills on file.</p>
        ) : (
          <div className="flex flex-wrap gap-2">
            {skills.map(s => (
              <span
                key={s.studentSkillId}
                className="px-2 py-0.5 rounded-full text-xs bg-primary/10 text-primary border border-primary/20"
              >
                {s.skillName ?? `Skill #${s.skillId}`}
              </span>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
