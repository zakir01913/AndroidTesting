package com.zakir.androidtesting;

/**
 * Created by zakir on 27/7/18.
 */

public class Response<T> {

    private Status status;
    private T data;
    private Throwable error;

    private Response(Status status, T data, Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(Status.SUCCESS, data, null);
    }

    public static <T> Response<T> error(Throwable throwable) {
        return new Response<>(Status.ERROR, null, throwable);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public Throwable getError() {
        return error;
    }
}
