package pt.studymatch.mapper;

import org.springframework.stereotype.Component;
import pt.studymatch.domain.KnowledgeArea;
import pt.studymatch.dto.KnowledgeAreaDTO;

@Component
public class KnowledgeAreaMapper {

    public KnowledgeAreaDTO toDTO(KnowledgeArea knowledgeArea) {
        return new KnowledgeAreaDTO(knowledgeArea.getName(), knowledgeArea.getDescription());
    }
}
