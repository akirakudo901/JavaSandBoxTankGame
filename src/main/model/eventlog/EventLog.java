package model.eventlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a log of tank game bullet addition / reset events.
 * We use the Singleton Design Pattern to ensure that there is only
 * one EventLog in the system and that the system has global access
 * to the single instance of the EventLog.
 *
 * ACKNOWLEDGEMENT: THIS CODE HAS BEEN ENTIRELY TAKEN FROM THE EventLog CLASS OF
 * THE AlarmSystem PROJECT, WHICH CAN BE FOUND BELOW:
 * https://github.students.cs.ubc.ca/CPSC210/AlarmSystem
 * SOME COMMENTS HAVE BEEN CHANGED TO FIT ITS USAGE IN THIS CONTEXT.
 *
 * LAST REVISED: 03/31/2022
 */

public class EventLog implements Iterable<Event> {
    /** the only EventLog in the system (Singleton Design Pattern) */
    private static EventLog theLog;
    private Collection<Event> events;

    /**
     * Prevent external construction.
     * (Singleton Design Pattern).
     */
    private EventLog() {
        events = new ArrayList<>();
    }

    /**
     * Gets instance of EventLog - creates it
     * if it doesn't already exist.
     * (Singleton Design Pattern)
     * @return  instance of EventLog
     */
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }
        return theLog;
    }

    /**
     * Adds an event to the event log.
     * @param e the event to be added
     */
    public void logEvent(Event e) {
        events.add(e);
    }

    /**
     * Clears the event log and logs the event.
     */
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}