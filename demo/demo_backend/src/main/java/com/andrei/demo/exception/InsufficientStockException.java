package com.andrei.demo.exception;

/**
 * Thrown when the requested motorcycle quantity exceeds availability.
 */
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}
