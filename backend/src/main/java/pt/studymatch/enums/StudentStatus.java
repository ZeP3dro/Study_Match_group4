package pt.studymatch.enums;

public enum StudentStatus {
    ACTIVE,
    SUSPENDED,
    GRADUATED,
    DROPPED_OUT;

    /**
     * UC04: a graduated student cannot go back to Active.
     */
    public boolean canChangeTo(StudentStatus newStatus) {
        if (newStatus == null || newStatus == this) {
            return false;
        }
        return !(this == GRADUATED && newStatus == ACTIVE);
    }
}
