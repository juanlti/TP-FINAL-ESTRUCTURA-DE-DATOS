package trenes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.lang.Math;

public class NodoAVL {

    private int altura;
    private Comparable clave;
    private Object dato;
    private NodoAVL izquierdo;
    private NodoAVL derecho;

    public NodoAVL(Comparable clave, Object elemento, NodoAVL izquierdo, NodoAVL derecho) {
        this.dato = elemento;
        this.izquierdo = izquierdo;
        this.derecho = derecho;
        this.clave = clave;
        recalcularAltura();
    }

    public NodoAVL(Comparable clave, Object data) {
        this.clave = clave;
        this.dato = data;
    }

    public Comparable getClave() {
        return this.clave;
    }

    public int getAltura() {
        return this.altura;
    }

    public void recalcularAltura() {
        // en caso de no tener hijos se dará la cuenta -1 + 1 = 0
        this.altura = Math.max((this.izquierdo == null) ? -1 : this.izquierdo.altura, (this.derecho == null) ? -1 : this.derecho.altura) + 1;
    }

    public Object getElemento() {
        return this.dato;
    }

    public void setElemento(Comparable clave,Object elemento) {
        this.clave=clave;
        this.dato = elemento;
    }

    public NodoAVL getIzquierdo() {
        return this.izquierdo;
    }

    public void setIzquierdo(NodoAVL izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoAVL getDerecho() {
        return this.derecho;
    }

    public void setDerecho(NodoAVL derecho) {
        this.derecho = derecho;
    }

    public int calcularBalance() {
        return ((this.izquierdo == null) ? -1 : this.izquierdo.altura) - ((this.derecho == null) ? -1 : this.derecho.altura);
    }

}
