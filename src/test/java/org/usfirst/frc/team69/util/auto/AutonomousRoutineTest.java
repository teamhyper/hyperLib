package org.usfirst.frc.team69.util.auto;

import static org.junit.Assert.*;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.usfirst.frc.team69.util.CommandBuilder;

public class AutonomousRoutineTest {

    AutonomousRoutine unnamed1, unnamed2, foo1, foo2, bar1;
    Bar bar2;
    
    class Bar extends AutonomousRoutine {
        @Override
        public void build(CommandBuilder builder) {
        }
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    public AutonomousRoutine makeNewRoutine() {
        return new AutonomousRoutine() {
            @Override
            public void build(CommandBuilder builder) {   
            }
        };
    }
    
    @Before
    public void setUp() throws Exception {
        unnamed1 = makeNewRoutine();
        unnamed2 = makeNewRoutine();
        foo1 = makeNewRoutine();
        foo2 = makeNewRoutine();
        bar1 = makeNewRoutine();
        bar2 = new Bar();
        
        foo1.setName("Foo");
        foo2.setName("Foo");
        bar1.setName("Bar");
        
        foo1.addDoublePreference("bizz");
        foo1.addDoublePreference("buzz");
        foo1.addSubroutine(bar1);
        foo1.addSubroutine(bar2);
        foo1.addSubroutine(unnamed1);
    }

    @Test
    public void testHashCode() {
        assertEquals(foo1.hashCode(), foo2.hashCode());
        assertEquals(bar1.hashCode(), bar2.hashCode());
    }

    @Test
    public void testGetName() {
        assertEquals("<unnamed routine>", unnamed1.getName());
        assertEquals("Foo", foo1.getName());
        assertEquals("Bar", bar2.getName());
    }

    @Test
    public void testGetSupportedPreferences() {
        AutonomousPreference bizzPref = new AutonomousPreference(foo1, "bizz");
        AutonomousPreference buzzPref = new AutonomousPreference(foo2, "buzz");
        assertThat(foo1.getSupportedPreferences(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(bizzPref, buzzPref));
    }
    
    @Test
    public void testEqualsObject() {
        // Objects should equal themselves
        assertEquals(foo1, foo1);
        assertEquals(unnamed1, unnamed1);
        // These should all be different
        assertNotEquals(foo1, bar1);
        assertNotEquals(unnamed1, foo1);
        assertNotEquals(unnamed1, unnamed2);
        // But if they have the same name they are the same
        assertEquals(foo1, foo2);
        assertEquals(bar1, bar2);
    }

}
