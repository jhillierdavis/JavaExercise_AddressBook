package com.jhdit.addressbook.persistence;

import com.jhdit.addressbook.domain.Contact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
    private static final String EMPTY_CSV_FILE = "/com/jhdit/addressbook/empty.csv";

    @Test
    public void canReadValidDataFileCorrectly() throws IOException {
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
        File resourceFile = getValidTestFile();

        // then: test file is obtained
        assertTrue(resourceFile != null);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void invalidDataFileHandled() throws IOException  {
        // expect: "the exception is caught & can be verified"
        thrown.expect( IllegalArgumentException.class );
        thrown.expectMessage( "Invalid parameter: file: NULL" );

        // when: data processed
        new CsvFileDataLoader( null );
    }

    @Test
    public void nonExistentDataFileHandled() throws IOException  {
        // given
        File nonExistentFile = new File("/tmp/bogus_non_existent.csv");
        assertFalse(nonExistentFile.exists() );

        // expect: "the exception is caught & can be verified"
        thrown.expect( NoSuchFileException.class );

        // when: data processed
        new CsvFileDataLoader( nonExistentFile );
    }

    @Test
    public void emptyDataProcessedWithoutIssue() throws IOException  {
        // given: a test AddressBook file
        File resourceFile = getEmptyTestFile();

        // when: data processed using empty CSV file
        CsvFileDataLoader loader = new CsvFileDataLoader( resourceFile );
        Set<Contact> set = loader.getContacts();

        // then: number of contacts is as expected
        assertThat(set.size(), is(0));
    }

    @Test
    public void validDataFileProcessedAndContactsLoaded() throws IOException  {
        // given: a test AddressBook file
        File resourceFile = getValidTestFile();

        // when: data processed
        CsvFileDataLoader loader = new CsvFileDataLoader( resourceFile );
        Set<Contact> set = loader.getContacts();

        // then: number of contacts is as expected
        assertThat(set.size(), is(5));
    }

    // Internal implementation methods

    private File getValidTestFile() throws IOException {
        return this.getTestFile(TEST_CSV_FILE);
    }

    private File getEmptyTestFile() throws IOException {
        return this.getTestFile(EMPTY_CSV_FILE);
    }


    private File getTestFile(String filename) throws IOException {
        URL url = this.getClass().getResource(filename);
        assertNotNull(url);

        File resourceFile = new File(url.getFile());
        assertTrue(resourceFile.exists());

        return resourceFile;
    }

}
