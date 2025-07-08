package pl.kurs.peopleapp.datatypes;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.apache.commons.csv.CSVRecord;
import pl.kurs.peopleapp.annotations.DtoMapping;
import pl.kurs.peopleapp.dtos.StudentDto;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@DiscriminatorValue("STUDENT")
@DtoMapping(StudentDto.class)
public class Student extends Person {

    private String universityName;
    private Integer currentStudyYear;
    private String studyField;
    private BigDecimal scholarship;

    public Student() {
    }

    public Student(String pesel, String firstName, String lastName, Double height, Double weight, String email,
                   String universityName, Integer currentStudyYear, String studyField, BigDecimal scholarship) {
        super(pesel, firstName, lastName, height, weight, email);
        this.universityName = universityName;
        this.currentStudyYear = currentStudyYear;
        this.studyField = studyField;
        this.scholarship = scholarship;
    }

    @Override
    public void populateSpecificFields(CSVRecord record) {
        this.universityName = record.get("uczelnia");
        this.currentStudyYear = Integer.parseInt(record.get("rok studiow"));
        this.studyField = record.get("kierunek studiow");
        this.scholarship = new BigDecimal(record.get("stypendium"));
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(universityName, student.universityName) && Objects.equals(currentStudyYear, student.currentStudyYear) && Objects.equals(studyField, student.studyField) && Objects.equals(scholarship, student.scholarship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), universityName, currentStudyYear, studyField, scholarship);
    }
}
