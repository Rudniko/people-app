package pl.kurs.peopleapp.dtos;


import java.math.BigDecimal;
import java.time.LocalDate;

public class EmploymentDto {

    private Long id;
    private String profession;
    private BigDecimal salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private String employeePesel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getEmployeePesel() {
        return employeePesel;
    }

    public void setEmployeePesel(String employeePesel) {
        this.employeePesel = employeePesel;
    }
}
