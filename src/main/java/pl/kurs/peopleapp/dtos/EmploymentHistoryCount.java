package pl.kurs.peopleapp.dtos;

public class EmploymentHistoryCount {
    private final String pesel;
    private final Long count;

    public EmploymentHistoryCount(String pesel, Long count) {
        this.pesel = pesel;
        this.count = count;
    }

    public String getPesel() {
        return pesel;
    }

    public Long getCount() {
        return count;
    }
}

