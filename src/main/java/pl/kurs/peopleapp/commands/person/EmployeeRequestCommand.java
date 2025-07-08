package pl.kurs.peopleapp.commands.person;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.kurs.peopleapp.annotations.PersonSubType;

import java.math.BigDecimal;
import java.time.LocalDate;

@PersonSubType("employee")
public class EmployeeRequestCommand extends PersonRequestCommand {

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
    @NotNull
    private LocalDate startOfEmployment;
    @NotBlank
    private String currentProfession;
    @NotNull
    @Positive
    private BigDecimal currentSalary;

    @Override
    public String getType() {
        return "employee";
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

    public LocalDate getStartOfEmployment() {
        return startOfEmployment;
    }

    public String getCurrentProfession() {
        return currentProfession;
    }

    public BigDecimal getCurrentSalary() {
        return currentSalary;
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

    public void setStartOfEmployment(LocalDate startOfEmployment) {
        this.startOfEmployment = startOfEmployment;
    }

    public void setCurrentProfession(String currentProfession) {
        this.currentProfession = currentProfession;
    }

    public void setCurrentSalary(BigDecimal currentSalary) {
        this.currentSalary = currentSalary;
    }
}
