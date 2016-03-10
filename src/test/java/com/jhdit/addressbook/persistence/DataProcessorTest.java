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
 * Unit tests for DataProcessor
 *
 * Example stand-alone execution:
 * ------------------------------
 *
 * gradle test --tests *DataProcessorTest --rerun-tasks
 */

public class DataProcessorTest {

    // Obtain test resource file
    private static final String TEST_CSV_FILE = "/com/jhdit/addressbook/addressbook.csv";


    @Test
    public void canReadTestFileCorrectly() throws IOException {
        // given:
        InputStream is = getClass().getResourceAsStream(TEST_CSV_FILE);

        // then:
        assertNotNull(is);

        // and: display for visual inspection
        BufferedReader br = new BufferedReader(new InputStreamReader(is) );
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("Line: `" + line + "'");
        }
    }

    @Test
    public void resourceFileIsAvailable() throws IOException {
        URL url = this.getClass().getResource(TEST_CSV_FILE);
        assertNotNull(url);

        File resourceFile = new File(url.getFile());
        assertTrue(resourceFile.exists());
    }

    @Test
    public void testContactsProcessed() throws IOException  {
        URL url = this.getClass().getResource(TEST_CSV_FILE);
        assertNotNull(url);

        File resourceFile = new File(url.getFile());
        assertTrue(resourceFile.exists());

        DataProcessor accessor = new DataProcessor( resourceFile );
        Set<Contact> set = accessor.getContacts();

        assertThat(set.size(), is(5));
    }

}
