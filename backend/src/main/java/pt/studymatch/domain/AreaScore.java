package pt.studymatch.domain;

import java.math.BigDecimal;
import java.util.List;
import pt.studymatch.enums.ScoreSource;

/**
 * Value of one knowledge area in the profile and where it comes from (cold-start cascade):
 * area average, then university average, then entry grade ÷ 10.
 */
public class AreaScore {
    private final KnowledgeArea knowledgeArea;
    private final List<CourseUnitEnrolment> results;
    private final BigDecimal value;
    private final ScoreSource source;
    private final int courseUnitsCount;
    private final int totalEcts;

    public AreaScore(KnowledgeArea area, List<CourseUnitEnrolment> results,
            BigDecimal universityAverage, BigDecimal entryGrade) {
        knowledgeArea = area;
        this.results = List.copyOf(results);
        courseUnitsCount = results.size();
        totalEcts = results.stream().mapToInt(result -> result.getCourseUnit().getEcts()).sum();
        BigDecimal average = AcademicRecord.weightedAverage(results);
        if (average != null) {
            value = average;
            source = ScoreSource.AREA_AVERAGE;
        } else {
            value = null;
            source = null;
        }
    }

    public List<CourseUnitEnrolment> getResults() {
        return results;
    }

    public KnowledgeArea getKnowledgeArea() {
        return knowledgeArea;
    }

    public BigDecimal getValue() {
        return value;
    }

    public ScoreSource getSource() {
        return source;
    }

    public int getCourseUnitsCount() {
        return courseUnitsCount;
    }

    public int getTotalEcts() {
        return totalEcts;
    }
}
