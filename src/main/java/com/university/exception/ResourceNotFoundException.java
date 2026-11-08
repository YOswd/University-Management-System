package com.university.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, int id) {
        super(resource + " with id " + id + " was not found");
    }
}
