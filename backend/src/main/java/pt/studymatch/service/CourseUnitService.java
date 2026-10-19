package pt.studymatch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.CourseUnit;
import pt.studymatch.domain.CourseUnitEnrolment;
import pt.studymatch.domain.StudyPlan;
import pt.studymatch.enums.CourseUnitType;
import pt.studymatch.repository.CourseUnitEnrolmentRepository;
import pt.studymatch.repository.CourseUnitRepository;
import pt.studymatch.repository.StudyPlanRepository;

/**
 * UC06 manage course units and UC13 assign knowledge area to course unit.
 */
@Service
@Transactional
public class CourseUnitService {

    /** The study plan with the ECTS of each curricular year (index 0 is the 1st year). */
    public static class StudyPlanSummary {
        private final StudyPlan studyPlan;
        private final List<Integer> ectsByYear;

        StudyPlanSummary(StudyPlan studyPlan, List<Integer> ectsByYear) {
            this.studyPlan = studyPlan;
            this.ectsByYear = ectsByYear;
        }

        public StudyPlan getStudyPlan() {
            return studyPlan;
        }

        public List<Integer> getEctsByYear() {
            return ectsByYear;
        }
    }

    private final CourseUnitRepository units;
    private final StudyPlanRepository plans;
    private final KnowledgeAreaService areas;
    private final CourseUnitEnrolmentRepository enrolments;

    public CourseUnitService(CourseUnitRepository units, StudyPlanRepository plans,
            KnowledgeAreaService areas, CourseUnitEnrolmentRepository enrolments) {
        this.units = units;
        this.plans = plans;
        this.areas = areas;
        this.enrolments = enrolments;
    }

    @Transactional(readOnly = true)
    public List<CourseUnit> findAll() {
        return units.findAllByOrderByCurricularYearAscSemesterAscCodeAsc();
    }

    @Transactional(readOnly = true)
    public CourseUnit findById(Long id) {
        return units.findById(id).orElseThrow(() -> new NoSuchElementException("Unidade curricular não encontrada."));
    }

    @Transactional(readOnly = true)
    public StudyPlanSummary studyPlan() {
        StudyPlan plan = currentPlan();
        List<Integer> ectsByYear = new ArrayList<>();
        for (int year = 1; year <= plan.getDurationYears(); year++) {
            ectsByYear.add(plan.curricularYearEcts(year));
        }
        return new StudyPlanSummary(plan, ectsByYear);
    }

    public CourseUnit create(String code, String name, int ects, int curricularYear, int semester,
            CourseUnitType type, String optionGroup, Long knowledgeAreaId) {
        if (units.existsByCodeIgnoreCase(code.strip())) {
            throw new IllegalArgumentException("Já existe uma UC com esse código.");
        }
        CourseUnit unit = new CourseUnit(currentPlan(), code, name, ects, curricularYear, semester, type, optionGroup);
        assignArea(unit, knowledgeAreaId);
        return units.save(unit);
    }

    public CourseUnit update(Long id, String name, int ects, int curricularYear, int semester,
            CourseUnitType type, String optionGroup, Long knowledgeAreaId) {
        CourseUnit unit = findById(id);
        unit.update(name, ects, curricularYear, semester, type, optionGroup);
        assignArea(unit, knowledgeAreaId);
        checkExistingEnrolments(unit);
        return unit;
    }

    public CourseUnit assignKnowledgeArea(Long id, Long knowledgeAreaId) {
        CourseUnit unit = findById(id);
        assignArea(unit, knowledgeAreaId);
        return unit;
    }

    public void delete(Long id) {
        CourseUnit unit = findById(id);
        if (enrolments.existsByCourseUnit(unit)) {
            throw new IllegalArgumentException("A UC tem inscrições ou notas e não pode ser eliminada.");
        }
        units.delete(unit);
    }

    private StudyPlan currentPlan() {
        return plans.findFirstByOrderByIdAsc().orElseThrow();
    }

    private void assignArea(CourseUnit unit, Long knowledgeAreaId) {
        unit.setKnowledgeArea(knowledgeAreaId == null ? null : areas.findById(knowledgeAreaId));
    }

    /**
     * Changing the ECTS or the semester must still respect the limits of the existing enrolments.
     */
    private void checkExistingEnrolments(CourseUnit unit) {
        for (CourseUnitEnrolment enrolment : enrolments.findByCourseUnit(unit)) {
            List<CourseUnitEnrolment> sameYear = enrolments.findByStudentAndAcademicYear(
                    enrolment.getStudent(), enrolment.getAcademicYear());
            CourseUnitEnrolment.checkEctsLimits(sameYear, null);
            for (CourseUnitEnrolment other : sameYear) {
                boolean isOtherUnit = !other.getCourseUnit().getId().equals(unit.getId());
                if (isOtherUnit && unit.hasSameOptionGroup(other.getCourseUnit())) {
                    throw new IllegalArgumentException("Um aluno ficaria inscrito em duas UC do mesmo grupo de opção.");
                }
            }
        }
    }
}
