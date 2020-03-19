package com.example.topmovies.data;

public class Resource<T> {

    private final T data;
    private final String errorMessage;
    private final Status status;

    protected Resource(Status status, T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data,null);
    }

    public static <T> Resource<T> error(String errorMessage) {
        return new Resource<>(Status.ERROR, null,errorMessage);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null,null);
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Status getStatus() {
        return status;
    }

}
