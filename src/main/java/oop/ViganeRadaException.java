package oop;

public class ViganeRadaException extends Exception {
    int rida;
    ViganeRadaException(String viga, int rida){
        super(viga);
        this.rida = rida;

    }
}
