package com.jhdit.addressbook.domain;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for Contact
 *
 * Example stand-alone execution:
 * ------------------------------
 *
 * gradle test --tests *ContactTest --rerun-tasks
 */

@RunWith(JUnitParamsRunner.class)
public class ContactTest {
    private static final long NON_LEAP_YEAR_IN_DAYS = 365;

    @Test
    @Parameters({
            // fullname , gender , dob
            "Barek Obama, MALE, 04/08/61",
            "Hilary Clinton, FEMALE, 26/08/47"
    })
    public void testValidInstantiation(String fullname, String genderValue, String dob) {
        // given
        Gender gender = Gender.valueOf( genderValue );
        Contact contact = new Contact(fullname, gender, dob );

        // then
        assertThat( contact.getFullname(), is( fullname ) );
        assertThat(contact.getGender().toString(), is( genderValue ) );
        assertThat( contact.getDob(), is( dob ) );
    }

    @Test
    @Parameters({
            // fullname , gender , dob, expectedAge
            "Barek Obama, MALE, 04/08/61, 38",
            "Hilary Clinton, FEMALE, 26/08/47, 52"
    })
    public void testDerivedAge(String fullname, String genderValue, String dob, long expectedAge)    {
        // given: valid contact info.
        Gender gender = Gender.valueOf( genderValue );
        Contact contact = new Contact(fullname, gender, dob );

        // then
        LocalDate millenium = LocalDate.of(2000, 1, 1);
        assertThat(contact.getAgeInYearsAt(millenium), is(expectedAge));
    }

    @Test
    public void testDifferenceInDays()  {
        // given: Two contacts born exactly a year apart
        Contact dee = new Contact("Dweedle Dee", Gender.MALE, "01/04/05");
        Contact dum = new Contact("Dweedle Dum", Gender.MALE, "01/04/06");

        // when:
        long differenceInDays =  dee.getCurrentAgeInDays() - dum.getCurrentAgeInDays();

        // then:
        assertThat(differenceInDays, is( NON_LEAP_YEAR_IN_DAYS ));
    }

}
