package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.CompanyRequest;
import ca.uwindsor.ims.entity.Company;
import ca.uwindsor.ims.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository repo;
    public CompanyService(CompanyRepository repo) { this.repo = repo; }

    public List<Company> findAll() { return repo.findAll(); }
    public Optional<Company> findById(Integer id) { return repo.findById(id); }

    public Company create(CompanyRequest r) {
        log.debug("Creating company: name={}", r.companyName());
        Company c = new Company();
        applyRequest(c, r);
        return repo.save(c);
    }

    public Optional<Company> update(Integer id, CompanyRequest r) {
        log.debug("Updating company: id={}", id);
        return repo.findById(id).map(c -> { applyRequest(c, r); return repo.save(c); });
    }

    public boolean delete(Integer id) {
        log.debug("Deleting company: id={}", id);
        if (!repo.existsById(id)) {
            log.debug("Company not found for delete: id={}", id);
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    private void applyRequest(Company c, CompanyRequest r) {
        c.setCompanyName(r.companyName());
        c.setAddress(r.address());
        c.setCity(r.city());
        c.setPostalCode(r.postalCode());
        c.setCountry(r.country());
        c.setContactPersonFname(r.contactPersonFname());
        c.setContactPersonLname(r.contactPersonLname());
        c.setContactPersonPosition(r.contactPersonPosition());
        c.setTelephone(r.telephone());
        c.setEmail(r.email());
        c.setCompanyWebsite(r.companyWebsite());
        c.setNotes(r.notes());
    }
}
