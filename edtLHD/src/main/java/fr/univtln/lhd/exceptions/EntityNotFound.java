package fr.univtln.lhd.exceptions;

public class EntityNotFound extends Exception{
    public EntityNotFound() {
        super("Might be because the object is not persisted see if id is greater than 0 id[");
    }
    public EntityNotFound(final String message) {
        super(message);
    }

}
