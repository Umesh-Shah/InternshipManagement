package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.InternshipTypeRequest;
import ca.uwindsor.ims.entity.InternshipType;
import ca.uwindsor.ims.repository.InternshipTypeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InternshipTypeService {
    private final InternshipTypeRepository repo;
    public InternshipTypeService(InternshipTypeRepository repo) { this.repo = repo; }

    public List<InternshipType> findAll() { return repo.findAll(); }
    public Optional<InternshipType> findById(Integer id) { return repo.findById(id); }

    public InternshipType create(InternshipTypeRequest r) {
        InternshipType t = new InternshipType();
        applyRequest(t, r);
        return repo.save(t);
    }

    public Optional<InternshipType> update(Integer id, InternshipTypeRequest r) {
        return repo.findById(id).map(t -> { applyRequest(t, r); return repo.save(t); });
    }

    public boolean delete(Integer id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    private void applyRequest(InternshipType t, InternshipTypeRequest r) {
        t.setInternshipType(r.internshipType());
        t.setDescription(r.description());
        t.setInternshipName(r.internshipName());
    }
}
