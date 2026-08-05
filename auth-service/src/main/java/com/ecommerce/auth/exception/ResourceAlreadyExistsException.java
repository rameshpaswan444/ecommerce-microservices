package com.ecommerce.auth.exception;

public class ResourceAlreadyExistsException extends RuntimeException{

    public ResourceAlreadyExistsException(String message){
        super(message);
    }
}
