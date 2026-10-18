package pt.studymatch.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Shared counter for new account numbers. It is stored, so it does not restart when the application stops. */
@Entity
@Table(name = "account_sequence")
public class AccountSequence {
    @Id
    private Long id;
    private long nextValue;

    public AccountSequence() {
        id = 1L;
        nextValue = 1;
    }

    public String takeNextNumber() {
        if (nextValue > 99999999) {
            throw new IllegalArgumentException("Já não há números de conta disponíveis.");
        }
        String number = String.valueOf(nextValue);
        nextValue++;
        return number;
    }
}
