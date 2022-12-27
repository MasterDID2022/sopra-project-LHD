package fr.univtln.lhd.facade;

import java.util.List;

public interface Observable {

    void subscribe(String eventName, Observer observer);
    void unsubscribe(String eventName, Observer observer);
    void notifyChanges(String eventName, List<EventChange<?>> changes);
}
