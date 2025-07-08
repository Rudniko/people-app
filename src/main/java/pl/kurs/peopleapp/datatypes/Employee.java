package pl.kurs.peopleapp.datatypes;

import jakarta.persistence.*;
import org.apache.commons.csv.CSVRecord;
import pl.kurs.peopleapp.annotations.DtoMapping;
import pl.kurs.peopleapp.dtos.EmployeeDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Entity
@DiscriminatorValue("EMPLOYEE")
@DtoMapping(EmployeeDto.class)
public class Employee extends Person {

    private LocalDate startOfEmployment;
    private String currentProfession;
    private BigDecimal currentSalary;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Set<Employment> employments = new HashSet<>();

    public Employee() {
    }

    public Employee(String pesel, String firstName, String lastName, Double height, Double weight, String email,
                    LocalDate startOfEmployment, String currentProfession, BigDecimal currentSalary) {
        super(pesel, firstName, lastName, height, weight, email);
        this.startOfEmployment = startOfEmployment;
        this.currentProfession = currentProfession;
        this.currentSalary = currentSalary;
    }

    public void addEmployment(Employment emp) {
        emp.setEmployee(this);
        employments.add(emp);
    }

    @Override
    public void populateSpecificFields(CSVRecord record) {
        this.startOfEmployment = LocalDate.parse(record.get("data zatrudnienia"));
        this.currentProfession = record.get("stanowisko");
        this.currentSalary = new BigDecimal(record.get("pensja"));
    }

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

    public Set<Employment> getEmployments() {
        return employments;
    }

    public void setEmployments(Set<Employment> employments) {
        this.employments = employments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(startOfEmployment, employee.startOfEmployment) && Objects.equals(currentProfession, employee.currentProfession) && Objects.equals(currentSalary, employee.currentSalary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startOfEmployment, currentProfession, currentSalary);
    }
}
