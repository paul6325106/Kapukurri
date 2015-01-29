package com.example.kapukurri.data;

/**
 * Created by Paul on 29/01/2015.
 */
public class Person {
    private final int personId;
    private final String name;
    private final String relationship;
    private final boolean isEnabled;

    public Person(int personId, String name, String relationship, boolean isEnabled) {
        this.personId = personId;
        this.name = name;
        this.relationship = relationship;
        this.isEnabled = isEnabled;
    }

    public int getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public String getRelationship() {
        return relationship;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
