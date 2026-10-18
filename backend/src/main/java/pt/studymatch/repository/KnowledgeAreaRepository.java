package pt.studymatch.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.studymatch.domain.KnowledgeArea;

public interface KnowledgeAreaRepository extends JpaRepository<KnowledgeArea, Long> {

    List<KnowledgeArea> findAllByOrderByIdAsc();
}
