package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class AmountLessThanCountException extends RuntimeException{
    public AmountLessThanCountException() {
        super();
    }

    public AmountLessThanCountException(String message) {
        super(message);
    }

    public AmountLessThanCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountLessThanCountException(Throwable cause) {
        super(cause);
    }
}
