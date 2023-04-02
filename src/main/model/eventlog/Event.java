package model.eventlog;

import java.util.Calendar;
import java.util.Date;


/**
 * Represents a tank game bullet addition / reset event.
 *
 * ACKNOWLEDGEMENT: THIS CODE HAS BEEN ENTIRELY TAKEN FROM THE Event CLASS OF
 * THE AlarmSystem PROJECT, WHICH CAN BE FOUND BELOW:
 * https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
 * SOME COMMENTS HAVE BEEN CHANGED TO FIT ITS USAGE IN THIS CONTEXT.
 *
 * LAST REVISED: 03/31/2022
 */

public class Event {
    private static final int HASH_CONSTANT = 13;
    private Date dateLogged;
    private String description;

    /**
     * Creates an event with the given description
     * and the current date/time stamp.
     * @param description  a description of the event
     */
    public Event(String description) {
        dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    /**
     * Gets the date of this event (includes time).
     * @return  the date of the event
     */
    public Date getDate() {
        return dateLogged;
    }

    /**
     * Gets the description of this event.
     * @return  the description of the event
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Event otherEvent = (Event) other;

        return (this.dateLogged.equals(otherEvent.dateLogged)
                && this.description.equals(otherEvent.description));
    }

    @Override
    public int hashCode() {
        return (HASH_CONSTANT * dateLogged.hashCode() + description.hashCode());
    }

    @Override
    public String toString() {
        return dateLogged.toString() + "\n" + description;
    }
}
