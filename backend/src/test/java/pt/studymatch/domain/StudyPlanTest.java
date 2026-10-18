package pt.studymatch.domain;

import org.junit.jupiter.api.Test;
import pt.studymatch.enums.CourseUnitType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudyPlanTest {

    @Test
    void curricularYearCanHaveExactlySixtyEcts() {
        StudyPlan plan = DomainFixtures.studyPlan();
        for (int i = 1; i <= 10; i++) {
            new CourseUnit(plan, "EI1" + i, "UC " + i, 6, 1, 1, CourseUnitType.MANDATORY);
        }

        assertThatThrownBy(() -> new CourseUnit(plan, "EI199", "UC extra", 1, 1, 2, CourseUnitType.MANDATORY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("61 ECTS");
    }

    @Test
    void editingACourseUnitDoesNotCountItsOwnEctsTwice() {
        StudyPlan plan = DomainFixtures.studyPlan();
        CourseUnit courseUnit = new CourseUnit(plan, "EI101", "Algoritmos", 60, 1, 1, CourseUnitType.MANDATORY);

        courseUnit.update("Algoritmos e Programação", 60, 1, 1, CourseUnitType.MANDATORY, null);

        assertThat(courseUnit.getName()).isEqualTo("Algoritmos e Programação");
    }

    @Test
    void shouldRejectInvalidSemesterAndEcts() {
        StudyPlan plan = DomainFixtures.studyPlan();

        assertThatThrownBy(() -> new CourseUnit(plan, "EI101", "UC", 6, 1, 3, CourseUnitType.MANDATORY))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new CourseUnit(plan, "EI102", "UC", 0, 1, 1, CourseUnitType.MANDATORY))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new CourseUnit(plan, "EI103", "UC", 6, 4, 1, CourseUnitType.MANDATORY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void areaNamesAreComparedIgnoringCaseAndSpaces() {
        KnowledgeArea area = new KnowledgeArea("Programação", "");

        assertThat(area.hasSameName("  programação ")).isTrue();
        assertThat(area.hasSameName("Matemática")).isFalse();
    }
}
