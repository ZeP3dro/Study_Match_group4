package pt.studymatch.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.studymatch.dto.KnowledgeAreaDTO;
import pt.studymatch.mapper.KnowledgeAreaMapper;
import pt.studymatch.service.KnowledgeAreaService;

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
                .map(knowledgeAreaMapper::toDTO)
                .toList();
    }
}
