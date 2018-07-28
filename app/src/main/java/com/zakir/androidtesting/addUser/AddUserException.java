package com.zakir.androidtesting.addUser;

/**
 * Created by zakir on 27/7/18.
 */

public class AddUserException extends Throwable{
    ErrorCode errorCode = ErrorCode.NONE;

    public enum ErrorCode {
        NONE,
        EMPTY_LAST_NAME, INVALID_EMAIL, EMPTY_FIRST_NAME
    }

    public AddUserException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
