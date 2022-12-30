package fr.univtln.lhd.facade;

public interface Observable {

    void subscribeToEvent(String eventName, Observer observer);
    void unsubscribeToEvent(String eventName, Observer observer);

    void notifyChanges(String eventName, EventChange<?> changes);
}
