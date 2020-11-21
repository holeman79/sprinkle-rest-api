package com.kakaopay.sprinklerestapi.sprinkling.exception;

public class AmountLessThanPeopleCountException extends RuntimeException{
    public AmountLessThanPeopleCountException() {
        super();
    }

    public AmountLessThanPeopleCountException(String message) {
        super(message);
    }

    public AmountLessThanPeopleCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountLessThanPeopleCountException(Throwable cause) {
        super(cause);
    }
}
