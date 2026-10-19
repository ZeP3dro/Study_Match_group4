package pt.studymatch.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.AcademicRecord;
import pt.studymatch.domain.Student;
import pt.studymatch.domain.StudentProfile;
import pt.studymatch.repository.CourseUnitEnrolmentRepository;
import pt.studymatch.repository.KnowledgeAreaRepository;

/**
 * Academic record and profile of a student (UC11, UC14, UC15), calculated from the enrolments
 * every time they are requested.
 */
@Service
@Transactional(readOnly = true)
public class ProfileService {

    private final CourseUnitEnrolmentRepository enrolments;
    private final KnowledgeAreaRepository areas;

    public ProfileService(CourseUnitEnrolmentRepository enrolments, KnowledgeAreaRepository areas) {
        this.enrolments = enrolments;
        this.areas = areas;
    }

    public AcademicRecord recordOf(Student student) {
        return new AcademicRecord(enrolments.findAllOf(student));
    }

    public int curricularYearOf(Student student) {
        return Student.curricularYearFor(recordOf(student).completedEcts());
    }

    public StudentProfile profileOf(Student student) {
        return StudentProfile.calculate(student, areas.findAll(), recordOf(student));
    }
}
