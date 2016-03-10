package com.jhdit.addressbook.analysis;

import com.jhdit.addressbook.domain.Contact;
import com.jhdit.addressbook.domain.Gender;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for Address Book Analyser.
 *
 * NB: Attempts to emulate Spock framework (& BDD style) via JUnit e.g. using parameters (for data driven approach).
 *
 * Example stand-alone execution:
 * ------------------------------
 *
 * gradle test --tests *AnalyserTest --rerun-tasks
 */

@RunWith(JUnitParamsRunner.class)
public class AnalyserTest {

    @Test
    @Parameters({
            // gender , expectedCount
            "MALE, 2",
            "FEMALE, 3"
    })
    public void filterByGender(String genderValue, int expectedCount)    {
        // given: dummy set of address book contacts
        Gender gender = Gender.valueOf(genderValue);
        Set<Contact> allContacts = getTestContacts();

        // then: verify test data size
        assertThat( allContacts.size(), is(5));

        // when: filtered by gender
        Set<Contact> filteredContacts = (new Analyser(allContacts).filterByGender( gender ));

        // then: is as expected
        assertThat(filteredContacts.size(), is( expectedCount ));
    }

    @Test
    public void sortedByAgeInYears()    {
        // given: dummy set of address book contacts
        Set<Contact> allContacts = getTestContacts();

        // when: sorted by age (in years descending; oldest first)
        List<Contact> filteredContacts = (new Analyser(allContacts).sortByAgeInYears());

        // then: number of entries remains the same (once sorted)
        assertThat( filteredContacts.size(), is(5) ); // Set remains same
       
        // when: find 1st entry
        Optional<Contact> eldest = filteredContacts.stream().findFirst();
        
        // then: is oldest
        assertTrue( eldest.isPresent() );
        assertThat( eldest.get().getFullname(), is("Adele Goldberg") );

        // when: find last entry
        Contact youngest = filteredContacts.get( filteredContacts.size() - 1 );

        // then: is youngest
        assertThat( youngest.getFullname(), is("Ada Lovelace") );

    }

    @Test
    @Parameters({
            // firstname , expectedFullname
            "Alan, Alan Turing",
            "Grace, Grace Hopper"
    })
    public void findByExistingValidFirstname(String firstname, String expectedFullname)   {
        // given: dummy set of address book contacts
        Set<Contact> allContacts = getTestContacts();

        // when: contact sought by first name
        Optional<Contact> optional = (new Analyser(allContacts).findByFirstname(firstname));

        // then: matches an entry
        assertTrue(optional.isPresent());

        // when: match obtained
        Contact match = optional.get();

        // then: is expected entry
        assertThat(match.getFullname(), is(expectedFullname) );
    }


    private Set<Contact> getTestContacts()  {
        Set<Contact> dummyData = new HashSet<>();

        dummyData.add( new Contact( "Charles Babbage", Gender.MALE, "26/12/91" )); // 1791, but will be treated as 1991 for our purposes
        dummyData.add( new Contact( "Ada Lovelace", Gender.FEMALE, "10/12/15") ); // 1815, but will be treated as 2015 for our purposes
        dummyData.add( new Contact( "Grace Hopper", Gender.FEMALE, "09/12/06") );
        dummyData.add( new Contact( "Alan Turing", Gender.MALE, "23/06/12") );
        dummyData.add( new Contact( "Adele Goldberg", Gender.FEMALE, "07/07/45") );

        return dummyData;
    }
}
