package pl.kurs.peopleapp.dtos;

import java.math.BigDecimal;

public class PensionerDto extends PersonDto {

    private BigDecimal pension;

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
}
