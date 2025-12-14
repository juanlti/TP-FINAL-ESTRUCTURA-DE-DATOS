/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trenes;

/**
 *
 * @author juanc
 */
public class Linea {

    private String nombre;
    private String[] estaciones;

    public  Linea(String nombre, String[] estaciones) {
        this.nombre = nombre;
        this.estaciones = estaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String[] getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(String[] estaciones) {
        this.estaciones = estaciones;
    }

    @Override
    public String toString() {
        String estaciones = "";
        for (int i = 0; i < this.estaciones.length; i++) {
            estaciones = this.estaciones[i] + " , " + estaciones;
        }
        return "Linea : " + nombre + "estaciones [ " + estaciones + " ]";
    }

}
