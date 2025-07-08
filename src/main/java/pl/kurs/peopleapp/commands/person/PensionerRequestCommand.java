package pl.kurs.peopleapp.commands.person;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.pl.PESEL;
import pl.kurs.peopleapp.annotations.PersonSubType;

import java.math.BigDecimal;

@PersonSubType("pensioner")
public class PensionerRequestCommand extends PersonRequestCommand {

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
    @PositiveOrZero
    private BigDecimal pension;
    @NotNull
    @Positive
    private Integer yearsWorked;

    @Override
    public String getType() {
        return "pensioner";
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

    public BigDecimal getPension() {
        return pension;
    }

    public Integer getYearsWorked() {
        return yearsWorked;
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

    public void setPension(BigDecimal pension) {
        this.pension = pension;
    }

    public void setYearsWorked(Integer yearsWorked) {
        this.yearsWorked = yearsWorked;
    }
}
