package fr.univtln.lhd.exception;

public class IdException extends Exception{
    public IdException(String message) {
        super("An ID must be positive");
    }
}
