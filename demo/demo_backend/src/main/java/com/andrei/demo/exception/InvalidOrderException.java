package com.andrei.demo.exception;

/**
 * Thrown when an order is submitted with invalid data (e.g., empty motorcycle list).
 */
public class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}
