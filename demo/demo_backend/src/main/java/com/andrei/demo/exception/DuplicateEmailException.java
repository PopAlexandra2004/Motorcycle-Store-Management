package com.andrei.demo.exception;

/**
 * Thrown when attempting to create a person with an already used email.
 */
public class DuplicateEmailException extends Exception {
    public DuplicateEmailException(String message) {
        super(message);
    }
}

