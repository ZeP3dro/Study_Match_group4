package pt.studymatch.dto;

import java.math.BigDecimal;
import java.util.List;
import pt.studymatch.enums.ScoreSource;

/**
 * value is the number shown in the profile; source says if it is the area average
 * or a cold-start value (university average or entry grade). Both are null when there is no data.
 */
public class AreaScoreDTO {

    private List<EnrolmentDTO> results;

    private Long areaId;

    private String name;

    private String description;

    private BigDecimal value;

    private ScoreSource source;

    private int courseUnitsCount;

    private int totalEcts;

    public AreaScoreDTO() {
    }

    public AreaScoreDTO(
            Long areaId,
            String name,
            String description,
            BigDecimal value,
            ScoreSource source,
            int courseUnitsCount,
            int totalEcts) {
        this.areaId = areaId;
        this.name = name;
        this.description = description;
        this.value = value;
        this.source = source;
        this.courseUnitsCount = courseUnitsCount;
        this.totalEcts = totalEcts;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ScoreSource getSource() {
        return source;
    }

    public void setSource(ScoreSource source) {
        this.source = source;
    }

    public int getCourseUnitsCount() {
        return courseUnitsCount;
    }

    public void setCourseUnitsCount(int courseUnitsCount) {
        this.courseUnitsCount = courseUnitsCount;
    }

    public int getTotalEcts() {
        return totalEcts;
    }

    public void setTotalEcts(int totalEcts) {
        this.totalEcts = totalEcts;
    }

    public List<EnrolmentDTO> getResults() {
        return results;
    }

    public void setResults(List<EnrolmentDTO> results) {
        this.results = results;
    }
}
