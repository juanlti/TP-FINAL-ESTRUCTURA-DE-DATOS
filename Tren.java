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
public class Tren {

    private final int codigo;
    private String propulsion;
    private int vagPasajeros;
    private int vagCarga;
    private String linea; // "no-asignado" si libre

    public Tren(int codigo, String propulsion, int vagPasajeros, int vagCarga, String linea) {
        this.codigo = codigo;
        this.propulsion = propulsion;
        this.vagPasajeros = vagPasajeros;
        this.vagCarga = vagCarga;
        this.linea = linea;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    @Override
    public String toString() {
        return "Tren " + codigo + " [" + propulsion + "] pas:" + vagPasajeros
                + " carga:" + vagCarga + " linea:" + linea;
    }

    public void setPropulsion(String propulsion) {
        this.propulsion = propulsion;

    }

    public void setCantidadVagonesPasajeros(int cantVagonesPasajeros) {
        this.vagPasajeros = cantVagonesPasajeros;

    }

    public void setCantidadVagonesCarga(int cantVagonesCarga) {
        this.vagCarga = cantVagonesCarga;

    }

}
