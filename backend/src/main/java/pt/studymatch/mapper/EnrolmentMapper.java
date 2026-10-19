package pt.studymatch.mapper;

import org.springframework.stereotype.Component;
import pt.studymatch.domain.AcademicYear;
import pt.studymatch.domain.CourseUnitEnrolment;
import pt.studymatch.dto.AcademicYearDTO;
import pt.studymatch.dto.EnrolmentDTO;

@Component
public class EnrolmentMapper {

    private final CourseUnitMapper courseUnitMapper;

    public EnrolmentMapper(CourseUnitMapper courseUnitMapper) {
        this.courseUnitMapper = courseUnitMapper;
    }

    public AcademicYearDTO toDTO(AcademicYear academicYear, AcademicYear currentYear) {
        return new AcademicYearDTO(academicYear.getId(), academicYear.label(),
                academicYear.getId().equals(currentYear.getId()));
    }

    public EnrolmentDTO toDTO(CourseUnitEnrolment enrolment) {
        return new EnrolmentDTO(
                enrolment.getId(),
                courseUnitMapper.toDTO(enrolment.getCourseUnit()),
                enrolment.getAcademicYear().getId(),
                enrolment.getAcademicYear().label(),
                enrolment.getEnrolmentDate(),
                enrolment.getGrade(),
                enrolment.isPassed());
    }

}
