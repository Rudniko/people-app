package pl.kurs.peopleapp.dtos;

import java.math.BigDecimal;

public class StudentDto extends PersonDto {

    private String universityName;

    private Integer currentStudyYear;

    private String studyField;

    private BigDecimal scholarship;

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public Integer getCurrentStudyYear() {
        return currentStudyYear;
    }

    public void setCurrentStudyYear(Integer currentStudyYear) {
        this.currentStudyYear = currentStudyYear;
    }

    public String getStudyField() {
        return studyField;
    }

    public void setStudyField(String studyField) {
        this.studyField = studyField;
    }

    public BigDecimal getScholarship() {
        return scholarship;
    }

    public void setScholarship(BigDecimal scholarship) {
        this.scholarship = scholarship;
    }
}
