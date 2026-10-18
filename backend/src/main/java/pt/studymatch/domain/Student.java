package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import pt.studymatch.enums.Role;
import pt.studymatch.enums.StudentStatus;

@Entity
@Table(name = "students")
public class Student extends UserAccount {

    public static final int MIN_ENTRY_GRADE = 0;
    public static final int MAX_ENTRY_GRADE = 200;
    private static final int FIRST_VALID_YEAR = 2000;

    @Column(nullable = false)
    private int firstEnrolmentYear;

    private Integer entryGrade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StudentStatus status = StudentStatus.ACTIVE;

    protected Student() {
    }

    public Student(String number, String name, String encodedPassword, int firstEnrolmentYear, Integer entryGrade) {
        super(number, name, encodedPassword);
        changeFirstEnrolmentYear(firstEnrolmentYear);
        changeEntryGrade(entryGrade);
    }


    public void changeFirstEnrolmentYear(int year) {
        if (year < FIRST_VALID_YEAR) {
            throw new IllegalArgumentException("O ano da primeira inscrição não é válido.");
        }
        this.firstEnrolmentYear = year;
    }

    /**
     * The entry grade is optional and uses the 0–200 scale.
     */
    public void changeEntryGrade(Integer newEntryGrade) {
        if (newEntryGrade != null && (newEntryGrade < MIN_ENTRY_GRADE || newEntryGrade > MAX_ENTRY_GRADE)) {
            throw new IllegalArgumentException("A nota de ingresso tem de estar entre 0 e 200.");
        }
        this.entryGrade = newEntryGrade;
    }


    public StudentStatus getStatus() {
        return status;
    }


    @Override
    public Role getRole() {
        return Role.STUDENT;
    }

    public int getFirstEnrolmentYear() {
        return firstEnrolmentYear;
    }

    public Integer getEntryGrade() {
        return entryGrade;
    }

}
