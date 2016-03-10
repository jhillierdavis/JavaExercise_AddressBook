package com.jhdit.addressbook.persistence;

import com.jhdit.addressbook.domain.Contact;
import org.junit.Test;
import java.io.*;
import java.net.URL;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for CsvFileDataLoader
 *
 * Example stand-alone execution:
 * ------------------------------
 *
 * gradle test --tests *CsvFileDataLoaderTest --rerun-tasks
 */

public class CsvFileDataLoaderTest {
    // Location of test resource file (in CSV format)
    private static final String TEST_CSV_FILE = "/com/jhdit/addressbook/addressbook.csv";

    @Test
    public void canReadTestFileCorrectly() throws IOException {
        // given:
        InputStream is = getClass().getResourceAsStream(TEST_CSV_FILE);

        // then:
        assertNotNull(is);

        // and: display lines for visual inspection
        BufferedReader br = new BufferedReader(new InputStreamReader(is) );
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("Line: '" + line + "'");
        }
    }

    @Test
    public void resourceFileIsAvailable() throws IOException {
        // given: a test AddressBook file
        File resourceFile = getTestFile();

        // then: test file is obtained
        assertTrue(resourceFile != null);
    }

    @Test
    public void testContactsProcessed() throws IOException  {
        // given: a test AddressBook file
        File resourceFile = getTestFile();

        // when: data processed
        CsvFileDataLoader loader = new CsvFileDataLoader( resourceFile );
        Set<Contact> set = loader.getContacts();

        // then: number of contacts is as expected
        assertThat(set.size(), is(5));
    }

    // Internal implementation methods

    private File getTestFile() throws IOException {
        URL url = this.getClass().getResource(TEST_CSV_FILE);
        assertNotNull(url);

        File resourceFile = new File(url.getFile());
        assertTrue(resourceFile.exists());

        return resourceFile;
    }
}
