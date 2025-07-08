package pl.kurs.peopleapp.commands.person;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import pl.kurs.peopleapp.annotations.PersonSubType;

import java.math.BigDecimal;

@PersonSubType("pensioner")
public class PensionerRequestCommand extends PersonRequestCommand {

    @NotNull
    @PositiveOrZero
    private BigDecimal pension;
    @NotNull
    @Positive
    private Integer yearsWorked;

    public BigDecimal getPension() {
        return pension;
    }

    public void setPension(BigDecimal pension) {
        this.pension = pension;
    }

    public Integer getYearsWorked() {
        return yearsWorked;
    }

    public void setYearsWorked(Integer yearsWorked) {
        this.yearsWorked = yearsWorked;
    }

    @Override
    public String getPesel() {
        return super.getPesel();
    }

    @Override
    public String getType() {
        return "pensioner";
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
}
