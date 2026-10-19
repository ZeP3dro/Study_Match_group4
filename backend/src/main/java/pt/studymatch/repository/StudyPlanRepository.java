package pt.studymatch.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.studymatch.domain.StudyPlan;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {

    Optional<StudyPlan> findFirstByOrderByIdAsc();
}
