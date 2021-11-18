package com.splineguy.junit.quotient;

public class MyClass {
    public int quotient(int x, int y){
        if(y==0){
            throw new IllegalArgumentException("Y nem lehet NuLlAAAA");
        }
        return x/y;
    }
}
