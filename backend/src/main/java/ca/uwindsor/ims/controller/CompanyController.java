package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.CompanyRequest;
import ca.uwindsor.ims.entity.Company;
import ca.uwindsor.ims.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

    private final CompanyService service;

    public CompanyController(CompanyService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public List<Company> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<Company> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Company create(@RequestBody CompanyRequest request) {
        Company created = service.create(request);
        log.info("Company created: id={}, name={}", created.getCompanyId(), created.getCompanyName());
        return created;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Company> update(@PathVariable Integer id, @RequestBody CompanyRequest request) {
        ResponseEntity<Company> response = service.update(id, request).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Company updated: id={}", id);
        }
        return response;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            log.info("Company deleted: id={}", id);
        }
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
