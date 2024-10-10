package com.ecommerce.jideBrand.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;

public class APIException extends RuntimeException {


    private static final Long serialVersionUID = 1L;

    public APIException(){

    }
    public APIException(String message) {
        super(message );
    }

}
