package pt.studymatch.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.studymatch.domain.AcademicYear;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {

    List<AcademicYear> findAllByOrderByStartDateDesc();

    Optional<AcademicYear> findByStartDate(LocalDate startDate);
}
