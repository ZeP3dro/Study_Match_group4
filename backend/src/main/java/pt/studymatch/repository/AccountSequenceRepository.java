package pt.studymatch.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pt.studymatch.domain.AccountSequence;

public interface AccountSequenceRepository extends JpaRepository<AccountSequence, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sequence from AccountSequence sequence where sequence.id = 1")
    Optional<AccountSequence> findCounterForUpdate();
}
