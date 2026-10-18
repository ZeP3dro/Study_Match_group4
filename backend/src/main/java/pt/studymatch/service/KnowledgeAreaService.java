package pt.studymatch.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.CourseUnit;
import pt.studymatch.domain.KnowledgeArea;
import pt.studymatch.repository.CourseUnitRepository;
import pt.studymatch.repository.KnowledgeAreaRepository;

/**
 * UC12 manage knowledge areas.
 */
@Service
@Transactional
public class KnowledgeAreaService {

    private final KnowledgeAreaRepository knowledgeAreaRepository;
    private final CourseUnitRepository courseUnitRepository;

    public KnowledgeAreaService(KnowledgeAreaRepository knowledgeAreaRepository,
            CourseUnitRepository courseUnitRepository) {
        this.knowledgeAreaRepository = knowledgeAreaRepository;
        this.courseUnitRepository = courseUnitRepository;
    }

    @Transactional(readOnly = true)
    public List<KnowledgeArea> findAll() {
        return knowledgeAreaRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public KnowledgeArea findById(Long id) {
        return knowledgeAreaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Área de conhecimento não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<CourseUnit> courseUnitsOf(KnowledgeArea area) {
        return courseUnitRepository.findByKnowledgeAreaOrderByCurricularYearAscSemesterAscCodeAsc(area);
    }

    public KnowledgeArea create(String name, String description) {
        checkNameIsFree(name, null);
        return knowledgeAreaRepository.save(new KnowledgeArea(name, description));
    }

    public KnowledgeArea update(Long id, String name, String description) {
        KnowledgeArea area = findById(id);
        checkNameIsFree(name, area);
        area.update(name, description);
        return area;
    }

    /**
     * UC12: an area with course units cannot be deleted.
     */
    public void delete(Long id) {
        KnowledgeArea area = findById(id);
        List<CourseUnit> courseUnits = courseUnitsOf(area);
        if (!courseUnits.isEmpty()) {
            String names = String.join(", ", courseUnits.stream().map(CourseUnit::getName).toList());
            throw new IllegalArgumentException("A área tem unidades curriculares associadas: " + names + ".");
        }
        knowledgeAreaRepository.delete(area);
    }

    /**
     * UC12: area names are unique, ignoring upper/lower case and extra spaces.
     */
    private void checkNameIsFree(String name, KnowledgeArea areaBeingEdited) {
        boolean nameTaken = knowledgeAreaRepository.findAll().stream()
                .filter(existing -> existing != areaBeingEdited)
                .anyMatch(existing -> existing.hasSameName(name));
        if (nameTaken) {
            throw new IllegalArgumentException("Já existe uma área com este nome (ignora maiúsculas e espaços).");
        }
    }
}
