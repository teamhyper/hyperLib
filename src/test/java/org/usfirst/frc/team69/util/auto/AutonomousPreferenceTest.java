package org.usfirst.frc.team69.util.auto;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.usfirst.frc.team69.util.CommandBuilder;

public class AutonomousPreferenceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    class MyRoutine extends AutonomousRoutine {
        @Override
        public void build(CommandBuilder builder) {
        }
    }
    
    class OtherRoutine extends AutonomousRoutine {
        @Override
        public void build(CommandBuilder builder) {
        }
    }
    
    AutonomousRoutine myRtn1, myRtn2, otherRtn;
    AutonomousPreference myPref1, myPref2, myPref3, otherPref, yetAnotherPref;
    
    @Before
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

    @Test
    public void testEqualsObject() {
        assertEquals(myPref1, myPref1);
        assertEquals(myPref1, myPref2);
        assertEquals(myPref1, myPref3);
        assertNotEquals(myPref1, otherPref);
        assertNotEquals(myPref1, yetAnotherPref);
    }
}
