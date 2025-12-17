package trenes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;

/**
 *
 * @author juanc
 */
public class Main {

    public static void main(String[] args) {
        TrenesSA app = new TrenesSA();
        Scanner sc = new Scanner(System.in);
        
        app.cargarInicialDesdeArchivo(fileName);
        app.comenzar();
    }
}

