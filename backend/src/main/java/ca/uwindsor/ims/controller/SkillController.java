package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.SkillRequest;
import ca.uwindsor.ims.entity.Skill;
import ca.uwindsor.ims.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillService service;
    public SkillController(SkillService service) { this.service = service; }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public List<Skill> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<Skill> getById(@PathVariable Integer id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Skill create(@RequestBody SkillRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Skill> update(@PathVariable Integer id, @RequestBody SkillRequest request) {
        return service.update(id, request).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.delete(id) ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
