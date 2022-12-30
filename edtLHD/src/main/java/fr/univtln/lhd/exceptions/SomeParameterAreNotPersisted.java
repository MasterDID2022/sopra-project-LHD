package fr.univtln.lhd.exceptions;

public class SomeParameterAreNotPersisted extends Exception{
    public SomeParameterAreNotPersisted() {
        super("You need to persisted these entity:$ListOfNotPersistedEntities");
    }
    public SomeParameterAreNotPersisted(final String message) {
        super(message);
    }

}
