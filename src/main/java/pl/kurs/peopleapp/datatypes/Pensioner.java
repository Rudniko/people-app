package pl.kurs.peopleapp.datatypes;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.apache.commons.csv.CSVRecord;
import pl.kurs.peopleapp.annotations.DtoMapping;
import pl.kurs.peopleapp.dtos.PensionerDto;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@DiscriminatorValue("PENSIONER")
@DtoMapping(PensionerDto.class)
public class Pensioner extends Person {

    private BigDecimal pension;
    private Integer yearsWorked;

    public Pensioner() {
    }

    public Pensioner(String pesel, String firstName, String lastName, Double height, Double weight, String email,
                     BigDecimal pension, Integer yearsWorked) {
        super(pesel, firstName, lastName, height, weight, email);
        this.pension = pension;
        this.yearsWorked = yearsWorked;
    }

    @Override
    public void populateSpecificFields(CSVRecord record) {
        this.pension = new BigDecimal(record.get("emerytura"));
        this.yearsWorked = Integer.parseInt(record.get("przepracowane lata"));
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Pensioner pensioner = (Pensioner) o;
        return Objects.equals(pension, pensioner.pension) && Objects.equals(yearsWorked, pensioner.yearsWorked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pension, yearsWorked);
    }
}
