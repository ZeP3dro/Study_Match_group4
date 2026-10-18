package pt.studymatch.controller;

/**
 * Role expressions used with @PreAuthorize.
 */
final class AccessRules {

    static final String ADMINISTRATOR_ONLY = "hasRole('ACADEMIC_ADMINISTRATOR')";

    private AccessRules() {
    }
}
