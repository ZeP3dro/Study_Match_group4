package pt.studymatch.mapper;

import org.springframework.stereotype.Component;
import pt.studymatch.domain.CourseUnit;
import pt.studymatch.domain.StudyPlan;
import pt.studymatch.dto.CourseUnitDTO;
import pt.studymatch.dto.StudyPlanDTO;
import pt.studymatch.service.CourseUnitService.StudyPlanSummary;

@Component
public class CourseUnitMapper {

    public CourseUnitDTO toDTO(CourseUnit courseUnit) {
        boolean hasArea = courseUnit.hasKnowledgeArea();
        CourseUnitDTO dto = new CourseUnitDTO(
                courseUnit.getId(),
                courseUnit.getCode(),
                courseUnit.getName(),
                courseUnit.getEcts(),
                courseUnit.getCurricularYear(),
                courseUnit.getSemester(),
                courseUnit.getType(),
                hasArea ? courseUnit.getKnowledgeArea().getId() : null,
                hasArea ? courseUnit.getKnowledgeArea().getName() : null);
        dto.setOptionGroup(courseUnit.getOptionGroup());
        return dto;
    }

    public StudyPlanDTO toDTO(StudyPlanSummary summary) {
        StudyPlan plan = summary.getStudyPlan();
        return new StudyPlanDTO(plan.getProgrammeName(), plan.getDurationYears(), plan.getTotalEcts(),
                summary.getEctsByYear());
    }
}
