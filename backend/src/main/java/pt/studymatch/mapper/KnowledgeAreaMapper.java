package pt.studymatch.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import pt.studymatch.domain.CourseUnit;
import pt.studymatch.domain.KnowledgeArea;
import pt.studymatch.dto.KnowledgeAreaDTO;

@Component
public class KnowledgeAreaMapper {

    private final CourseUnitMapper courseUnitMapper;

    public KnowledgeAreaMapper(CourseUnitMapper courseUnitMapper) {
        this.courseUnitMapper = courseUnitMapper;
    }

    public KnowledgeAreaDTO toDTO(KnowledgeArea knowledgeArea, List<CourseUnit> courseUnits) {
        return new KnowledgeAreaDTO(
                knowledgeArea.getId(),
                knowledgeArea.getName(),
                knowledgeArea.getDescription(),
                courseUnits.stream().map(courseUnitMapper::toDTO).toList());
    }
}
