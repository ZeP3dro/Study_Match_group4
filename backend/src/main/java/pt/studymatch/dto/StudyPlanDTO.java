package pt.studymatch.dto;

import java.util.List;

/** Summary of the study plan. Index 0 of ectsByYear is the 1st curricular year. */
public class StudyPlanDTO {
    private final String programmeName;
    private final int durationYears;
    private final int totalEcts;
    private final List<Integer> ectsByYear;

    public StudyPlanDTO(String programmeName, int durationYears, int totalEcts, List<Integer> ectsByYear) {
        this.programmeName = programmeName;
        this.durationYears = durationYears;
        this.totalEcts = totalEcts;
        this.ectsByYear = ectsByYear;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public int getTotalEcts() {
        return totalEcts;
    }

    public int getMaxEctsPerCurricularYear() {
        return 60;
    }

    public List<Integer> getEctsByYear() {
        return ectsByYear;
    }
}
