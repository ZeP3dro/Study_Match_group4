package pt.studymatch.dto;

import java.util.Map;

public class ErrorDTO {

    private String message;

    private Map<String, String> fieldErrors;

    public ErrorDTO() {
    }

    public ErrorDTO(String message, Map<String, String> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

}
