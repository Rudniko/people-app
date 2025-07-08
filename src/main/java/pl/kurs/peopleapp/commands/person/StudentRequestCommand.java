package pl.kurs.peopleapp.commands.person;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.kurs.peopleapp.annotations.PersonSubType;

import java.math.BigDecimal;

@PersonSubType("student")
public class StudentRequestCommand extends PersonRequestCommand {


    @PESEL
    @NotBlank
    private String pesel;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @Positive
    private Double height;
    @NotNull
    @Positive
    private Double weight;
    @NotBlank
    private String email;
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

    @Override
    public String getType() {
        return "student";
    }

    @Override
    public String getPesel() {
        return pesel;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWeight() {
        return weight;
    }

    public String getEmail() {
        return email;
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

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
