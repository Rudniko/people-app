package pl.kurs.peopleapp.commands.employment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmploymentCreateCommand {

    @NotBlank
    private String profession;
    @NotNull
    @PositiveOrZero
    private BigDecimal salary;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotBlank
    private String employeePesel;

    public String getProfession() {
        return profession;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getEmployeePesel() {
        return employeePesel;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setEmployeePesel(String employeePesel) {
        this.employeePesel = employeePesel;
    }
}
