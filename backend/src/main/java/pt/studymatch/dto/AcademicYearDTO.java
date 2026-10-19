package pt.studymatch.dto;

public class AcademicYearDTO {

    private Long id;

    private String label;

    private boolean current;

    public AcademicYearDTO() {
    }

    public AcademicYearDTO(Long id, String label, boolean current) {
        this.id = id;
        this.label = label;
        this.current = current;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

}
