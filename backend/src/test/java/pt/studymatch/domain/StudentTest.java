package pt.studymatch.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentTest {

    @Test
    void shouldCreateInstitutionalEmailFromNumber() {
        Student student = new Student("20241234", "Joana Silva", "encoded", 2024, 164);

        assertThat(student.getEmail()).isEqualTo("20241234@studymatch.com");
    }


    @ParameterizedTest
    @ValueSource(strings = {"0", "202412345", "abcdefgh", "12 345", "1234a"})
    void shouldRejectInvalidAccountNumbers(String number) {
        assertThatThrownBy(() -> new Student(number, "Joana Silva", "encoded", 2024, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 200})
    void shouldAcceptEntryGradeOnTheLimits(int entryGrade) {
        Student student = new Student("20241234", "Joana Silva", "encoded", 2024, entryGrade);

        assertThat(student.getEntryGrade()).isEqualTo(entryGrade);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 201})
    void shouldRejectEntryGradeOutsideZeroToTwoHundred(int entryGrade) {
        assertThatThrownBy(() -> new Student("20241234", "Joana Silva", "encoded", 2024, entryGrade))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("entre 0 e 200");
    }

    @Test
    void shouldConvertEntryGradeToTwentyScale() {
        Student student = new Student("20241234", "Joana Silva", "encoded", 2024, 164);

        assertThat(student.entryGradeOn20()).hasValueSatisfying(grade -> assertThat(grade).isEqualByComparingTo("16.4"));
    }



    @ParameterizedTest
    @CsvSource({"0, 1", "59, 1", "60, 2", "119, 2", "120, 3", "180, 3"})
    void shouldDeriveCurricularYearFromCompletedEcts(int completedEcts, int expectedYear) {
        assertThat(Student.curricularYearFor(completedEcts)).isEqualTo(expectedYear);
    }
}
