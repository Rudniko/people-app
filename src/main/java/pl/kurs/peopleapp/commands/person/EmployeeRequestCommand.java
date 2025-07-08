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

    @NotNull
    private LocalDate startOfEmployment;
    @NotBlank
    private String currentProfession;
    @NotNull
    @Positive
    private BigDecimal currentSalary;

    public String getType() {
        return "employee";
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

    public LocalDate getStartOfEmployment() {
        return startOfEmployment;
    }

    public String getCurrentProfession() {
        return currentProfession;
    }

    public BigDecimal getCurrentSalary() {
        return currentSalary;
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
