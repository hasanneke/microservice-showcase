package com.mentoapp.mentoapp.Exception.Instance;

public class ResourceNotFound extends RuntimeException {
    String errorCode;
    public ResourceNotFound(String message) {
        super(message);
    }

    public ResourceNotFound(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
