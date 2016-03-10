package com.jhdit.addressbook.service;

import com.jhdit.addressbook.domain.Contact;
import com.jhdit.addressbook.domain.Gender;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.Comparator.comparing;

/**
 * Analyser service for Contacts (e.g. obtained from an Address Book)
 *
 * NB: Requires Java 8 (since uses Streams & Lambda functions)
 */

public class Analyser {
    Set<Contact> contacts;

    public Analyser(final Set<Contact> contacts) {
        this.contacts = contacts;
    }


    public  Set<Contact> filterByGender(final Gender gender)   {
        if ( null == gender ) {
            throw new IllegalArgumentException( "Invalid parameter: gender: NULL" );
        }

        Set<Contact> filteredSet = contacts.stream().filter(c -> c.getGender().equals(gender)).collect( toSet() );
        return filteredSet;
    }

    public List<Contact> sortByAgeInYears() {
        List<Contact> sorted = contacts.stream().sorted(comparing(Contact::getCurrentAgeInYears, reverseOrder())).collect( toList() );
        return sorted;
    }

    public Optional<Contact> findByFirstName(final String firstName)   {
        String normalisedFirstName = toNormalisedFirstName(firstName);

        return contacts.stream().filter(c -> c.getFullname().toLowerCase().startsWith( normalisedFirstName )).findFirst();
    }

    // Internal implementations methods

    private String toNormalisedFirstName(final String firstName) {
        if (null == firstName)  {
            throw new IllegalArgumentException( "Invalid parameter: firstName: NULL" );
        }

        String normalisedFirstName = firstName.trim().toLowerCase();
        if (normalisedFirstName.isEmpty()) {
            throw new IllegalArgumentException("Invalid parameter: firstName: " + firstName);
        }
        return normalisedFirstName;
    }
}
