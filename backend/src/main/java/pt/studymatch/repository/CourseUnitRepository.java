package pt.studymatch.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.studymatch.domain.CourseUnit;
import pt.studymatch.domain.KnowledgeArea;

public interface CourseUnitRepository extends JpaRepository<CourseUnit, Long> {

    List<CourseUnit> findAllByOrderByCurricularYearAscSemesterAscCodeAsc();

    List<CourseUnit> findByKnowledgeAreaOrderByCurricularYearAscSemesterAscCodeAsc(KnowledgeArea knowledgeArea);

    boolean existsByCodeIgnoreCase(String code);
}
