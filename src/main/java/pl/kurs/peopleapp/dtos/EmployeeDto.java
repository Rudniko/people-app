package pl.kurs.peopleapp.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeDto extends PersonDto {

    private LocalDate startOfEmployment;

    private String currentProfession;

    private BigDecimal currentSalary;

    private Long numberOfProfessions;

    public LocalDate getStartOfEmployment() {
        return startOfEmployment;
    }

    public void setStartOfEmployment(LocalDate startOfEmployment) {
        this.startOfEmployment = startOfEmployment;
    }

    public String getCurrentProfession() {
        return currentProfession;
    }

    public void setCurrentProfession(String currentProfession) {
        this.currentProfession = currentProfession;
    }

    public BigDecimal getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(BigDecimal currentSalary) {
        this.currentSalary = currentSalary;
    }

    public Long getNumberOfProfessions() {
        return numberOfProfessions;
    }

    public void setNumberOfProfessions(Long numberOfProfessions) {
        this.numberOfProfessions = numberOfProfessions;
    }
}
