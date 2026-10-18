package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Month;

/**
 * An academic year runs from 1 September to 31 July of the next year.
 */
@Entity
@Table(name = "academic_years")
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    protected AcademicYear() {
    }

    public AcademicYear(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("O fim do ano letivo tem de ser depois do início.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static AcademicYear startingIn(int year) {
        return new AcademicYear(LocalDate.of(year, Month.SEPTEMBER, 1), LocalDate.of(year + 1, Month.JULY, 31));
    }

    public static int startYearOf(LocalDate date) {
        return date.getMonthValue() >= Month.SEPTEMBER.getValue() ? date.getYear() : date.getYear() - 1;
    }

    /**
     * Label used in the interface, e.g. "2026/27".
     */
    public String label() {
        int startYear = startDate.getYear();
        return startYear + "/" + String.format("%02d", (startYear + 1) % 100);
    }

    public int getStartYear() {
        return startDate.getYear();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
