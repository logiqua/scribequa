package com.javax0.logiqua.jsonlogic;

public class JsonLogicException extends Exception {

    private String path;

    private JsonLogicException() {
        // The default constructor should not be called for exceptions. A reason must be provided.
    }

    public JsonLogicException(String msg, String path) {
        super(msg);
        this.path = path;
    }

    public JsonLogicException(Throwable cause, String path) {
        super(cause);
        this.path = path;
    }

    public JsonLogicException(String msg, Throwable cause, String path) {
        super(msg, cause);
        this.path = path;
    }

    public String getJsonPath() {
        return path;
    }
}