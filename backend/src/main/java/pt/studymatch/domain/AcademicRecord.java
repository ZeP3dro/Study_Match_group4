package pt.studymatch.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The academic record of a student (UC11). When a course unit was taken more than once,
 * only its best grade counts, so repetitions are never counted twice.
 */
public class AcademicRecord {

    private final Map<Long, CourseUnitEnrolment> bestResultByCourseUnit = new LinkedHashMap<>();

    public AcademicRecord(List<CourseUnitEnrolment> enrolments) {
        for (CourseUnitEnrolment enrolment : enrolments) {
            if (enrolment.hasGrade()) {
                keepBestGrade(enrolment);
            }
        }
    }

    private void keepBestGrade(CourseUnitEnrolment enrolment) {
        Long courseUnitId = enrolment.getCourseUnit().getId();
        CourseUnitEnrolment current = bestResultByCourseUnit.get(courseUnitId);
        if (current == null || enrolment.getGrade().compareTo(current.getGrade()) > 0) {
            bestResultByCourseUnit.put(courseUnitId, enrolment);
        }
    }

    public List<CourseUnitEnrolment> getResults() {
        return List.copyOf(bestResultByCourseUnit.values());
    }

    public BigDecimal universityAverage() {
        return weightedAverage(getResults());
    }

    /**
     * Sum of (grade × ECTS) divided by the sum of ECTS. Returns null when there are no grades.
     */
    public static BigDecimal weightedAverage(List<CourseUnitEnrolment> results) {
        BigDecimal weightedSum = BigDecimal.ZERO;
        int totalEcts = 0;
        for (CourseUnitEnrolment result : results) {
            int ects = result.getCourseUnit().getEcts();
            weightedSum = weightedSum.add(result.getGrade().multiply(BigDecimal.valueOf(ects)));
            totalEcts += ects;
        }
        if (totalEcts == 0) {
            return null;
        }
        return weightedSum.divide(BigDecimal.valueOf(totalEcts), 2, RoundingMode.HALF_UP);
    }

    /**
     * ECTS of the course units passed (best grade of 10 or more).
     */
    public int completedEcts() {
        return bestResultByCourseUnit.values().stream()
                .filter(CourseUnitEnrolment::isPassed)
                .mapToInt(result -> result.getCourseUnit().getEcts())
                .sum();
    }
}
