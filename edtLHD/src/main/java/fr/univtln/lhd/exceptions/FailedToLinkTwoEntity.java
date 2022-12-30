package fr.univtln.lhd.exceptions;

public class FailedToLinkTwoEntity extends Exception{
    public FailedToLinkTwoEntity() {
        super("Might be because one if the entity is not persisted see ID1[$IdOfObject1] and ID2[$IdOfObject2]");
    }
    public FailedToLinkTwoEntity(final String message) {
        super(message);
    }

}
