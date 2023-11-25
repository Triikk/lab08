package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote {

    private final Map<String, DeathNoteRecord> deathNote;
    private String lastName;

    public DeathNoteImpl(){
        this.deathNote = new HashMap<>();
        this.lastName = "";
    }

    @Override
    public String getRule(int ruleNumber) {
        if(ruleNumber == 0){
            throw new IllegalArgumentException("The rule number must be greater than 0");
        } else if(ruleNumber > RULES.size()){
            throw new IllegalArgumentException("The rule number must be less than the maximum");
        }
        return RULES.get(ruleNumber - 1);
    }

    @Override
    public void writeName(String name) {
        if(name.equals(null)){
            throw new NullPointerException("Name must be not null");
        }
        this.deathNote.put(name, new DeathNoteRecord(name));
        this.lastName = name;
    }

    @Override
    public boolean writeDeathCause(String cause) {
        if(cause.equals(null)){
            throw new IllegalStateException("Cause must not be null");
        } else if(this.deathNote.isEmpty()){
            throw new IllegalStateException("Death note is empty");
        }
        if((System.currentTimeMillis() - this.deathNote.get(lastName).writeNameTime) <= 40){
            this.deathNote.get(lastName).deathCause = cause;
            return true;
        }
        return false;
    }

    @Override
    public boolean writeDetails(String details) {
        if(details.equals(null)){
            throw new IllegalStateException("Details must no be null");
        }
        else if(this.deathNote.isEmpty()){
            throw new IllegalStateException("Death note is empty");
        }
        if((System.currentTimeMillis() - this.deathNote.get(lastName).writeNameTime) <= 6040){
            this.deathNote.get(lastName).deathDetails = details;
            return true;
        }
        return false;
    }

    @Override
    public String getDeathCause(String name) {
        return this.deathNote.get(name).deathCause;
    }

    @Override
    public String getDeathDetails(String name) {
        return this.deathNote.get(name).deathDetails;
    }

    @Override
    public boolean isNameWritten(String name) {
        return deathNote.containsKey(name);
    }
    
    private class DeathNoteRecord {

        private final String name;
        private String deathCause;
        private String deathDetails;
        private final long writeNameTime;

        private DeathNoteRecord(String name){
            this.name = name;
            this.deathCause = "heart attack";
            this.deathDetails = "";
            this.writeNameTime = System.currentTimeMillis();
        }
    }
}
