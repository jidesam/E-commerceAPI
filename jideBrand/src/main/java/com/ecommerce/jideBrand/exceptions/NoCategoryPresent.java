package com.ecommerce.jideBrand.exceptions;

public class NoCategoryPresent extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public  NoCategoryPresent(){

    }

    public NoCategoryPresent(String message){
        super(message);
    }
}
