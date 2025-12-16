package trenes;

/**
 * ↑: Alt+24 para flecha arriba. ↓: Alt+25 para flecha abajo. →: Alt+26 para
 * flecha derecha. ←: Alt+27 para flecha izquierda
 *
 * @author juanc //revisar Clone, Recorrido, grafo no dirigodo
 */
public class Grafo<Vertice, Etiqueta> {

    private NodoVert<Vertice, Etiqueta> inicio;
    private int cantidadVertices;

    public Grafo() {
        this.inicio = null;
        this.cantidadVertices = 0;
    }

    public boolean insertarVertice(Vertice x) {

        boolean exito = false;
        NodoVert<Vertice, Etiqueta> aux = ubicarVertice(x);
        if (aux == null) {
            //el object x no existe en el la columna de vertices, por lo tanto lo agrego.
            this.inicio = new NodoVert<>(x, this.inicio, null);
            exito = true;
            this.cantidadVertices++;
        }
        return exito;
    }

    private NodoVert<Vertice, Etiqueta> ubicarVertice(Vertice x) {

        boolean exito = false;

        NodoVert<Vertice, Etiqueta> aux = this.inicio;

        while (aux != null && !exito) {

            if (aux.getElem().equals(x)) {
                exito = true;
            } else {
                aux = aux.getSigVertice();
            }

        }
        return aux;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (NodoVert<Vertice, Etiqueta> v = this.inicio; v != null; v = v.getSigVertice()) {
            sb.append(v.getElem()).append(" → ");

            NodoAdy<Vertice, Etiqueta> a = v.getPrimerAdy();
            if (a == null) {
                sb.append("∅");
            } else {
                while (a != null) {
                    sb.append('[')
                            .append(a.getVertice().getElem())
                            .append(':')
                            .append(a.getEtiqueta())
                            .append(']');
                    a = a.getSigAdyancete();
                    if (a != null) {
                        sb.append(" → ");
                    }
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    public boolean insertarArco(Vertice origen, Vertice destino, boolean esGrafo, Etiqueta etiqueta) {
        //esGrafo es false entonces, considero como grafo dirigido
        //esGrafo es true entonces, considero como grafo no dirigido
        boolean exito = false;
        if (this.inicio != null) {
            //busco el vertice origenn
            NodoVert<Vertice, Etiqueta> auxOrigen = ubicarVertice(origen);

            if (auxOrigen != null) {
                NodoVert<Vertice, Etiqueta> auxDestino = ubicarVertice(destino);

                if (auxDestino != null) {

                    //agrego a origen un nuevo nodoAdy destino
                    NodoAdy<Vertice, Etiqueta> destinoNodoAdy = new NodoAdy<>(auxDestino, null, etiqueta);

                    recorrerAdyacentes(auxOrigen, destinoNodoAdy);
                    //   exito = recorrerAdyacentesOtraOpc(auxOrigen, destinoNodoAdy);
                    exito = true;

                    // revisar:  cuando es grafo no dirigido
                    if (esGrafo) {
                        System.out.println("aca no debe entrar");
                        // agrego a destino un nuevo nodoAdy origen
                        NodoAdy<Vertice, Etiqueta> origenNodoAdy = new NodoAdy<>(auxOrigen, null, etiqueta);
                        recorrerAdyacentes(auxDestino, origenNodoAdy);

                    }

                }

            }
        }
        return exito;
    }

    private void recorrerAdyacentes(NodoVert<Vertice, Etiqueta> ubicacion, NodoAdy<Vertice, Etiqueta> destinoInsertar) {

        NodoAdy aux = null;

        if (ubicacion.getPrimerAdy() != null) {
            //significa que el nodoUbicacion, es decir el nodo de origen tiene nodosAdy
            aux = ubicacion.getPrimerAdy();
            destinoInsertar.setSigAdyancete(aux);
            ubicacion.setPrimerAdy(destinoInsertar);

            if (ubicacion.getPrimerAdy().getSigAdyancete() != null) {
                if (ubicacion.getPrimerAdy().getSigAdyancete().getSigAdyancete() != null) {
                }
                /*
                 if (ubicacion.getPrimerAdy().getSigAdyancete().getSigAdyancete().getSigAdyancete() != null) {
                 System.out.println("resultado ? " + ubicacion.getElem() + " 1 ady debe ser => " + ubicacion.getPrimerAdy().getVertice().getElem().toString() + " y ult ady debe ser => " + ubicacion.getPrimerAdy().getSigAdyancete().getVertice().getElem().toString() + " no debe haber nada " + ubicacion.getPrimerAdy().getSigAdyancete().getSigAdyancete().getVertice().getElem().toString() + "1 no debe haber nada " + ubicacion.getPrimerAdy().getSigAdyancete().getSigAdyancete().getSigAdyancete().getVertice().getElem().toString());
                 }
                 */
            }
        } else {
            ubicacion.setPrimerAdy(destinoInsertar);
        }

    }

    private boolean recorrerAdyacentesOtraOpc(NodoVert ubicacion, NodoAdy destinoInsertar) {

        boolean existe = false;

        if (ubicacion.getPrimerAdy() == null) {
            ubicacion.setPrimerAdy(destinoInsertar);
            existe = true;

        } else {
            NodoAdy moverAdy = ubicacion.getPrimerAdy();
            while (moverAdy.getSigAdyancete() != null && !existe) {

                if (moverAdy.getVertice().getElem().equals(destinoInsertar.toString())) {
                    existe = true;

                }

                moverAdy = moverAdy.getSigAdyancete();

            }
            if (!existe) {
                moverAdy.setSigAdyancete(destinoInsertar);

            }
        }

        return existe;
    }

    public Lista listarEnProfundidad(Vertice x) {

        Lista visitados = new Lista();

        NodoVert<Vertice, Etiqueta> aux = ubicarVertice(x);

        while (aux != null) {

            if (visitados.localizar(aux.getElem()) < 0) {

                //entonces el vertice aux no se encuentra en visitados, por entede sus ady tampoco
                listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigVertice();

        }
        return visitados;
    }

    private void listarEnProfundidadAux(NodoVert n, Lista visitados) {

        if (n != null) {
            visitados.insertar(n.getElem(), visitados.longitud() + 1);
            NodoAdy moverAdy = n.getPrimerAdy();

            while (moverAdy != null) {
                //  System.out.println("VISITADOS " + moverAdy.getVertice().getElem());
                if (visitados.localizar(moverAdy.getVertice().getElem()) < 0) {
                    listarEnProfundidadAux(moverAdy.getVertice(), visitados);
                }

                moverAdy = moverAdy.getSigAdyancete();

            }
        }

    }

    public Lista listarEnAnchura(Vertice x) {
        Lista visitados = new Lista();
        NodoVert<Vertice, Etiqueta> aux = ubicarVertice(x);
        // NodoVert aux = this.inicio.getSigVertice().getSigVertice().getSigVertice().getSigVertice().getSigVertice().getSigVertice();
        System.out.println("el primero es ? " + aux.getElem());

        if (this.inicio != null) {
            visitados = listarEnAnchuraAux(aux, visitados);

            while (aux != null && visitados.longitud() < this.cantidadVertices) {

                if (visitados.localizar(aux.getElem()) == -1) {
                    visitados = listarEnAnchuraAux(aux, visitados);
                }
                aux = aux.getSigVertice();

            }

        }
        return visitados;

    }

    private Lista listarEnAnchuraAux(NodoVert u, Lista visitados) {
        Cola c1 = new Cola();

        visitados.insertar(u.getElem(), visitados.longitud() + 1);
        c1.poner(u);

        while (!c1.esVacia()) {
            NodoVert auxVert = (NodoVert) c1.obtenerFrente();

            c1.sacar();

            NodoAdy moverAdy = auxVert.getPrimerAdy();
            while (moverAdy != null) {

                if (visitados.localizar(moverAdy.getVertice().getElem()) == -1) {

                    visitados.insertar(moverAdy.getVertice().getElem(), visitados.longitud() + 1);
                    c1.poner(moverAdy.getVertice());
                }

                moverAdy = moverAdy.getSigAdyancete();

            }

        }

        return visitados;
    }

    public boolean eliminarVertice(Object elemento) {
        boolean fueEliminado = false;
        NodoVert otrosVertices = this.inicio;

        while (otrosVertices != null) {
            if (!otrosVertices.getElem().equals(elemento)) {

                //busco ady que apunten al alemento
                NodoAdy auxMover = otrosVertices.getPrimerAdy();
                if (auxMover != null && auxMover.getVertice().getElem().equals(elemento)) {
                    otrosVertices.setPrimerAdy(auxMover.getSigAdyancete());
                } else {

                    while (auxMover != null && !fueEliminado) {
                        if (auxMover.getSigAdyancete() != null && auxMover.getSigAdyancete().getVertice().getElem().equals(elemento)) {
                            //elimino ady
                            auxMover.setSigAdyancete(auxMover.getSigAdyancete().getSigAdyancete());
                            fueEliminado = true;
                        }
                        auxMover = auxMover.getSigAdyancete();
                    }
                    fueEliminado = false;
                }

            }
            otrosVertices = otrosVertices.getSigVertice();
        }

        return auxEliminarYauxExisteUnVerice(elemento, true);

    }

    private boolean auxEliminarYauxExisteUnVerice(Object elemento, boolean seElimina) {
        boolean fueEliminado = false;
        if (this.inicio != null) {

            if (this.inicio.getElem().equals(elemento)) {
                if (seElimina) {
                    //utilizo el recorrer arco

                    this.inicio = this.inicio.getSigVertice();

                }
                fueEliminado = true;

            } else {

                NodoVert auxVert = this.inicio;

                while (auxVert.getSigVertice() != null && !fueEliminado) {
                    if (auxVert.getSigVertice().getElem().equals(elemento)) {
                        if (seElimina) {
                            auxVert.setSigVertice(auxVert.getSigVertice().getSigVertice());
                        }

                        fueEliminado = true;
                    }
                    auxVert = auxVert.getSigVertice();
                }

            }

        }

        return fueEliminado;
    }

    public boolean existeVertice(Object elemento) {

        return auxEliminarYauxExisteUnVerice(elemento, false);

    }

    public boolean existeArco(Vertice origen, Vertice destino) {
        NodoVert<Vertice, Etiqueta> vo = ubicarVertice(origen);
        if (vo == null) {
            return false;
        }

        NodoAdy<Vertice, Etiqueta> a = vo.getPrimerAdy();
        while (a != null) {
            if (a.getVertice().getElem().equals(destino)) {
                return true;
            }
            a = a.getSigAdyancete();
        }
        return false;
    }

    public boolean eliminarArco(Vertice origen, Vertice destino) {
        NodoVert<Vertice, Etiqueta> vo = ubicarVertice(origen);
        if (vo == null) {
            return false;
        }

        NodoAdy<Vertice, Etiqueta> a = vo.getPrimerAdy();
        NodoAdy<Vertice, Etiqueta> prev = null;

        while (a != null) {
            if (a.getVertice().getElem().equals(destino)) {
                if (prev == null) {
                    vo.setPrimerAdy(a.getSigAdyancete()); // era el primero
                } else {
                    prev.setSigAdyancete(a.getSigAdyancete()); // salteo el nodo
                }
                return true;
            }
            prev = a;
            a = a.getSigAdyancete();
        }
        return false;
    }

    public Grafo clone() {
        // revisar 
        Grafo grafoClone = new Grafo();
        //   System.out.println("valor de inicio " + this.inicio.getElem());
        // System.out.println("valor de cantidad " + this.cantidadVertices);
        grafoClone.inicio = new NodoVert(this.inicio.getElem(), null, null);
        // System.out.println("valor de inicio " + grafoClone.inicio.getElem());
        NodoVert moverVerticesClone = grafoClone.inicio;
        NodoVert moverVerticeOriginal = this.inicio;
        while (moverVerticeOriginal != null) {

            //   System.out.println("vertice " + moverVerticeOriginal.getElem().toString());
            //  System.out.println("ady " + moverVerticeOriginal.getPrimerAdy());
            if (moverVerticeOriginal.getPrimerAdy() != null) {
                ///    System.out.println("ady dentro " + moverVerticeOriginal.getPrimerAdy().getVertice().getElem().toString());
                NodoAdy moverAdyOriginal = moverVerticeOriginal.getPrimerAdy();
                NodoAdy moverAdyClone = new NodoAdy(moverAdyOriginal.getVertice(), null);
                System.out.println(" nuevbo elemento " + moverAdyClone.getVertice().getElem());
                moverVerticesClone.setPrimerAdy(moverAdyClone);

                //moverAdyOriginal = moverAdyOriginal.getSigAdyancete();
                //  moverAdyClone = moverVerticesClone.getPrimerAdy();
                //primer vertice
                //(Object elem, NodoVert sigVertice, NodoAdy primerAdy
                while (moverAdyOriginal != null) {
                    //     System.out.println("ady de original " + moverAdyOriginal.getVertice().getElem());
                    //clono
                    //public NodoAdy(NodoVert vertice, NodoAdy sigAdyancete)
                    NodoAdy aux = new NodoAdy(moverAdyOriginal.getVertice(), null);
                    moverAdyClone.setSigAdyancete(aux);
                    moverAdyClone = moverAdyClone.getSigAdyancete();
                    moverAdyOriginal = moverAdyOriginal.getSigAdyancete();

                }

            } else {

                NodoVert auxVertClone = new NodoVert(moverVerticeOriginal.getSigVertice().getElem(), null, null);
                moverVerticesClone.setSigVertice(auxVertClone);
            }
            //   System.out.println("vertice "+moverVerticesClone.getElem());
            moverVerticesClone = moverVerticesClone.getSigVertice();
            moverVerticeOriginal = moverVerticeOriginal.getSigVertice();

        }
        return grafoClone;
    }

    public boolean existeCamino(Vertice origen, Vertice destino) {

        NodoVert<Vertice, Etiqueta> o = ubicarVertice(origen);

        if (o == null) {
            return false;
        }

        NodoVert<Vertice, Etiqueta> d = ubicarVertice(destino);
        if (d == null) {
            return false;
        }

        Lista visitados = new Lista(); // actúa como set de visitados
        return existeCaminoAux(o, d, visitados);
    }

    private boolean existeCaminoAux(NodoVert n, NodoVert destino, Lista visitados) {
        if (n == null) {
            return false;
        }

        // Si ya llegamos
        if (n.getElem().equals(destino.getElem())) {
            return true;
        }

        // Si ya lo visité, corto (evita ciclos)
        if (visitados.localizar(n.getElem()) > 0) {
            return false;
        }

        // Marco visitado UNA sola vez
        visitados.insertar(n.getElem(), visitados.longitud() + 1);

        // Exploro vecinos con short-circuit
        NodoAdy ady = n.getPrimerAdy();
        while (ady != null) {
            if (existeCaminoAux(ady.getVertice(), destino, visitados)) {
                return true; // corto apenas encuentro
            }
            ady = ady.getSigAdyancete();
        }

        return false;
    }

    public Object obtenerEtiquetaArco(Vertice origen, Vertice destino) {
        NodoVert<Vertice, Etiqueta> vo = ubicarVertice(origen);
        if (vo == null) {
            return null;
        }

        NodoAdy<Vertice, Etiqueta> ady = vo.getPrimerAdy();
        while (ady != null) {
            if (ady.getVertice().getElem().equals(destino)) {
                return ady.getEtiqueta();
            }
            ady = ady.getSigAdyancete();
        }
        return null;
    }

    public Lista listarEtiquetas() {
        Lista l = new Lista();
        for (NodoVert<Vertice, Etiqueta> v = this.inicio; v != null; v = v.getSigVertice()) {
            NodoAdy<Vertice, Etiqueta> a = v.getPrimerAdy();
            while (a != null) {
                l.insertar(a.getEtiqueta(), l.longitud() + 1);
                a = a.getSigAdyancete();
            }
        }
        return l;
    }

    public Lista listarEtiquetasDeVertice(Vertice v) {
        Lista l = new Lista();
        NodoVert<Vertice, Etiqueta> nv = ubicarVertice(v);
        if (nv == null) {
            return l;
        }

        NodoAdy<Vertice, Etiqueta> a = nv.getPrimerAdy();
        while (a != null) {
            l.insertar(a.getEtiqueta(), l.longitud() + 1); // guarda la etiqueta (Riel)
            a = a.getSigAdyancete();
        }
        return l;
    }

}
