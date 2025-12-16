package trenes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author juanc
 */
public class NodoAdy<Vertice, Etiqueta> {

    private NodoVert<Vertice, Etiqueta> vertice;
    private NodoAdy<Vertice, Etiqueta> sigAdyancete;
    private Object etiqueta;

    public NodoAdy(NodoVert<Vertice, Etiqueta> vertice, NodoAdy<Vertice, Etiqueta> sigAdyancete) {
        this.vertice = vertice;
        this.sigAdyancete = sigAdyancete;

    }

    public NodoAdy(NodoVert<Vertice, Etiqueta> vertice, NodoAdy<Vertice, Etiqueta> sigAdyancete, Object etiqueta) {
        this.vertice = vertice;
        this.sigAdyancete = sigAdyancete;
        this.etiqueta = etiqueta;
    }

    public Object getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Object etiqueta) {
        this.etiqueta = etiqueta;
    }

    public NodoVert getVertice() {
        return vertice;
    }

    public NodoAdy getSigAdyancete() {
        return sigAdyancete;
    }

    public void setVertice(NodoVert vertice) {
        this.vertice = vertice;
    }

    public void setSigAdyancete(NodoAdy sigAdyancete) {
        this.sigAdyancete = sigAdyancete;
    }

}
