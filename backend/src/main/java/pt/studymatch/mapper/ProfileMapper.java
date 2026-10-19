package pt.studymatch.mapper;

import org.springframework.stereotype.Component;
import pt.studymatch.domain.AreaScore;
import pt.studymatch.domain.Student;
import pt.studymatch.domain.StudentProfile;
import pt.studymatch.dto.AreaScoreDTO;
import pt.studymatch.dto.ProfileDTO;

@Component
public class ProfileMapper {
    private final EnrolmentMapper enrolments;

    public ProfileMapper(EnrolmentMapper enrolments) {
        this.enrolments = enrolments;
    }

    public ProfileDTO toDTO(Student student, StudentProfile profile) {
        return new ProfileDTO(
                student.getId(),
                profile.getUniversityAverage(),
                student.getEntryGrade(),
                profile.getEntryGradeOn20(),
                profile.courseUnitsCount(),
                profile.totalEcts(),
                profile.hasAcademicData(),
                profile.getAreaScores().stream().map(this::toDTO).toList());
    }

    public AreaScoreDTO toDTO(AreaScore areaScore) {
        AreaScoreDTO dto = new AreaScoreDTO(
                areaScore.getKnowledgeArea().getId(),
                areaScore.getKnowledgeArea().getName(),
                areaScore.getKnowledgeArea().getDescription(),
                areaScore.getValue(),
                areaScore.getSource(),
                areaScore.getCourseUnitsCount(),
                areaScore.getTotalEcts());
        return dto;
    }

}
