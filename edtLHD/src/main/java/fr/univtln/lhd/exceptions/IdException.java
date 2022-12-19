package fr.univtln.lhd.exceptions;

public class IdException extends Exception{
    public IdException() {
        super("An ID must be positive");
    }
    public IdException(final String message) {
        super(message);
    }

}
