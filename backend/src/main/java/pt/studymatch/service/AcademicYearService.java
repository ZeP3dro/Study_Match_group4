package pt.studymatch.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.AcademicYear;
import pt.studymatch.repository.AcademicYearRepository;

@Service
@Transactional(readOnly = true)
public class AcademicYearService {

    private final AcademicYearRepository academicYearRepository;

    public AcademicYearService(AcademicYearRepository academicYearRepository) {
        this.academicYearRepository = academicYearRepository;
    }


    public LocalDate today() {
        return LocalDate.now();
    }

    /**
     * The current academic year, created on demand so the system keeps working in new years.
     */
    @Transactional
    public AcademicYear current() {
        AcademicYear thisYear = AcademicYear.startingIn(AcademicYear.startYearOf(today()));
        return academicYearRepository.findByStartDate(thisYear.getStartDate())
                .orElseGet(() -> academicYearRepository.save(thisYear));
    }
}
