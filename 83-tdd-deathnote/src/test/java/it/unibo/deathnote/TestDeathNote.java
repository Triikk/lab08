package it.unibo.deathnote;

import it.unibo.deathnote.impl.DeathNoteImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TestDeathNote {

    DeathNoteImpl deathNote;

    @BeforeEach
    public void setUp(){
        this.deathNote = new DeathNoteImpl();
    }

    @Test
    public void testGetRule(){
        try {
            this.deathNote.getRule(0);
            fail("Getting rule 0 did not throw any exception");
        } catch (IllegalArgumentException e) {
            assertEquals("The rule number must be greater than 0", e.getMessage());
        }
        
        try {
            this.deathNote.getRule(DeathNoteImpl.RULES.size()+1);
            fail("Getting rule above the maximum did not throw any exception");
        } catch (IllegalArgumentException e) {
            assertEquals("The rule number must be less than the maximum", e.getMessage());
        }
    }

    @Test
    public void testRules(){
        assertNotNull(DeathNoteImpl.RULES, "Rules list is null");
        for(String rule : DeathNoteImpl.RULES){
            assertNotNull(rule, "A rule is null");
            assertNotEquals("", rule, "A rule is blank");
        }
    }

    @Test
    public void testWriteName(){
        String name = "Mattia Ronchi";
        assertFalse(this.deathNote.isNameWritten(name));
        this.deathNote.writeName(name);
        assertTrue(this.deathNote.isNameWritten(name));
        assertFalse(this.deathNote.isNameWritten("Fabrizio"));
        assertFalse(this.deathNote.isNameWritten(""));
    }

    @Test
    public void testWriteDeathCause1() throws InterruptedException {
        try {
            this.deathNote.writeDeathCause("bla bla bla");
            fail("Writing a death cause before writing name did not throw any exception");
        } catch(IllegalStateException e){
            assertEquals("Death note is empty", e.getMessage());
        }

        String name = "Mattia Ronchi";
        this.deathNote.writeName(name);
        assertEquals("heart attack", this.deathNote.getDeathCause(name));

        name = "Mattia Ronchi 2";
        this.deathNote.writeName(name);
        String deathCause = "karting accident";
        assertTrue(this.deathNote.writeDeathCause(deathCause), "The cause was not written within 40ms");
        assertEquals(deathCause, this.deathNote.getDeathCause(name));
        Thread.sleep(100);
        this.deathNote.writeDeathCause("heart attack");
        assertEquals(deathCause, this.deathNote.getDeathCause(name));
    }

    @Test
    public void testWriteDeathCause2() throws InterruptedException{
        try {
            this.deathNote.writeDetails("bla bla bla details");
            fail("Writing death's details before writing name did not throw any exception");
        } catch(IllegalStateException e){
            assertEquals("Death note is empty", e.getMessage());
        }

        String name = "Mattia Ronchi";
        this.deathNote.writeName(name);
        assertEquals("", this.deathNote.getDeathDetails(name), name + "'s death details are not empty");
        String deathDetails = "ran for too long";
        assertTrue(this.deathNote.writeDetails(deathDetails), "Death details were not written within 6 seconds and 40 milliseconds");
        assertEquals(deathDetails, this.deathNote.getDeathDetails(name));

        name = "Mattia Ronchi 2";
        this.deathNote.writeName(name);
        Thread.sleep(6100);
        this.deathNote.writeDetails("another detail");
        assertEquals("", this.deathNote.getDeathDetails(name));
    }
    
}
