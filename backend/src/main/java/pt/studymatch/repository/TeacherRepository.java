package pt.studymatch.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.studymatch.domain.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    // Pesquisa por parte do número e ordena os resultados pelo número.
    List<Teacher> findByNumberContainingOrderByNumberAsc(String number);
}
