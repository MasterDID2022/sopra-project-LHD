package fr.univtln.lhd.exceptions;

public class FailedToUpdateEntity extends Exception{
    public FailedToUpdateEntity() {
        super("Might be because the object $ClassOfObject is not persisted see if id is greater than 0 id[$IdOfObject] or it might be because the entity failed to satisfied an unicity constraint");
    }
    public FailedToUpdateEntity(final String message) {
        super(message);
    }

}
