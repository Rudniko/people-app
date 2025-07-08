package pl.kurs.peopleapp.commands.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import pl.kurs.peopleapp.annotations.PersonSubType;

import java.math.BigDecimal;

@PersonSubType("student")
public class StudentRequestCommand extends PersonRequestCommand {

    @NotBlank
    private String universityName;
    @NotNull
    @Positive
    private Integer currentStudyYear;
    @NotBlank
    private String studyField;
    @NotNull
    @PositiveOrZero
    private BigDecimal scholarship;


    public String getType() {
        return "student";
    }

    @Override
    public String getPesel() {
        return super.getPesel();
    }

    @Override
    public void setType(String type) {
        super.setType(type);
    }

    @Override
    public void setPesel(String pesel) {
        super.setPesel(pesel);
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    @Override
    public Double getHeight() {
        return super.getHeight();
    }

    @Override
    public void setHeight(Double height) {
        super.setHeight(height);
    }

    @Override
    public Double getWeight() {
        return super.getWeight();
    }

    @Override
    public void setWeight(Double weight) {
        super.setWeight(weight);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public void setCurrentStudyYear(Integer currentStudyYear) {
        this.currentStudyYear = currentStudyYear;
    }

    public void setStudyField(String studyField) {
        this.studyField = studyField;
    }

    public void setScholarship(BigDecimal scholarship) {
        this.scholarship = scholarship;
    }

    public String getUniversityName() {
        return universityName;
    }

    public Integer getCurrentStudyYear() {
        return currentStudyYear;
    }

    public String getStudyField() {
        return studyField;
    }

    public BigDecimal getScholarship() {
        return scholarship;
    }

}
