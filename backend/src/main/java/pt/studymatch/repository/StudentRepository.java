package pt.studymatch.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pt.studymatch.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNumberContainingOrderByNumberAsc(String number);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select student from Student student where student.id = :id")
    Optional<Student> findForEnrolment(Long id);
}
