package fr.univtln.lhd.exceptions;

public class FailedToSaveEntity extends Exception{
    public FailedToSaveEntity() {
        super("Might be because the entity failed to satisfied an unicity constraint");
    }
    public FailedToSaveEntity(final String message) {
        super(message);
    }

}
