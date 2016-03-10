package com.jhdit.addressbook.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Represents an Address Book contact (i.e. details of a person)
 */


public class Contact {
    private final String fullname;
    private final Gender gender;
    private final String dob;
    private final LocalDate dateOfBirth;

    // Constants
    private static final String DATE_FORMAT = "dd/MM/yy";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final int YEARS_PER_CENTURY = 100;


    public Contact(final String fullName, final Gender gender, final String dob)    {

        this.fullname = fullName;
        this.gender = gender;
        this.dob = dob;
        this.dateOfBirth = toDateOfBirth( this.dob );
    }

    public String getFullname() {
        return fullname;
    }

    public Gender getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public long getCurrentAgeInYears()   {
        return this.getAgeInYearsAt( LocalDate.now() );
    }

    public long getAgeInYearsAt(final LocalDate targetDate)    {
        // return Period.between( this.dateOfBirth, targetDate ).getYears();
        return ChronoUnit.YEARS.between( this.dateOfBirth, targetDate );
    }

    public long getCurrentAgeInDays()   {
        return this.getAgeInDaysAt( LocalDate.now() );
    }

    public long getAgeInDaysAt(final LocalDate targetDate)    {
        return ChronoUnit.DAYS.between( this.dateOfBirth, targetDate );
    }

    // Internal methods

    private LocalDate toDateOfBirth(final String dob)   {
        LocalDate dateOfBirth = LocalDate.parse(dob, DATE_TIME_FORMATTER);
        return correctForRelevantCentury( dateOfBirth );
    }

    private LocalDate correctForRelevantCentury(LocalDate dateOfBirth)   {
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isBefore(dateOfBirth))  {
            return dateOfBirth.minusYears(YEARS_PER_CENTURY); // Previous century
        }
        return dateOfBirth;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "fullname='" + fullname + '\'' +
                ", gender=" + gender +
                ", dob='" + dob + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
