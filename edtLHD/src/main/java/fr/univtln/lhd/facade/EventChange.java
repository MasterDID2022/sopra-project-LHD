package fr.univtln.lhd.facade;

import lombok.Getter;

@Getter
public class EventChange<T> {

    public enum ChangeType { ADD, MODIFY, REMOVE, UNDEFINED }

    private ChangeType type;
    private final T data; //might be a list

    private EventChange(ChangeType type, T changes){
        this.type = type;
        this.data = changes;
    }

    public static <T> EventChange<T> of(ChangeType type, T changes) { return new EventChange<>(type, changes); }
}
