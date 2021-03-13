package org.hyperonline.hyperlib.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

/**
 * 
 * @author James
 *
 */
public class AutonomousPreferenceTest {

    class MyRoutine extends AutonomousRoutine {
        @Override
        public Command getCommand() {
        	return new InstantCommand();
        }
    }

    class OtherRoutine extends AutonomousRoutine {
        @Override
        public Command getCommand() {
        	return new InstantCommand();
        }
    }

    AutonomousRoutine myRtn1, myRtn2, otherRtn;
    AutonomousPreference myPref1, myPref2, myPref3, otherPref, yetAnotherPref;

    /**
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        myRtn1 = new MyRoutine();
        myRtn2 = new MyRoutine();
        otherRtn = new OtherRoutine();
        myPref1 = new AutonomousPreference(myRtn1, "myPref");
        myPref2 = new AutonomousPreference(myRtn2, "myPref");
        myPref3 = new AutonomousPreference(myRtn1, "myPref");
        otherPref = new AutonomousPreference(myRtn1, "otherPref");
        yetAnotherPref = new AutonomousPreference(otherRtn, "myPref");
    }

    /**
     * Test that equal preference objects are equal
     */
    @Test
    public void testEqualsObject() {
        assertEquals(myPref1, myPref1);
        assertEquals(myPref1, myPref2);
        assertEquals(myPref1, myPref3);
        assertNotEquals(myPref1, otherPref);
        assertNotEquals(myPref1, yetAnotherPref);
    }

    /**
     * Test that equal preferences have equal hashcodes
     */
    @Test
    public void testHashCode() {
        assertEquals(myPref1.hashCode(), myPref2.hashCode());
        assertEquals(myPref1.hashCode(), myPref3.hashCode());
    }
}
