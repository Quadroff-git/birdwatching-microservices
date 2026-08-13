package org.pileka.birdwatching_exception_commons.exception;

public class ReferredEntityDoesntExistException extends RuntimeException {
    public ReferredEntityDoesntExistException(String message) {
        super(message);
    }
}
