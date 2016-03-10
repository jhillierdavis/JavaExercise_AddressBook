package com.jhdit.junit;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

import org.junit.runner.RunWith;
import org.junit.rules.ExpectedException;
import junitparams.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import static org.mockito.Matchers.*; // e.g. for anyString()
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.mockito.BDDMockito.*;


/**
 * JUnit 4 verification test (to check unit testing set-up & for reference).
 *
 * Uses JUnitParams see: https://github.com/Pragmatists/junitparams/wiki/Quickstart
 *
 * Execution:
 * ----------
 *
 * gradle test --tests *JUnitVerificationTest --rerun-tasks
 */

@RunWith(JUnitParamsRunner.class)
public class JUnitVerificationTest	{

    @Test
    public void superSimpleLogicExampleBasicMultiplication()  {
        // expect
        assertThat(3 * 7 , is( 21 ));
    }

    @Test
    @Parameters({
            " , 0",
            "Kirk, 4",
            "Spock, 5",
            "Scotty, 6"
    })
    public void nameLength(final String name, final int length) {
        // expect: "matching name length"
        assertThat(name.length(), is(length));
    }

    @Test
    @Parameters({
            // base , exponent , result
            "2, 0, 1" ,
            "2, 1 , 2" ,
            "2 , 2 , 4" ,
            "3 , 2 , 9" ,
            "10 , 2 , 100" ,
            "2 , 10 , 1024" ,
            "2 , -1 , 0.5"

    })
    public void baseRaisedToPowerOfExponentIsResult(final int base, final int exponent, final double result) {
        // expect: "a specific outcome"
        assertThat( Math.pow(base, exponent) , is( result ) );
        // See http://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#pow-double-double-
    }

    @Test(expected = ArithmeticException.class)
    public void exceptionHandlingDivideByZeroExample() {
        // when: "an exception is raised"
        int divideByZero = 1 / 0 ;
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void exceptionHandlingDivideByZeroExampleVerifyMessage() {
        // expect: "the exception is caught & can be verified"
        thrown.expect(ArithmeticException.class);
        thrown.expectMessage("/ by zero");

        // when: "an exception is raised"
        int divideByZero = 1 / 0 ;	// throws java.lang.ArithmeticException: / by zero
    }

    @Ignore("example of how to intentionally omit a test method")
    @Test
    public void ignoreForNow() {
        assertThat(true, is(false));
    }

    @Test(expected= IndexOutOfBoundsException.class)
    public void empty() {
        new ArrayList<Object>().get(0);
    }

    @Test
    public void stackExample()    {
        // setup:
        Deque stack = new ArrayDeque(); // Use as a Stack (favour over old java.util.Stack)
        Date today = new Date();

        // expect:
        assertTrue( stack.isEmpty() );

        // when:
        try	{
            stack.pop();
            fail("NoSuchElementException expected!");
        } catch (NoSuchElementException e)	{
            // Expected
        }

        // and:
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        stack.push( "First entry" );
        stack.push( map ); // Map
        stack.push( Arrays.asList("x", "y", "z") ); // List
        stack.push(today); // Last item

        // then:
        assertTrue( !stack.isEmpty() );
        assertThat( stack.size(), is(4) );
        assertThat( stack.peek(), is(today) );

    }

    /**
     * A Stub is a fake class that can be programmed with custom behavior.
     */

    @Test
    public void stubbingSubscriber() {
        Subscriber stub = mock( Subscriber.class );
        // when( stub.receive(anyString()) ).thenReturn(true);
        when( stub.receive("1st event") ).thenReturn(true);
        when( stub.receive("2nd event") ).thenReturn(false);
        when( stub.receive("3rd event") ).thenReturn(true);

        // expect:
        assertTrue( stub.receive("1st event") );
        assertFalse( stub.receive("2nd event") );
        assertTrue( stub.receive("3rd event") );
    }

	/*
	 * A Mock is a fake class that can be examined (after the test is finished) for its interactions with the class under test 
	 * (which methods were called, what was the arguments etc.)
	 */

    @Test
    public void useMocksToVerifyEventsArePublishedToAllSubscribers() {
        // given: "mocked subscribers"
        Subscriber mockSubscriber = mock(Subscriber.class);
        given( mockSubscriber.receive(anyString()) ).willReturn(true);

        // and:
        Publisher publisher = new Publisher();
        publisher.add( mockSubscriber );

        // when: "an event is published to all subscribers"
        publisher.fire("event");

        // then: "a receive invocation occurs for each listening subscriber"
        then(mockSubscriber).should(times(1)).receive("event");
    }
}

class Subscriber  {
    public boolean receive(String event) {
        System.out.println("Receive" + event);
        return true;
    }
}

class Publisher   {
    private List<Subscriber> subscribers = new ArrayList();

    public void add(final Subscriber subscriber)  {
        this.subscribers.add( subscriber );
    }

    public void fire(final String event) {
        this.subscribers.stream().forEach( t -> {
            if ( !t.receive(event) ) {
                throw new RuntimeException("Subscriber problem!");
            }
        });
    }
}