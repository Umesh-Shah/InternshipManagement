import { Route, Routes } from 'react-router-dom';
import { AdminShell } from '@/features/admin/layout/AdminShell';
import { CompaniesPage } from '@/features/admin/companies/CompaniesPage';
import { CompanyFormPage } from '@/features/admin/companies/CompanyFormPage';
import { JobsPage } from '@/features/admin/jobs/JobsPage';
import { JobFormPage } from '@/features/admin/jobs/JobFormPage';
import { SkillsPage } from '@/features/admin/skills/SkillsPage';
import { SkillFormPage } from '@/features/admin/skills/SkillFormPage';
import { InternshipTypesPage } from '@/features/admin/internship-types/InternshipTypesPage';
import { InternshipTypeFormPage } from '@/features/admin/internship-types/InternshipTypeFormPage';
import { StudentsPage } from '@/features/admin/students/StudentsPage';
import { StudentFormPage } from '@/features/admin/students/StudentFormPage';
import { StudentProfilePage } from '@/features/admin/students/StudentProfilePage';
import { ApprovalsPage } from '@/features/admin/approvals/ApprovalsPage';
import { InternshipStatusPage } from '@/features/admin/internships/InternshipStatusPage';
import { AssignInternshipPage } from '@/features/admin/internships/AssignInternshipPage';
import { ReportsPage } from '@/features/admin/reports/ReportsPage';

export function AdminRoutes() {
  return (
    <Routes>
      <Route element={<AdminShell />}>
        <Route index element={<CompaniesPage />} />
        <Route path="companies" element={<CompaniesPage />} />
        <Route path="companies/new" element={<CompanyFormPage />} />
        <Route path="companies/:id/edit" element={<CompanyFormPage />} />
        <Route path="jobs" element={<JobsPage />} />
        <Route path="jobs/new" element={<JobFormPage />} />
        <Route path="jobs/:id/edit" element={<JobFormPage />} />
        <Route path="skills" element={<SkillsPage />} />
        <Route path="skills/new" element={<SkillFormPage />} />
        <Route path="skills/:id/edit" element={<SkillFormPage />} />
        <Route path="internship-types" element={<InternshipTypesPage />} />
        <Route path="internship-types/new" element={<InternshipTypeFormPage />} />
        <Route path="internship-types/:id/edit" element={<InternshipTypeFormPage />} />
        <Route path="students" element={<StudentsPage />} />
        <Route path="students/new" element={<StudentFormPage />} />
        <Route path="students/:studentId/profile" element={<StudentProfilePage />} />
        <Route path="approvals" element={<ApprovalsPage />} />
        <Route path="internships" element={<InternshipStatusPage />} />
        <Route path="internships/assign" element={<AssignInternshipPage />} />
        <Route path="reports" element={<ReportsPage />} />
      </Route>
    </Routes>
  );
}
