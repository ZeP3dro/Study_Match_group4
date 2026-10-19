package pt.studymatch.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * Academic profile of a student (UC14, UC15): one score per knowledge area and the
 * overall university average.
 *
 * The profile is calculated from the current academic record every time it is requested,
 * so it always reflects the latest grades (decision taken for Sprint 2).
 */
public class StudentProfile {

    private final List<AreaScore> areaScores;
    private final List<CourseUnitEnrolment> results;
    private final BigDecimal universityAverage;
    private final BigDecimal entryGradeOn20;

    private StudentProfile(List<AreaScore> areaScores, List<CourseUnitEnrolment> results,
            BigDecimal universityAverage, BigDecimal entryGradeOn20) {
        this.areaScores = areaScores;
        this.results = results;
        this.universityAverage = universityAverage;
        this.entryGradeOn20 = entryGradeOn20;
    }

    public static StudentProfile calculate(Student student, List<KnowledgeArea> areas, AcademicRecord record) {
        List<CourseUnitEnrolment> results = record.getResults();
        BigDecimal universityAverage = record.universityAverage();
        BigDecimal entryGradeOn20 = student.entryGradeOn20().orElse(null);

        List<AreaScore> areaScores = areas.stream()
                .map(area -> new AreaScore(area, resultsOf(area, results), universityAverage, entryGradeOn20))
                .toList();
        return new StudentProfile(areaScores, results, universityAverage, entryGradeOn20);
    }

    private static List<CourseUnitEnrolment> resultsOf(KnowledgeArea area, List<CourseUnitEnrolment> results) {
        return results.stream()
                .filter(result -> result.getCourseUnit().belongsTo(area))
                .toList();
    }

    public boolean hasAcademicData() {
        return universityAverage != null;
    }

    public int courseUnitsCount() {
        return results.size();
    }

    public int totalEcts() {
        return results.stream().mapToInt(result -> result.getCourseUnit().getEcts()).sum();
    }

    public List<AreaScore> getAreaScores() {
        return areaScores;
    }

    public BigDecimal getUniversityAverage() {
        return universityAverage;
    }

    public BigDecimal getEntryGradeOn20() {
        return entryGradeOn20;
    }
}
