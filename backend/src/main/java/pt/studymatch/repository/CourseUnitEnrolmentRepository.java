package pt.studymatch.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.studymatch.domain.AcademicYear;
import pt.studymatch.domain.CourseUnit;
import pt.studymatch.domain.CourseUnitEnrolment;
import pt.studymatch.domain.Student;

public interface CourseUnitEnrolmentRepository extends JpaRepository<CourseUnitEnrolment, Long> {
    @Query("select e from CourseUnitEnrolment e where e.student = :student "
            + "order by e.academicYear.startDate, e.courseUnit.curricularYear, e.courseUnit.semester, e.courseUnit.code")
    List<CourseUnitEnrolment> findAllOf(Student student);
    List<CourseUnitEnrolment> findByStudentAndAcademicYear(Student student, AcademicYear year);
    List<CourseUnitEnrolment> findByCourseUnit(CourseUnit unit);
    boolean existsByCourseUnit(CourseUnit unit);
    boolean existsByStudentAndCourseUnitAndAcademicYear(Student student, CourseUnit unit, AcademicYear year);
    boolean existsByStudentAndCourseUnitAndGradeGreaterThanEqual(Student student, CourseUnit unit, BigDecimal grade);
}
