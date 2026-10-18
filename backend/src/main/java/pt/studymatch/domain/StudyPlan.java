package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pt.studymatch.enums.CourseUnitType;

/**
 * The single study plan supported by StudyMatch: Computer Engineering, 3 years, 180 ECTS (UC06).
 * It owns the rules about the whole plan, which no single course unit can check on its own.
 */
@Entity
@Table(name = "study_plans")
public class StudyPlan {

    public static final int MAX_ECTS_PER_CURRICULAR_YEAR = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String programmeName;

    @Column(nullable = false)
    private int durationYears;

    @Column(nullable = false)
    private int totalEcts;

    @OneToMany(mappedBy = "studyPlan")
    @OrderBy("curricularYear, semester, code")
    private List<CourseUnit> courseUnits = new ArrayList<>();

    protected StudyPlan() {
    }

    public StudyPlan(String programmeName, int durationYears, int totalEcts) {
        this.programmeName = programmeName;
        this.durationYears = durationYears;
        this.totalEcts = totalEcts;
    }

    public int curricularYearEcts(int curricularYear) {
        int total = 0;
        Set<String> countedGroups = new HashSet<>();
        for (CourseUnit unit : courseUnits) {
            if (unit.getCurricularYear() != curricularYear) {
                continue;
            }
            if (!unit.isOptional() || countedGroups.add(unit.getOptionGroup())) {
                total += unit.getEcts();
            }
        }
        return total;
    }

    /**
     * UC06: a curricular year cannot exceed 60 ECTS. An option group counts only once,
     * even if it has several course units to choose from.
     */
    public void checkCanAccommodate(CourseUnit edited, int year, int semester, int ects,
            CourseUnitType type, String optionGroup) {
        if (year < 1 || year > durationYears) {
            throw new IllegalArgumentException("O ano curricular tem de estar entre 1 e " + durationYears + ".");
        }
        int total = ects;
        Set<String> countedGroups = new HashSet<>();
        if (type == CourseUnitType.OPTIONAL) {
            countedGroups.add(optionGroup);
        }
        for (CourseUnit unit : courseUnits) {
            if (unit == edited || unit.getCurricularYear() != year) {
                continue;
            }
            if (unit.isOptional() && type == CourseUnitType.OPTIONAL
                    && unit.getOptionGroup().equals(optionGroup)
                    && (unit.getEcts() != ects || unit.getSemester() != semester)) {
                throw new IllegalArgumentException("As UC do mesmo grupo de opção devem ter os mesmos ECTS e semestre.");
            }
            if (!unit.isOptional() || countedGroups.add(unit.getOptionGroup())) {
                total += unit.getEcts();
            }
        }
        if (total > MAX_ECTS_PER_CURRICULAR_YEAR) {
            throw new IllegalArgumentException("O " + year + ".º ano ficaria com " + total + " ECTS. O máximo é 60.");
        }
    }

    public void addCourseUnit(CourseUnit courseUnit) {
        courseUnits.add(courseUnit);
    }

    public Long getId() {
        return id;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public int getTotalEcts() {
        return totalEcts;
    }
}
