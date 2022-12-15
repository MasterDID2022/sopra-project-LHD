package fr.univtln.lhd.exceptions;

public class IdException extends Exception{
    public IdException(String message) {
        super("An ID must be positive");
    }
}
