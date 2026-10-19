package pt.studymatch.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.AcademicYear;
import pt.studymatch.domain.CourseUnit;
import pt.studymatch.domain.CourseUnitEnrolment;
import pt.studymatch.domain.Student;
import pt.studymatch.repository.CourseUnitEnrolmentRepository;

@Service
@Transactional
public class EnrolmentService {
    private final CourseUnitEnrolmentRepository enrolments;
    private final StudentService students;
    private final CourseUnitService units;
    private final AcademicYearService years;

    public EnrolmentService(CourseUnitEnrolmentRepository enrolments, StudentService students,
            CourseUnitService units, AcademicYearService years) {
        this.enrolments = enrolments;
        this.students = students;
        this.units = units;
        this.years = years;
    }

    @Transactional(readOnly = true)
    public List<CourseUnitEnrolment> enrolmentsOf(Long studentId) {
        return enrolments.findAllOf(students.findById(studentId));
    }

    public CourseUnitEnrolment enrol(Long studentId, Long courseUnitId, Long academicYearId) {
        Student student = students.findForEnrolment(studentId);
        CourseUnit unit = units.findById(courseUnitId);
        AcademicYear year = years.findById(academicYearId);
        if (!student.canEnrol()) {
            throw new IllegalArgumentException("Só estudantes ativos se podem inscrever.");
        }
        if (year.getStartYear() < student.getFirstEnrolmentYear()) {
            throw new IllegalArgumentException("O ano letivo é anterior à primeira inscrição do estudante.");
        }
        if (enrolments.existsByStudentAndCourseUnitAndAcademicYear(student, unit, year)) {
            throw new IllegalArgumentException("O estudante já está inscrito nesta UC neste ano letivo.");
        }
        if (enrolments.existsByStudentAndCourseUnitAndGradeGreaterThanEqual(student, unit, BigDecimal.TEN)) {
            throw new IllegalArgumentException("Esta UC já foi aprovada.");
        }
        List<CourseUnitEnrolment> enrolledYear = enrolments.findByStudentAndAcademicYear(student, year);
        if (enrolledYear.stream().anyMatch(item -> unit.hasSameOptionGroup(item.getCourseUnit()))) {
            throw new IllegalArgumentException("O estudante já escolheu uma UC deste grupo de opção.");
        }
        CourseUnitEnrolment.checkEctsLimits(enrolledYear, unit);
        return enrolments.save(new CourseUnitEnrolment(student, unit, year, years.today()));
    }

    public CourseUnitEnrolment recordGrade(Long studentId, Long enrolmentId, BigDecimal grade) {
        CourseUnitEnrolment enrolment = enrolments.findById(enrolmentId)
                .filter(item -> item.getStudent().getId().equals(studentId))
                .orElseThrow(() -> new NoSuchElementException("Inscrição não encontrada para este estudante."));
        enrolment.recordGrade(grade);
        return enrolment;
    }
}
