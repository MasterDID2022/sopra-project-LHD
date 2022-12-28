package fr.univtln.lhd.facade;

import java.util.List;

public interface Observable {

    void subscribeToEvent(String eventName, Observer observer);
    void unsubscribeToEvent(String eventName, Observer observer);
    void notifyChanges(String eventName, List<EventChange<?>> changes);
}
