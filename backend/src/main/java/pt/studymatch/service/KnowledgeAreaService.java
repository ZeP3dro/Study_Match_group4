package pt.studymatch.service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.stereotype.Service;
import pt.studymatch.domain.KnowledgeArea;
import pt.studymatch.repository.KnowledgeAreaRepository;

@Service
public class KnowledgeAreaService {

    private static final List<KnowledgeArea> DEFAULT_KNOWLEDGE_AREAS = List.of(
            new KnowledgeArea("Programming", "Algorithms, programming and web technologies."),
            new KnowledgeArea("Mathematics and Physics", "Mathematics, physics and applied statistics."),
            new KnowledgeArea("Information Systems and Software Engineering",
                    "Information systems, requirements and software quality."),
            new KnowledgeArea("Computer Systems and Networks",
                    "Computer architecture, operating systems, networks and security."),
            new KnowledgeArea("Data and Artificial Intelligence",
                    "Databases, artificial intelligence and data analysis."));

    private final KnowledgeAreaRepository knowledgeAreaRepository;

    public KnowledgeAreaService(KnowledgeAreaRepository knowledgeAreaRepository) {
        this.knowledgeAreaRepository = knowledgeAreaRepository;
    }

    @PostConstruct
    public void initData() {
        if (knowledgeAreaRepository.count() == 0) {
            knowledgeAreaRepository.saveAll(DEFAULT_KNOWLEDGE_AREAS.stream()
                    .map(area -> new KnowledgeArea(area.getName(), area.getDescription())).toList());
        }
    }

    public List<KnowledgeArea> findAll() {
        return knowledgeAreaRepository.findAll();
    }
}
