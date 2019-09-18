package com.example.lab2;

public class Converter {

    public static double feh2Cel(double value){

        double result = (value-32)*5/9;
        return result;

    }

    public static double cel2Feh(double value){

        double result = (value/5*9)+32;
        return result;

    }

}
