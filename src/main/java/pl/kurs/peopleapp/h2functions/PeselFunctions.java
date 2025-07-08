package pl.kurs.peopleapp.h2functions;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.time.LocalDate;

@Component
@Profile("test")
public class PeselFunctions {

    public static Date pesel_birthdate(String pesel) {
        if (pesel == null || pesel.length() != 11) {
            return null;
        }
        try {
            int year = Integer.parseInt(pesel.substring(0, 2));
            int month = Integer.parseInt(pesel.substring(2, 4));
            int day = Integer.parseInt(pesel.substring(4, 6));

            int centuryCode = month / 20;
            month = month % 20;

            int fullYear = switch (centuryCode) {
                case 0 -> 1900 + year;
                case 1 -> 2000 + year;
                case 2 -> 2100 + year;
                case 3 -> 2200 + year;
                case 4 -> 1800 + year;
                default -> throw new IllegalArgumentException("Invalid pesel");
            };


            LocalDate date = LocalDate.of(fullYear, month, day);
            return Date.valueOf(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String pesel_gender(String pesel) {
        if (pesel == null || pesel.length() != 11) return null;
        try {
            int genderDigit = Character.getNumericValue(pesel.charAt(9));
            return (genderDigit % 2 == 0) ? "F" : "M";
        } catch (Exception e) {
            return null;
        }
    }
}
