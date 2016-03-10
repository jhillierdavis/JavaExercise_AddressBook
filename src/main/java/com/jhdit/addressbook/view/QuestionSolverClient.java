package com.jhdit.addressbook.view;

import com.jhdit.addressbook.service.Analyser;
import com.jhdit.addressbook.domain.Contact;
import com.jhdit.addressbook.domain.Gender;
import com.jhdit.addressbook.persistence.CsvFileDataLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Command-line program to provide answers to Address Book exercise questions posed.
 *
 * Example stand-alone execution:
 * ------------------------------
 *
 * gradle run -Dexec.args="./src/test/resources/com/jhdit/addressbook/addressbook.csv"
 *
 * NB: Provided this class is set as 'mainClassName' in Gradle build file.
 *
 * Example output:
 * ---------------
 *
 * 1. How many males are in the address book?
 * Answer: 3
 *
 * 2. Who is the oldest person in the address book?
 * Answer: Wes Jackson
 *
 * 3. How many days older is Bill than Paul?
 * Answer: 2862 days.
 *
 */

public class QuestionSolverClient {
    private Analyser analyser;

    public QuestionSolverClient(final File inputFile) throws IOException   {
        CsvFileDataLoader dataProcessor = new CsvFileDataLoader(inputFile);
        this.analyser = new Analyser( dataProcessor.getContacts() );
    }

    /**
     * Execution entry point
     * @param args CSV Address Book file to process expected as 1st arg
     * @throws IOException If issue processing provided file
     */

    public static void main(final String[] args) throws IOException {
        if (args.length != 1)    {
            printUsageAndExit();
        }

        File inputFile = new File(args[0]);
        QuestionSolverClient solver = new QuestionSolverClient(inputFile);

        solver.displayQuestionAndAnswer("1. How many males are in the address book?", "" + solver.getMaleCount());
        solver.displayQuestionAndAnswer("2. Who is the oldest person in the address book?", solver.getOldest()  );
        solver.displayQuestionAndAnswer("3. How many days older is Bill than Paul?", solver.getDaysDifference("Bill", "Paul")  + " days." );

    }

    // Internal methods

    private static void printUsageAndExit()    {
        System.out.println( "Usage: com.jhdit.addressbook.view.QuestionSolverClient <Adddress Book CSV file>");
        System.exit(0);
    }

    private void displayQuestionAndAnswer(String questionText, String answerText) {
        System.out.println( questionText );
        System.out.println( "Answer: " + answerText );
        System.out.println();
    }

    private int getMaleCount() {
        return this.analyser.filterByGender(Gender.MALE ).size();
    }

    private String getOldest()  {
        List<Contact> sorted = this.analyser.sortByAgeInYears();

        if (null == sorted || sorted.isEmpty())    {
            return "<none>";
        }

        return sorted.get(0).getFullname();
    }

    private long getDaysDifference(String firstNameOne, String firstNameTwo) {
        Optional<Contact> one = analyser.findByFirstName(firstNameOne);
        Optional<Contact> two = analyser.findByFirstName(firstNameTwo);

        if (one.isPresent()  && two.isPresent())    {
            return one.get().getCurrentAgeInDays() - two.get().getCurrentAgeInDays();
        }
        return -1;
    }
}
