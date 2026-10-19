package pt.studymatch.domain;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pt.studymatch.enums.ScoreSource;
import static org.assertj.core.api.Assertions.*;

class StudentProfileTest {
    private final KnowledgeArea programming = DomainFixtures.area("Programação");
    private final KnowledgeArea maths = DomainFixtures.area("Matemática");
    private final StudyPlan plan = DomainFixtures.studyPlan();

    private StudentProfile profile(Integer entryGrade, CourseUnitEnrolment... results) {
        return StudentProfile.calculate(DomainFixtures.student(entryGrade), List.of(programming, maths), new AcademicRecord(List.of(results)));
    }
    private CourseUnit unit(String code, int ects, KnowledgeArea area) {
        return DomainFixtures.courseUnit(plan, code, ects, area);
    }
    @Test
    void entryGradeIsUsedWhenThereAreNoUniversityGrades() {
        StudentProfile profile = profile(152);
        assertThat(profile.getAreaScores()).allSatisfy(area -> {
            assertThat(area.getValue()).isEqualByComparingTo("15.2");
            assertThat(area.getSource()).isEqualTo(ScoreSource.ENTRY_GRADE);
        });
        assertThat(profile.getUniversityAverage()).isNull();
    }
    @Test
    void missingDataIsExplicitlyAbsentInsteadOfZero() {
        StudentProfile profile = profile(null);
        assertThat(profile.hasAcademicData()).isFalse();
        assertThat(profile.getAreaScores()).allSatisfy(area -> {
            assertThat(area.getValue()).isNull();
            assertThat(area.getSource()).isNull();
        });
    }
    @ParameterizedTest
    @ValueSource(ints = {0, 200})
    void coldStartAcceptsBothEntryGradeBoundaries(int grade) {
        assertThat(profile(grade).getAreaScores().getFirst().getValue()).isEqualByComparingTo(BigDecimal.valueOf(grade).divide(BigDecimal.TEN));
    }
    @Test
    void firstGradeReplacesEntryGradeAndProvidesFallbackForOtherAreas() {
        StudentProfile profile = profile(190, DomainFixtures.result(unit("P1", 6, programming), 12));
        assertThat(profile.getAreaScores().get(0).getValue()).isEqualByComparingTo("12");
        assertThat(profile.getAreaScores().get(0).getSource()).isEqualTo(ScoreSource.AREA_AVERAGE);
        assertThat(profile.getAreaScores().get(1).getValue()).isEqualByComparingTo("12");
        assertThat(profile.getAreaScores().get(1).getSource()).isEqualTo(ScoreSource.UNIVERSITY_AVERAGE);
    }
    @Test
    void eachAreaUsesItsOwnGradesWeightedByEcts() {
        StudentProfile profile = profile(null,
                DomainFixtures.result(unit("P1", 6, programming), 10),
                DomainFixtures.result(unit("P2", 12, programming), 16),
                DomainFixtures.result(unit("M1", 6, maths), 20));
        assertThat(profile.getAreaScores().getFirst().getValue()).isEqualByComparingTo("14");
        assertThat(profile.getAreaScores().get(1).getValue()).isEqualByComparingTo("20");
        assertThat(profile.getUniversityAverage()).isEqualByComparingTo("15.5");
    }
    @Test
    void ungradedEnrolmentDoesNotReplaceColdStart() {
        CourseUnitEnrolment empty = new CourseUnitEnrolment(DomainFixtures.student(null), unit("P1",6,programming), AcademicYear.startingIn(2024), java.time.LocalDate.of(2024,9,1));
        StudentProfile profile = profile(150, empty);
        assertThat(profile.courseUnitsCount()).isZero();
        assertThat(profile.getAreaScores().getFirst().getSource()).isEqualTo(ScoreSource.ENTRY_GRADE);
    }
    @Test
    void gradesWithoutAreaContributeToGlobalAverage() {
        StudentProfile profile = profile(null, DomainFixtures.result(unit("N1",6,null), 18));
        assertThat(profile.getUniversityAverage()).isEqualByComparingTo("18");
        assertThat(profile.getAreaScores()).allSatisfy(area -> assertThat(area.getSource()).isEqualTo(ScoreSource.UNIVERSITY_AVERAGE));
    }
    @Test
    void failedGradeContributesToAverageButNotCompletedEcts() {
        AcademicRecord record = new AcademicRecord(List.of(
                DomainFixtures.result(unit("P1",6,programming), 8),
                DomainFixtures.result(unit("M1",6,maths), 10)));
        assertThat(record.universityAverage()).isEqualByComparingTo("9");
        assertThat(record.completedEcts()).isEqualTo(6);
    }
    @Test
    void correctingAGradeRecalculatesProfile() {
        CourseUnitEnrolment result = DomainFixtures.result(unit("P1",6,programming), 18);
        assertThat(profile(null,result).getUniversityAverage()).isEqualByComparingTo("18");
        result.recordGrade(BigDecimal.TEN);
        assertThat(profile(null,result).getUniversityAverage()).isEqualByComparingTo("10");
    }
    @ParameterizedTest
    @ValueSource(strings = {"-0.1", "20.1", "10.11"})
    void invalidGradesAreRejected(String grade) {
        assertThatThrownBy(() -> CourseUnitEnrolment.validateGrade(new BigDecimal(grade))).isInstanceOf(IllegalArgumentException.class);
    }
    @ParameterizedTest
    @ValueSource(strings = {"0", "20"})
    void gradesAcceptBothScaleBoundaries(String grade) {
        assertThat(CourseUnitEnrolment.validateGrade(new BigDecimal(grade))).isEqualByComparingTo(grade);
    }
    @Test
    void nullGradeIsRejected() {
        assertThatThrownBy(() -> CourseUnitEnrolment.validateGrade(null)).isInstanceOf(IllegalArgumentException.class);
    }
}
