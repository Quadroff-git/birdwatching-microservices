package org.pileka.birdwatching_exception_commons.exception;

public class EntityDoesntExistException extends RuntimeException {
    public EntityDoesntExistException(String message) {
        super(message);
    }
}
