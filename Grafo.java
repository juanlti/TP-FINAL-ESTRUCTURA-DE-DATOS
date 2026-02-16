package eed.tp;

import eed.tp.Cola;
import eed.tp.Lista;
import eed.tp.NodoAdy;
import eed.tp.NodoVert;

/**
 * ↑: Alt+24 para flecha arriba. ↓: Alt+25 para flecha abajo. →: Alt+26 para
 * flecha derecha. ←: Alt+27 para flecha izquierda
 *
 * @author juanc //revisar Clone, Recorrido, grafo no dirigodo
 */
public class Grafo {

    private NodoVert inicio;
    private int cantidadVertices;

    public Grafo() {
        this.inicio = null;
        this.cantidadVertices = 0;
    }

    public boolean insertarVertice(Estacion x) {

        boolean exito = false;
        NodoVert aux = ubicarVertice(x);
        if (aux == null) {
            //el object x no existe en el la columna de vertices, por lo tanto lo agrego.
            this.inicio = new NodoVert(x, this.inicio, null);
            exito = true;
            this.cantidadVertices++;
        }
        return exito;
    }

    public void ubierVerticePublico(String unaEstacion) {

    }

    private NodoVert ubicarVertice(Estacion x) {

        boolean exito = false;

        NodoVert aux = this.inicio;

        while (aux != null && !exito) {

            if (aux.getEstacion().equals(x)) {
                exito = true;
            } else {
                aux = aux.getSigEstacion();
            }

        }
        return aux;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (NodoVert v = this.inicio; v != null; v = v.getSigEstacion()) {
            sb.append(v.getEstacion()).append(" → ");

            NodoAdy a = v.getPrimerRiel();
            if (a == null) {
                sb.append("∅");
            } else {
                while (a != null) {
                    sb.append('[')
                            .append(a.getVertice().getEstacion())
                            .append(':')
                            .append(a.getEtiqueta())
                            .append(']');
                    a = a.getSigRiel();
                    if (a != null) {
                        sb.append(" → ");
                    }
                }
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    public boolean insertarArco(Estacion origen, Estacion destino, boolean esGrafo, Riel etiqueta) {
        //esGrafo es false entonces, considero como grafo dirigido
        //esGrafo es true entonces, considero como grafo no dirigido
        boolean exito = false;
        if (this.inicio != null) {
            //busco el vertice origenn
            NodoVert auxOrigen = ubicarVertice(origen);

            if (auxOrigen != null) {
                NodoVert auxDestino = ubicarVertice(destino);

                if (auxDestino != null) {

                    //agrego a origen un nuevo nodoAdy destino
                    NodoAdy destinoNodoAdy = new NodoAdy(auxDestino, null, etiqueta);

                    recorrerAdyacentes(auxOrigen, destinoNodoAdy);
                    //   exito = recorrerAdyacentesOtraOpc(auxOrigen, destinoNodoAdy);
                    exito = true;

                    // revisar:  cuando es grafo no dirigido
                    if (esGrafo) {
                        System.out.println("aca no debe entrar");
                        // agrego a destino un nuevo nodoAdy origen
                        NodoAdy origenNodoAdy = new NodoAdy(auxOrigen, null, etiqueta);
                        recorrerAdyacentes(auxDestino, origenNodoAdy);

                    }

                }

            }
        }
        return exito;
    }

    private void recorrerAdyacentes(NodoVert ubicacion, NodoAdy destinoInsertar) {

        NodoAdy aux = null;

        if (ubicacion.getPrimerRiel() != null) {
            //significa que el nodoUbicacion, es decir el nodo de origen tiene nodosAdy
            aux = ubicacion.getPrimerRiel();
            destinoInsertar.setSigAdyancete(aux);
            ubicacion.setPrimerRiel(destinoInsertar);

            if (ubicacion.getPrimerRiel().getSigRiel() != null) {
                if (ubicacion.getPrimerRiel().getSigRiel().getSigRiel() != null) {
                }
                /*
                 if (ubicacion.getPrimerAdy().getSigAdyancete().getSigAdyancete().getSigAdyancete() != null) {
                 System.out.println("resultado ? " + ubicacion.getEstacion() + " 1 ady debe ser => " + ubicacion.getPrimerAdy().getVertice().getEstacion().toString() + " y ult ady debe ser => " + ubicacion.getPrimerAdy().getSigAdyancete().getVertice().getEstacion().toString() + " no debe haber nada " + ubicacion.getPrimerAdy().getSigAdyancete().getSigAdyancete().getVertice().getEstacion().toString() + "1 no debe haber nada " + ubicacion.getPrimerAdy().getSigAdyancete().getSigAdyancete().getSigAdyancete().getVertice().getEstacion().toString());
                 }
                 */
            }
        } else {
            ubicacion.setPrimerRiel(destinoInsertar);
        }

    }

    private boolean recorrerAdyacentesOtraOpc(NodoVert ubicacion, NodoAdy destinoInsertar) {

        boolean existe = false;

        if (ubicacion.getPrimerRiel() == null) {
            ubicacion.setPrimerRiel(destinoInsertar);
            existe = true;

        } else {
            NodoAdy moverAdy = ubicacion.getPrimerRiel();
            while (moverAdy.getSigRiel() != null && !existe) {

                if (moverAdy.getVertice().getEstacion().equals(destinoInsertar.toString())) {
                    existe = true;

                }

                moverAdy = moverAdy.getSigRiel();

            }
            if (!existe) {
                moverAdy.setSigAdyancete(destinoInsertar);

            }
        }

        return existe;
    }

    public Lista listarEnProfundidad(Estacion x) {

        Lista visitados = new Lista();

        NodoVert aux = ubicarVertice(x);

        while (aux != null) {

            if (visitados.localizar(aux.getEstacion()) < 0) {

                //entonces el vertice aux no se encuentra en visitados, por entede sus ady tampoco
                listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigEstacion();

        }
        return visitados;
    }

    private void listarEnProfundidadAux(NodoVert n, Lista visitados) {

        if (n != null) {
            visitados.insertar(n.getEstacion(), visitados.longitud() + 1);
            NodoAdy moverAdy = n.getPrimerRiel();

            while (moverAdy != null) {
                //  System.out.println("VISITADOS " + moverAdy.getVertice().getEstacion());
                if (visitados.localizar(moverAdy.getVertice().getEstacion()) < 0) {
                    listarEnProfundidadAux(moverAdy.getVertice(), visitados);
                }

                moverAdy = moverAdy.getSigRiel();

            }
        }

    }

    public Lista listarEnAnchura(Estacion x) {
        Lista visitados = new Lista();
        NodoVert aux = ubicarVertice(x);
        // NodoVert aux = this.inicio.getSigEstacion().getSigVertice().getSigVertice().getSigVertice().getSigVertice().getSigVertice();
        System.out.println("el primero es ? " + aux.getEstacion());

        if (this.inicio != null) {
            visitados = listarEnAnchuraAux(aux, visitados);

            while (aux != null && visitados.longitud() < this.cantidadVertices) {

                if (visitados.localizar(aux.getEstacion()) == -1) {
                    visitados = listarEnAnchuraAux(aux, visitados);
                }
                aux = aux.getSigEstacion();

            }

        }
        return visitados;

    }

    private Lista listarEnAnchuraAux(NodoVert u, Lista visitados) {
        Cola c1 = new Cola();

        visitados.insertar(u.getEstacion(), visitados.longitud() + 1);
        c1.poner(u);

        while (!c1.esVacia()) {
            NodoVert auxVert = (NodoVert) c1.obtenerFrente();

            c1.sacar();

            NodoAdy moverAdy = auxVert.getPrimerRiel();
            while (moverAdy != null) {

                if (visitados.localizar(moverAdy.getVertice().getEstacion()) == -1) {

                    visitados.insertar(moverAdy.getVertice().getEstacion(), visitados.longitud() + 1);
                    c1.poner(moverAdy.getVertice());
                }

                moverAdy = moverAdy.getSigRiel();

            }

        }

        return visitados;
    }

    public boolean eliminarVertice(Object elemento) {
        boolean fueEliminado = false;
        NodoVert otrosVertices = this.inicio;

        while (otrosVertices != null) {
            if (!otrosVertices.getEstacion().equals(elemento)) {

                //busco ady que apunten al alemento
                NodoAdy auxMover = otrosVertices.getPrimerRiel();
                if (auxMover != null && auxMover.getVertice().getEstacion().equals(elemento)) {
                    otrosVertices.setPrimerRiel(auxMover.getSigRiel());
                } else {

                    while (auxMover != null && !fueEliminado) {
                        if (auxMover.getSigRiel() != null && auxMover.getSigRiel().getVertice().getEstacion().equals(elemento)) {
                            //elimino ady
                            auxMover.setSigAdyancete(auxMover.getSigRiel().getSigRiel());
                            fueEliminado = true;
                        }
                        auxMover = auxMover.getSigRiel();
                    }
                    fueEliminado = false;
                }

            }
            otrosVertices = otrosVertices.getSigEstacion();
        }

        return auxEliminarYauxExisteUnVerice(elemento, true);

    }

    private boolean auxEliminarYauxExisteUnVerice(Object elemento, boolean seElimina) {
        boolean fueEliminado = false;
        if (this.inicio != null) {

            if (this.inicio.getEstacion().equals(elemento)) {
                if (seElimina) {
                    //utilizo el recorrer arco

                    this.inicio = this.inicio.getSigEstacion();

                }
                fueEliminado = true;

            } else {

                NodoVert auxVert = this.inicio;

                while (auxVert.getSigEstacion() != null && !fueEliminado) {
                    if (auxVert.getSigEstacion().getEstacion().equals(elemento)) {
                        if (seElimina) {
                            auxVert.setSigEstacion(auxVert.getSigEstacion().getSigEstacion());
                        }

                        fueEliminado = true;
                    }
                    auxVert = auxVert.getSigEstacion();
                }

            }

        }

        return fueEliminado;
    }

    public boolean existeVertice(Object elemento) {

        return auxEliminarYauxExisteUnVerice(elemento, false);

    }

    public boolean existeArco(Estacion origen, Estacion destino) {
        NodoVert vo = ubicarVertice(origen);
        if (vo == null) {
            return false;
        }

        NodoAdy a = vo.getPrimerRiel();
        while (a != null) {
            if (a.getVertice().getEstacion().equals(destino)) {
                return true;
            }
            a = a.getSigRiel();
        }
        return false;
    }

    public boolean eliminarArco(Estacion origen, Estacion destino) {
        NodoVert vo = ubicarVertice(origen);
        if (vo == null) {
            return false;
        }

        NodoAdy a = vo.getPrimerRiel();
        NodoAdy prev = null;

        while (a != null) {
            if (a.getVertice().getEstacion().equals(destino)) {
                if (prev == null) {
                    vo.setPrimerRiel(a.getSigRiel()); // era el primero
                } else {
                    prev.setSigAdyancete(a.getSigRiel()); // salteo el nodo
                }
                return true;
            }
            prev = a;
            a = a.getSigRiel();
        }
        return false;
    }

    public Grafo clone() {
        // revisar 
        Grafo grafoClone = new Grafo();
        //   System.out.println("valor de inicio " + this.inicio.getEstacion());
        // System.out.println("valor de cantidad " + this.cantidadVertices);
        grafoClone.inicio = new NodoVert(this.inicio.getEstacion(), null, null);
        // System.out.println("valor de inicio " + grafoClone.inicio.getEstacion());
        NodoVert moverVerticesClone = grafoClone.inicio;
        NodoVert moverVerticeOriginal = this.inicio;
        while (moverVerticeOriginal != null) {

            //   System.out.println("vertice " + moverVerticeOriginal.getEstacion().toString());
            //  System.out.println("ady " + moverVerticeOriginal.getPrimerAdy());
            if (moverVerticeOriginal.getPrimerRiel() != null) {
                ///    System.out.println("ady dentro " + moverVerticeOriginal.getPrimerAdy().getVertice().getEstacion().toString());
                NodoAdy moverAdyOriginal = moverVerticeOriginal.getPrimerRiel();
                NodoAdy moverAdyClone = new NodoAdy(moverAdyOriginal.getVertice(), null);
                System.out.println(" nuevbo elemento " + moverAdyClone.getVertice().getEstacion());
                moverVerticesClone.setPrimerRiel(moverAdyClone);

                //moverAdyOriginal = moverAdyOriginal.getSigAdyancete();
                //  moverAdyClone = moverVerticesClone.getPrimerAdy();
                //primer vertice
                //(Object elem, NodoVert sigVertice, NodoAdy primerAdy
                while (moverAdyOriginal != null) {
                    //     System.out.println("ady de original " + moverAdyOriginal.getVertice().getEstacion());
                    //clono
                    //public NodoAdy(NodoVert vertice, NodoAdy sigAdyancete)
                    NodoAdy aux = new NodoAdy(moverAdyOriginal.getVertice(), null);
                    moverAdyClone.setSigAdyancete(aux);
                    moverAdyClone = moverAdyClone.getSigRiel();
                    moverAdyOriginal = moverAdyOriginal.getSigRiel();

                }

            } else {

                NodoVert auxVertClone = new NodoVert(moverVerticeOriginal.getSigEstacion().getEstacion(), null, null);
                moverVerticesClone.setSigEstacion(auxVertClone);
            }
            //   System.out.println("vertice "+moverVerticesClone.getEstacion());
            moverVerticesClone = moverVerticesClone.getSigEstacion();
            moverVerticeOriginal = moverVerticeOriginal.getSigEstacion();

        }
        return grafoClone;
    }

    public boolean existeCamino(Estacion origen, Estacion destino) {

        NodoVert o = ubicarVertice(origen);

        if (o == null) {
            return false;
        }

        NodoVert d = ubicarVertice(destino);
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
        if (n.getEstacion().equals(destino.getEstacion())) {
            return true;
        }

        // Si ya lo visité, corto (evita ciclos)
        if (visitados.localizar(n.getEstacion()) > 0) {
            return false;
        }

        // Marco visitado UNA sola vez
        visitados.insertar(n.getEstacion(), visitados.longitud() + 1);

        // Exploro vecinos con short-circuit
        NodoAdy ady = n.getPrimerRiel();
        while (ady != null) {
            if (existeCaminoAux(ady.getVertice(), destino, visitados)) {
                return true; // corto apenas encuentro
            }
            ady = ady.getSigRiel();
        }

        return false;
    }

    public Object obtenerEtiquetaArco(Estacion origen, Estacion destino) {
        NodoVert vo = ubicarVertice(origen);
        if (vo == null) {
            return null;
        }

        NodoAdy ady = vo.getPrimerRiel();
        while (ady != null) {
            if (ady.getVertice().getEstacion().equals(destino)) {
                return ady.getEtiqueta();
            }
            ady = ady.getSigRiel();
        }
        return null;
    }

    public Lista listarEtiquetas() {
        Lista l = new Lista();
        for (NodoVert v = this.inicio; v != null; v = v.getSigEstacion()) {
            NodoAdy a = v.getPrimerRiel();
            while (a != null) {
                l.insertar(a.getEtiqueta(), l.longitud() + 1);
                a = a.getSigRiel();
            }
        }
        return l;
    }

    public Lista listarEtiquetasDeVertice(Estacion v) {
        Lista l = new Lista();
        NodoVert nv = ubicarVertice(v);
        if (nv == null) {
            return l;
        }

        NodoAdy a = nv.getPrimerRiel();
        while (a != null) {
            l.insertar(a.getEtiqueta(), l.longitud() + 1); // guarda la etiqueta (Riel)
            a = a.getSigRiel();
        }
        return l;
    }

    // eliminar
    public String getAdyacentesDeUnaEstaciones(int codigo) {
        NodoAdy aux = this.inicio.getSigEstacion().getSigEstacion().getSigEstacion().getPrimerRiel();

        String estaciones = "";
        while (aux != null) {
            estaciones = estaciones + aux.getEtiqueta().toString();
            aux = aux.getSigRiel();
        }
        return "adyacentes de " + this.inicio.getSigEstacion().getSigEstacion().getSigEstacion().toString() + " estaciones : " + estaciones;
    }
// eliminar

    public String getEstacionMasCercana(int codigo) {
        NodoAdy aux = this.inicio.getSigEstacion().getSigEstacion().getSigEstacion().getPrimerRiel();

        String estaciones = "";
        Riel r = (Riel) aux.getSigRiel().getEtiqueta();

        while (aux != null) {
            estaciones = estaciones + (Riel) aux.getSigRiel().getEtiqueta();
            aux = aux.getSigRiel();
        }
        return "adyacentes de " + this.inicio.getSigEstacion().getSigEstacion().getSigEstacion().toString() + " estaciones : " + estaciones;
    }
// eliminar

    public String obtenerCaminoMasCortoEnNodos(String v) {
        NodoAdy aux = this.inicio.getSigEstacion().getSigEstacion().getSigEstacion().getPrimerRiel();

        Riel distancia = (Riel) aux.getSigRiel().getEtiqueta();
        String distanciasEstaciones = "primera distancia: [" + distancia + "] ";
        aux = aux.getSigRiel();
        int distanciaMasCorta = 1000000;
        int codigoProximaEstacion = -1;
        AVL myAVL = new AVL();

        Riel rielDeLaEstacionOrigen = (Riel) aux.getEtiqueta();

// 1. Obtener el arreglo de respuesta, no el objeto directo
        Object[] response = (Object[]) myAVL.buscar(rielDeLaEstacionOrigen.getCodEstacionOrigen());

// 2. Verificar si la búsqueda fue exitosa antes de hacer el casting
        if (response != null && (Boolean) response[0]) {
            // 3. El objeto Estacion está en la posición 1 del arreglo
            Estacion unaEstacion = (Estacion) response[1];
            System.out.println("Estación encontrada: " + unaEstacion.getNombre());
        }

        String estacionMasCercana = "";
        while (aux.getSigRiel() != null) {

            aux = aux.getSigRiel();
            distancia = (Riel) aux.getEtiqueta();
            if (distanciaMasCorta > distancia.getDistanciaKm()) {
                distanciaMasCorta = distancia.getDistanciaKm();
                codigoProximaEstacion = distancia.getCodEstacionDestino();
                // Estacion e = (Estacion) aux.getEtiqueta();

                // Object[] response = myAVL.buscar(codigoProximaEstacion);
                if (response[0] instanceof Boolean) {
                    if (!(Boolean) response[0]) {
                        System.out.println("✗ No existe la estación " + codigoProximaEstacion);
                        return "";
                    }
                    Estacion estacion = (Estacion) response[1];
                    //System.out.println(estacion);
                    estacionMasCercana = estacion.getNombre();
                }
                ///  estacionMasCercana=e.toString();

            }

            distanciasEstaciones = distanciasEstaciones + " [" + distancia.getDistanciaKm() + "]";

        }

        return "la distancia entre dos estaciones es: " + estacionMasCercana + " " + distanciaMasCorta + " distancias desde la estacion : " + this.inicio.getSigEstacion().getSigEstacion().getSigEstacion().toString() + "  respecto a las estaciones adyacentes " + distanciasEstaciones;

    }

    private void caminoMasCortoEnNodosAux(Lista ls, String v, NodoAdy nodo) {

        if (nodo == null) {
            System.out.println("Lista ls " + ls.toString());
            System.out.println("muestro estaciones de la linea ");
        } else {

            //pregunto si existe nodo es igual a v
            if (nodo.equals(v)) {

            } else {
                //pregunto si nodo existe en ls
                if (ls.localizar(v) != -1) {
                    //no debo recorrer ese nodo

                } else {
                    //lo agrego y recorro
                    ls.insertar(v, ls.longitud());
                    System.out.println("Lista ls antes " + ls.toString());
                    caminoMasCortoEnNodosAux(ls, v, nodo.getSigRiel());
                    System.out.println("Lista ls despues " + ls.toString());

                }

            }

        }

    }

    public String caminoConMenosEstaciones(Estacion origen, Estacion destino) {
        Lista caminoFinal = new Lista();
        Cola cola = new Cola();
        Lista visitados = new Lista();
        Lista tablaPadres = new Lista(); // Guardará objetos Parentesco
        NodoVert nodoOrigen = ubicarVertice(origen); // Método que ya debes tener
        if (nodoOrigen == null) {
            return caminoFinal.toString();
        }

        //System.out.println("oriden " + nodoOrigen);
        cola.poner(nodoOrigen);
        visitados.insertar(origen.getNombre(), visitados.longitud() + 1);
        boolean encontrado = false;

        // 1. EXPLORACIÓN: Llenamos la tabla de padres
        while (!cola.esVacia() && !encontrado) {
            NodoVert actual = (NodoVert) cola.obtenerFrente();
            cola.sacar();
            Estacion datosActual = (Estacion) actual.getEstacion();

            if (datosActual.getNombre().equalsIgnoreCase(destino.getNombre())) {
                encontrado = true;
            } else {
                //recorro adyacentes a nodo Padre
                NodoAdy ady = actual.getPrimerRiel();
                while (ady != null) {
                    NodoVert vecino = ady.getVertice();
                    Estacion vecinoEstacion = (Estacion) vecino.getEstacion();
                    String nombreVecino = vecinoEstacion.getNombre();

                    if (visitados.localizar(nombreVecino) < 0) {
                        visitados.insertar(vecinoEstacion.getNombre(), visitados.longitud() + 1);
                        // IMPORTANTE: Anotamos quién descubrió a quién
                        tablaPadres.insertar(new Parentesco(nombreVecino, datosActual.getNombre()), 1);
                        cola.poner(vecino);
                    }

                    ady = ady.getSigRiel();
                }
            }
        }

        cola.sacar();

        // 2. RECONSTRUCCIÓN: Vamos del destino al origen usando la tabla
        if (encontrado) {
            String actual = destino.getNombre();
            while (actual != null) {
                // Insertamos al principio para que quede Origen -> Destino
                caminoFinal.insertar(actual, 1);
                actual = buscarPadreEnTabla(actual, tablaPadres);
            }
        }

        return caminoFinal.toString();
    }

    private String buscarPadreEnTabla(String hijo, Lista tabla) {
        for (int i = 1; i <= tabla.longitud(); i++) {
            Parentesco p = (Parentesco) tabla.recuperar(i);
            if (p.getHijo().equalsIgnoreCase(hijo)) {
                return p.getPadre();
            }
        }
        return null; // Llegamos al origen (no tiene padre)
    }

}
