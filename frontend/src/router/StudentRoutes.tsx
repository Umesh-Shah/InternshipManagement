import { Route, Routes, Navigate } from 'react-router-dom';
import { StudentShell } from '@/features/student/layout/StudentShell';
import { ProfilePage } from '@/features/student/profile/ProfilePage';
import { EducationPage } from '@/features/student/profile/EducationPage';
import { CertificatesPage } from '@/features/student/profile/CertificatesPage';
import { WorkPage } from '@/features/student/profile/WorkPage';
import { SkillsPage } from '@/features/student/profile/SkillsPage';
import { JobsPage } from '@/features/student/jobs/JobsPage';
import { MyApplicationsPage } from '@/features/student/jobs/MyApplicationsPage';
import { InternshipStatusPage as StudentInternshipStatusPage } from '@/features/student/internships/InternshipStatusPage';

export function StudentRoutes() {
  return (
    <Routes>
      <Route element={<StudentShell />}>
        <Route index element={<Navigate to="profile" replace />} />
        <Route path="profile" element={<ProfilePage />} />
        <Route path="education" element={<EducationPage />} />
        <Route path="certificates" element={<CertificatesPage />} />
        <Route path="work" element={<WorkPage />} />
        <Route path="skills" element={<SkillsPage />} />
        <Route path="jobs" element={<JobsPage />} />
        <Route path="my-applications" element={<MyApplicationsPage />} />
        <Route path="internship-status" element={<StudentInternshipStatusPage />} />
      </Route>
    </Routes>
  );
}
