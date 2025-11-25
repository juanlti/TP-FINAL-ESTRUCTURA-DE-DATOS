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
public class Estacion {

    private String nombre;
    private String calle;
    private int numero;
    private String ciudad;
    private String cp;
    private int vias;
    private int plataformas;

    public Estacion(String nombre, String calle, int numero, String ciudad, String cp, int vias, int plataformas) {
        this.nombre = nombre;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.cp = cp;
        this.vias = vias;
        this.plataformas = plataformas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    @Override
    public String toString() {
        return nombre + " (" + ciudad + ") - " + calle + " " + numero + " CP:" + cp
                + " | vías: " + vias + ", plataformas: " + plataformas;
    }

    public void getEstacion(int idEstacion) {
        Object estacion = null;

    }

    public void setCiudad(String data) {
        this.ciudad=data;
    }

    public void setCodigoPostal(String data) {
             this.cp=data;
    }

    public void setCalle(String data) {
             this.calle=data;
    }

    public void setNumero(int data) {
             this.numero=data;
    }

    /*
     
                bajaEstacion(in);
            } else if ("3".equals(op)) {
                modificarEstacion(in);
            } else if ("4".equals(op)) {
                listarEstaciones();
            } else if ("5".equals(op)) {
                buscarEstacion(in);
            } else if ("0".equals(op)) {
                volver = true;

     */
}
