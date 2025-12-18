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
    private Lista estaciones;

    public Linea(String nombre, Lista estaciones) {
        this.nombre = nombre;
        this.estaciones = estaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Lista getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(Lista estaciones) {
        this.estaciones = estaciones;
    }

    @Override
    public String toString() {
        String estaciones = "";
        for (int i = 1; i < this.estaciones.longitud(); i++) {
            estaciones = this.estaciones.recuperar(i) + estaciones;
        }

        return "Linea : " + nombre + "estaciones [ " + estaciones + " ]";

    }

}
