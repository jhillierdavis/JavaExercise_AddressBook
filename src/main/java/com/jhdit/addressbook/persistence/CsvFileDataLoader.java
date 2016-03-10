package com.jhdit.addressbook.persistence;

import com.jhdit.addressbook.domain.Contact;
import com.jhdit.addressbook.domain.Gender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Processes Address Book data from a CSV file & load into a Contact collection.
 */

public class CsvFileDataLoader implements AddressBookData {
    private Set<Contact> set = new HashSet<>();

    // Constants
    private static final int NUMBER_OF_FIELDS = 3;
    private static final String DELIMITOR = ",";

    public CsvFileDataLoader(final File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        for (String line: lines)    {
            processContactLine(line);
        }
    }

    public Set<Contact> getContacts()  {
        return this.set;
    }

    private void processContactLine(String line) {
        String[] fields = line.split(DELIMITOR);

        if (fields.length != NUMBER_OF_FIELDS) {
            throw new IllegalArgumentException("Invalid contact line: " + line);
        }

        String fullname = fields[0].trim();
        String genderValue = fields[1].trim().toUpperCase();
        String dob = fields[2].trim();

        Contact contact = new Contact(fullname, Gender.valueOf( genderValue ), dob);
        set.add( contact );
    }
}
