package trenes;


public class NodoVert<Vertice, Etiqueta> {

    private Object elem;
    private NodoVert<Vertice, Etiqueta> sigVertice;
    private NodoAdy<Vertice, Etiqueta> primerAdy;
    private Lista refAdy;

    public NodoVert(Object elem, NodoVert<Vertice, Etiqueta> sigVertice, NodoAdy<Vertice, Etiqueta> primerAdy) {
        this.elem = elem;
        this.sigVertice = sigVertice;
        this.primerAdy = primerAdy;
        this.refAdy = null;
    }

    public Lista getRefAdy() {
        return refAdy;
    }

    public void setRefAdy(Lista refAdy) {
        this.refAdy = refAdy;
    }

    public Object getElem() {
        return elem;
    }

    public NodoVert getSigVertice() {
        return sigVertice;
    }

    public NodoAdy getPrimerAdy() {
        return primerAdy;
    }

    public void setElem(Object elem) {
        this.elem = elem;
    }

    public void setSigVertice(NodoVert sigVertice) {
        this.sigVertice = sigVertice;
    }

    public void setPrimerAdy(NodoAdy primerAdy) {
        this.primerAdy = primerAdy;
    }

}
