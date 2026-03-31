package org.pileka.bird_service.exception;

public class EntityDoesntExistException extends RuntimeException {
    public EntityDoesntExistException(String message) {
        super(message);
    }
}
