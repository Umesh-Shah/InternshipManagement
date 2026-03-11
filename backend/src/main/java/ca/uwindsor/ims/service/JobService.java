package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.JobRequest;
import ca.uwindsor.ims.entity.Job;
import ca.uwindsor.ims.repository.JobRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private final JobRepository repo;
    public JobService(JobRepository repo) { this.repo = repo; }

    public List<Job> findAll() { return repo.findAll(); }
    public List<Job> findByCompanyId(Integer companyId) { return repo.findByCompanyId(companyId); }
    public Optional<Job> findById(Integer id) { return repo.findById(id); }

    public Job create(JobRequest r) {
        Job j = new Job();
        applyRequest(j, r);
        return repo.save(j);
    }

    public Optional<Job> update(Integer id, JobRequest r) {
        return repo.findById(id).map(j -> { applyRequest(j, r); return repo.save(j); });
    }

    public boolean delete(Integer id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    private void applyRequest(Job j, JobRequest r) {
        j.setJobPosition(r.jobPosition());
        j.setDescription(r.description());
        j.setRequirements(r.requirements());
        j.setSalary(r.salary());
        j.setCompanyId(r.companyId());
        j.setResponsibilities(r.responsibilities());
        j.setJobSkill(r.jobSkill());
        j.setInternshipType(r.internshipType());
    }
}
