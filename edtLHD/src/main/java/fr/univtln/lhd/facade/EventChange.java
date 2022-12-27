package fr.univtln.lhd.facade;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventChange<T> {

    public enum ChangeType { ADD, MODIFY, REMOVE, UNDEFINED }

    private ChangeType type;
    private final T data;
}
