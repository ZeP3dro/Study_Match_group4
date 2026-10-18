package pt.studymatch.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.studymatch.domain.KnowledgeArea;
import pt.studymatch.dto.KnowledgeAreaDTO;
import pt.studymatch.dto.KnowledgeAreaRequestDTO;
import pt.studymatch.mapper.KnowledgeAreaMapper;
import pt.studymatch.service.KnowledgeAreaService;

/**
 * UC12 manage knowledge areas. Every signed-in user can see the areas.
 */
@RestController
@RequestMapping("/knowledge-areas")
public class KnowledgeAreaController {

    private final KnowledgeAreaService knowledgeAreaService;
    private final KnowledgeAreaMapper knowledgeAreaMapper;

    public KnowledgeAreaController(KnowledgeAreaService knowledgeAreaService,
            KnowledgeAreaMapper knowledgeAreaMapper) {
        this.knowledgeAreaService = knowledgeAreaService;
        this.knowledgeAreaMapper = knowledgeAreaMapper;
    }

    @GetMapping
    public List<KnowledgeAreaDTO> findAll() {
        return knowledgeAreaService.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public KnowledgeAreaDTO findById(@PathVariable Long id) {
        return toDTO(knowledgeAreaService.findById(id));
    }

    @PostMapping
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public ResponseEntity<KnowledgeAreaDTO> create(@Valid @RequestBody KnowledgeAreaRequestDTO request) {
        KnowledgeArea area = knowledgeAreaService.create(request.getName(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(area));
    }

    @PutMapping("/{id}")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public KnowledgeAreaDTO update(@PathVariable Long id, @Valid @RequestBody KnowledgeAreaRequestDTO request) {
        return toDTO(knowledgeAreaService.update(id, request.getName(), request.getDescription()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        knowledgeAreaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private KnowledgeAreaDTO toDTO(KnowledgeArea area) {
        return knowledgeAreaMapper.toDTO(area, knowledgeAreaService.courseUnitsOf(area));
    }
}
