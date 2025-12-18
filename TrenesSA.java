package trenes;

import static com.sun.org.apache.xalan.internal.lib.ExsltDynamic.map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Consumer;
import static jdk.nashorn.internal.objects.NativeArray.map;
import static jdk.nashorn.internal.objects.NativeDebug.map;

public class TrenesSA {

    // ===== Estructuras =====
    private final AVL trenes = new AVL(); // TERMINADO Y REVISADO
    private final AVL estaciones = new AVL(); // TERMINADO Y REVISADO
    private final HashMap<String, Linea> lineas = new HashMap<>(); // TERMINADO Y REVISADO
    private final Grafo<Estacion, Riel> red = new Grafo<>(); // TERMINADO Y REVISADO

    // ===== Programa =====
    public void comenzar() {
        Scanner sc = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            imprimirMenu();

            String linea = sc.nextLine().trim();
            int opc;
            try {
                opc = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            salir = procesarOpcion(opc, sc);
        }

        sc.close();
    }

    public boolean procesarOpcion(int opc, Scanner sc) {
        switch (opc) {
            case 1:
                cargarInicial(sc); // TODO (StringTokenizer)
                break;
            case 2:
                abmTrenes(sc);
                break;
            case 3:
                abmEstaciones(sc);
                break;
            case 4:
                abmLineas(sc);
                break;
            case 5:
                abmRieles(sc);
                break;
            case 6:
                menuConsultas(sc); // ✅ ahora sí se usa
                break;
            case 7:
                consultasEstaciones(sc); // TODO
                break;
            case 8:
                consultasViajes(sc); // TODO
                break;
            case 9:
                mostrarSistema(); // TODO
                break;
            case 0:
                System.out.println("Saliendo...");
                return true;
            default:
                System.out.println("Opción inválida.");
                break;
        }
        return false;
    }

    private void imprimirMenu() {
        System.out.print(
                "\n================== TrenesSA ==================\n"
                + "1) Cargar datos iniciales (archivo)\n"
                + "2) ABM Trenes\n"
                + "3) ABM Estaciones\n"
                + "4) ABM Líneas\n"
                + "5) ABM Rieles\n"
                + "6) Consultas\n"
                + "7) Consultas de Estaciones\n"
                + "8) Consultas de Viajes\n"
                + "9) Mostrar sistema\n"
                + "0) Salir\n"
                + "------------------------------------------------\n"
                + "Opción: "
        );
    }

    // =========================================================
    // ===== Adaptador AVL (AJUSTÁ ESTOS MÉTODOS A TU AVL) ======
    // =========================================================
    private boolean trenInsertar(int codigo, Tren tren) {

        return trenes.insertar((Comparable) codigo, tren);
    }

    private Object trenBuscar(int codigo) {
        // return trenes.buscar(codigo);
        Object[] response = trenes.buscar(codigo);
        boolean trenEncontrado = (boolean) response[0];
        if ((boolean) response[0]) {
            return response[1];
        }
        return null;

    }

    private boolean trenEliminar(int codigo) {
        // return trenes.eliminar(codigo);
        return trenes.eliminar(codigo);
    }

    // ====== Opción 1 ======
    private void cargarInicial(Scanner sc) {
        System.out.println("[TODO] Carga inicial (StringTokenizer)...");
    }

    // =========================
    // ====== ABM TRENES =======
    // =========================
    private void abmTrenes(Scanner in) {
        boolean volver = false;
        while (!volver) {
            System.out.print(
                    "---- ABM Trenes ----\n"
                    + "1) Alta\n"
                    + "2) Baja\n"
                    + "3) Modificación\n"
                    + "4) Listar\n"
                    + "5) Buscar por código\n"
                    + "6) Buscar destino del tren\n"
                    + "0) Volver\n"
                    + "Opción: "
            );

            String op = in.nextLine().trim();
            switch (op) {
                case "1":
                    altaTren(in);
                    break;
                case "2":
                    bajaTren(in);
                    break;
                case "3":
                    modificarTren(in);
                    break;
                case "4":
                    listarTrenes();
                    break;
                case "5":
                    buscarTren(in);
                    break;
                case "6":
                    buscarDestino(in);
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void altaTren(Scanner in) {
        int codigo = leerInt(in, "Código (entero positivo): ", 1);
        Object[] response = trenes.buscar(codigo);

        if (response[0] instanceof Boolean) {
            if ((Boolean) response[0]) {
                System.out.println("✗ Ya existe un tren con código " + codigo);
                return;
            }

        }

        String propulsion = leerNoVacio(in, "Propulsión (electrico/diesel/otro): ");
        int vagPas = leerInt(in, "Vagones de pasajeros (>=0): ", 0);
        int vagCar = leerInt(in, "Vagones de carga (>=0): ", 0);
        String linea = leerOpcional(in, "Línea (Enter para 'no-asignado'): ");
        if (linea.isEmpty()) {
            linea = "no-asignado";
        }

        boolean ok = trenInsertar(codigo, new Tren(codigo, propulsion, vagPas, vagCar, linea));
        if (ok) {
            System.out.println("✓ Alta OK");
        } else {
            System.out.println("✗ No se pudo insertar (clave duplicada o error AVL).");
        }
    }

    private void bajaTren(Scanner in) {
        int codigo = leerInt(in, "Código del tren a eliminar: ", 1);
        boolean ok = trenEliminar(codigo);
        if (ok) {
            System.out.println("✓ Baja OK");
        } else {
            System.out.println("✗ No existe el tren " + codigo);
        }
    }

    private void modificarTren(Scanner in) {
        int codigo = leerInt(in, "Código del tren a modificar: ", 1);
        Tren t = (Tren) trenBuscar(codigo);

        if (t == null) {
            System.out.println("✗ No existe el tren " + codigo);
            return;
        }

        System.out.println("Actual: " + t);

        String propulsion = leerOpcional(in, "Nueva propulsión (Enter mantiene): ");
        Integer vagPas = leerIntOpcional(in, "Nuevos vagones pasajeros (Enter mantiene): ", 0);
        Integer vagCar = leerIntOpcional(in, "Nuevos vagones carga (Enter mantiene): ", 0);
        String linea = leerOpcional(in, "Nueva línea (Enter mantiene): ");

        if (!propulsion.isEmpty()) {
            t.setPropulsion(propulsion);
        }
        if (vagPas != null) {
            t.setCantidadVagonesPasajeros(vagPas);
        }
        if (vagCar != null) {
            t.setCantidadVagonesCarga(vagCar);
        }
        if (!linea.isEmpty()) {
            t.setLinea(linea);
        }

        System.out.println("✓ Modificación OK");
    }

    private void listarTrenes() {
        if (trenes.esVacio()) {
            System.out.println("== Trenes == (sin trenes)");
            return;
        }

        System.out.println("== Trenes ==");
        System.out.println(trenes.listar());

    }

    private void buscarTren(Scanner in) {
        int codigo = leerInt(in, "Código a buscar: ", 1);
        Object[] response = trenes.buscar(codigo);
        if (response[0] instanceof Boolean) {
            if ((Boolean) response[0]) {
                Tren tren = (Tren) response[1];
                System.out.println(tren.getLinea());
            } else {
                System.out.println("✗ No existe el tren " + codigo);
            }
        }

    }

    private void buscarDestino(Scanner in) {
        int codigo = leerInt(in, "Código a buscar: ", 1);
        Object[] response = trenes.buscar(codigo);
        if (response[0] instanceof Boolean) {
            if ((Boolean) response[0]) {
                Tren tren = (Tren) response[1];
                //Estacion estaciones.
                System.out.println("TODAS LAS LINEAS DE ESTE TREEN " + tren.getLinea());

                Linea unaLinea = lineas.get(tren.getLinea());

                System.out.println("aca estoy " + unaLinea.toString());

            } else {
                System.out.println("✗ No existe el tren " + codigo);
            }
        }

    }

    // ============================
    // ====== ABM ESTACIONES ======
    // ============================
    private void abmEstaciones(Scanner in) {
        boolean volver = false;
        while (!volver) {
            System.out.print(
                    "---- ABM Estaciones ----\n"
                    + "1) Alta\n"
                    + "2) Baja\n"
                    + "3) Modificación\n"
                    + "4) Listar\n"
                    + "5) Buscar por código\n"
                    + "6) Buscar por coincidencia\n"
                    + "0) Volver\n"
                    + "Opción: "
            );

            String op = in.nextLine().trim();
            switch (op) {
                case "1":
                    altaEstacion(in);
                    break;
                case "2":
                    bajaEstacion(in);
                    break;
                case "3":
                    modificarEstacion(in);
                    break;
                case "4":
                    listarEstaciones();
                    break;
                case "5":
                    buscarEstacion(in);
                    break;
                case "6":
                    buscarCoincidencia(in);
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void altaEstacion(Scanner in) {
        int codigo = leerInt(in, "Código de estación (entero positivo): ", 1);
        Object[] response = estaciones.buscar(codigo);
        if (response[0] instanceof Boolean) {
            if ((Boolean) response[0]) {
                System.out.println("✗ Ya existe una estacion con código " + codigo);
                return;
            }
        }

        String nombre = leerNoVacio(in, "Nombre de la estación: ");
        String ciudad = leerNoVacio(in, "Ciudad: ");
        String calle = leerNoVacio(in, "Calle de la estación: ");
        String numero = leerNoVacio(in, "Número: ");
        String cp = leerNoVacio(in, "Código Postal: ");
        int cantVias = leerInt(in, "Cantidad de vías: ", 0);
        int cantPlataformas = leerInt(in, "Cantidad de plataformas: ", 0);

        estaciones.insertar(codigo, new Estacion(nombre, calle, numero, ciudad, cp, cantVias, cantPlataformas));
        System.out.println("✓ Alta de estación OK");
    }

    private void bajaEstacion(Scanner in) {
        int codigo = leerInt(in, "Código de la estación a eliminar: ", 1);
        if (estaciones.eliminar(codigo)) {
            System.out.println("✓ Baja OK");
        } else {
            System.out.println("✗ No existe la estación " + codigo);
        }
    }

    private void modificarEstacion(Scanner in) {
        int codigo = leerInt(in, "Código de la estación a modificar: ", 1);
        Object[] response = estaciones.buscar(codigo);

        if (response[0] instanceof Boolean) {
            if (!(Boolean) response[0]) {
                System.out.println("✗ No existe la estación " + codigo);
                return;
            }
            Estacion estacion = (Estacion) response[1];

            System.out.println("Actual: " + estacion);

            String numero = leerOpcional(in, "Nuevo número (Enter mantiene): ");
            String calle = leerOpcional(in, "Nueva calle (Enter mantiene): ");
            String ciudad = leerOpcional(in, "Nueva ciudad (Enter mantiene): ");
            String codigoPostal = leerOpcional(in, "Nuevo código postal (Enter mantiene): ");

            if (numero != null) {
                estacion.setNumero(numero);
            }
            if (!calle.isEmpty()) {
                estacion.setCalle(calle);
            }

            // ✅ FIX: antes estabas seteando "calle" en ciudad/cp
            if (!ciudad.isEmpty()) {
                estacion.setCiudad(ciudad);
            }
            if (!codigoPostal.isEmpty()) {
                estacion.setCodigoPostal(codigoPostal);
            }

            System.out.println("✓ Modificación OK");
        }
    }

    private void listarEstaciones() {
        if (estaciones.esVacio()) {
            System.out.println("== Estaciones == (sin estaciones)");
            return;
        }
        System.out.println("== Estaciones ==");
        System.out.println(estaciones.listar());
    }

    private void buscarEstacion(Scanner in) {
        int codigo = leerInt(in, "Código a buscar: ", 1);
        Object[] response = estaciones.buscar(codigo);
        if (response[0] instanceof Boolean) {
            if (!(Boolean) response[0]) {
                System.out.println("✗ No existe la estación " + codigo);
                return;
            }
            Estacion estacion = (Estacion) response[1];
            System.out.println(estacion);
        }
    }

    private void buscarCoincidencia(Scanner sc) {
        String prefijo = leerNoVacio(sc, "Nombre (prefijo) a buscar: ")
                .trim()
                .toLowerCase();

        Lista ls = estaciones.listar(); // Lista de Estacion
        StringBuilder out = new StringBuilder();

        for (int i = 1; i <= ls.longitud(); i++) {   // 1..N
            Estacion est = (Estacion) ls.recuperar(i);
            if (est == null) {
                continue;
            }

            String nombre = est.getNombre();
            if (nombre != null && nombre.trim().toLowerCase().startsWith(prefijo)) {
                out.append("• ").append(nombre).append('\n');
            }
        }

        if (out.length() == 0) {
            System.out.println("No hay estaciones que empiecen con '" + prefijo + "'.");
        } else {
            System.out.println("Coincidencias:\n" + out);
        }
    }

    // =======================
    // ====== ABM LÍNEAS =====
    // =======================
    private void abmLineas(Scanner sc) {
        boolean volver = false;
        while (!volver) {
            imprimirMenuLineas();
            String op = sc.nextLine().trim();

            switch (op) {
                case "1":
                    altaLinea(sc);
                    break;
                case "2":
                    bajaLinea(sc);
                    break;
                case "3":
                    modificarLinea(sc);
                    break;
                case "4":
                    listarLineas();
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void imprimirMenuLineas() {
        System.out.println("=== ABM LÍNEAS ===");
        System.out.println("1) Alta de línea");
        System.out.println("2) Baja de línea");
        System.out.println("3) Modificación de línea");
        System.out.println("4) Listar líneas");
        System.out.println("0) Volver");
        System.out.print("Opción: ");
    }

    private void altaLinea(Scanner sc) {
        String nombreLinea = leerNoVacio(sc, "Nombre de la línea: ");
        if (lineas.containsKey(nombreLinea)) {
            System.out.println("✗ Ya existe la línea " + nombreLinea);
            return;
        }

        int n = leerInt(sc, "Cantidad de estaciones en el recorrido: ", 2);
        Lista recorrido = new Lista();

        for (int i = 1; i <= n; i++) {
            int codEst = leerInt(sc, "Código estación #" + i + ": ", 1);
            Object[] res = estaciones.buscar(codEst);
            if (!(Boolean) res[0]) {
                System.out.println("✗ No existe estación con código " + codEst);
                return;
            }
            recorrido.insertar(res[1], recorrido.longitud() + 1); // guarda Estacion

        }
        Linea linea = new Linea(nombreLinea, recorrido);
        lineas.put(nombreLinea, linea);

        // true => no dirigido (carga ida y vuelta)
        //  lineas.put(nombreLinea, recorrido);
        System.out.println("✓ Línea creada: " + nombreLinea);
    }

    private void bajaLinea(Scanner sc) {
        String nombreLinea = leerNoVacio(sc, "Nombre de la línea a eliminar: ");
        Linea elim = lineas.remove(nombreLinea);
        if (elim == null) {
            System.out.println("✗ No existe la línea " + nombreLinea);
        } else {
            System.out.println("✓ Línea eliminada: " + nombreLinea);
        }
    }

    private void modificarLinea(Scanner sc) {
        String nombreLinea = leerNoVacio(sc, "Nombre de la línea a modificar: ");
        if (!lineas.containsKey(nombreLinea)) {
            System.out.println("✗ No existe la línea " + nombreLinea);
            return;
        }
        // opción simple: re-cargar recorrido completo
        lineas.remove(nombreLinea);
        System.out.println("(Se re-carga el recorrido completo)");
        altaLinea(sc);
    }

    private void listarLineas() {
        if (lineas.isEmpty()) {
            System.out.println("No hay líneas cargadas.");
            return;
        }
        System.out.println("=== LÍNEAS ===");
        lineas.forEach((key, linea) -> System.out.println(key + " = " + linea));
    }

    // =======================
    // ====== ABM RIELES =====
    // =======================
    // =======================
// ====== ABM RIELES =====
// =======================
    private void abmRieles(Scanner sc) {
        boolean volver = false;
        while (!volver) {
            imprimirMenuRieles();
            String op = sc.nextLine().trim();

            switch (op) {
                case "1":
                    altaRiel(sc);
                    break;
                case "2":
                    bajaRiel(sc);
                    break;
                case "3":
                    modificarRiel(sc);
                    break;
                case "4":
                    listarRieles();
                    break;
                case "5":
                    listarRielesDeEstacion(sc);
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void imprimirMenuRieles() {
        System.out.println("=== ABM RIELES (RED) ===");
        System.out.println("1) Alta de riel");
        System.out.println("2) Baja de riel");
        System.out.println("3) Modificación de riel");
        System.out.println("4) Listar rieles");
        System.out.println("5) Listar rieles de estacion");
        System.out.println("0) Volver");
        System.out.print("Opción: ");
    }

    private Estacion buscarEstacionPorCodigo(int codigo) {
        Object[] res = estaciones.buscar(codigo);
        if (!(Boolean) res[0]) {
            return null;
        }
        return (Estacion) res[1];
    }

    private void altaRiel(Scanner sc) {
        int codOri = leerInt(sc, "Código estación origen: ", 1);
        int codDes = leerInt(sc, "Código estación destino: ", 1);

        if (codOri == codDes) {
            System.out.println("No se puede crear un riel de una estación consigo misma.");
            return;
        }

        Estacion ori = buscarEstacionPorCodigo(codOri);
        Estacion des = buscarEstacionPorCodigo(codDes);

        if (ori == null) {
            System.out.println("No existe estación con código " + codOri);
            return;
        }
        if (des == null) {
            System.out.println("No existe estación con código " + codDes);
            return;
        }

        red.insertarVertice(ori);
        red.insertarVertice(des);

        // Verifico riel DIRECTO (arco). En tu Grafo el “exists” es verificarArco(...)
        if (red.existeArco(ori, des) || red.existeArco(des, ori)) {
            System.out.println("Ya existe un riel entre esas dos estaciones.");
            return;
        }

        int distancia = leerInt(sc, "Distancia (km): ", 1);

        Riel r = new Riel(codOri, codDes, distancia);

        // true => no dirigido (carga ida y vuelta)
        red.insertarArco(ori, des, true, r);

        System.out.println("✓ Riel agregado: " + r);
    }

    private void bajaRiel(Scanner sc) {
        int codOri = leerInt(sc, "Código estación origen: ", 1);
        int codDes = leerInt(sc, "Código estación destino: ", 1);

        Estacion ori = buscarEstacionPorCodigo(codOri);
        Estacion des = buscarEstacionPorCodigo(codDes);

        if (ori == null) {
            System.out.println("No existe estación con código " + codOri);
            return;
        }
        if (des == null) {
            System.out.println("No existe estación con código " + codDes);
            return;
        }

        // Recupero el riel antes de borrar (etiqueta del arco)
        Riel eliminado = (Riel) red.obtenerEtiquetaArco(ori, des);
        if (eliminado == null) {
            eliminado = (Riel) red.obtenerEtiquetaArco(des, ori);
        }

        if (eliminado == null) {
            System.out.println("No existe riel entre esas dos estaciones.");
            return;
        }

        // Borro ambos sentidos (porque lo cargaste como no dirigido)
        red.eliminarArco(ori, des);
        red.eliminarArco(des, ori);

        System.out.println("✓ Riel eliminado: " + eliminado);
    }

    private void modificarRiel(Scanner sc) {
        int codOri = leerInt(sc, "Código estación origen: ", 1);
        int codDes = leerInt(sc, "Código estación destino: ", 1);

        Estacion ori = buscarEstacionPorCodigo(codOri);
        Estacion des = buscarEstacionPorCodigo(codDes);

        if (ori == null) {
            System.out.println("No existe estación con código " + codOri);
            return;
        }
        if (des == null) {
            System.out.println("No existe estación con código " + codDes);
            return;
        }

        Riel r = (Riel) red.obtenerEtiquetaArco(ori, des);
        if (r == null) {
            r = (Riel) red.obtenerEtiquetaArco(des, ori);
        }

        if (r == null) {
            System.out.println("No existe riel entre esas dos estaciones.");
            return;
        }

        System.out.println("Riel actual: " + r);
        int nuevaDist = leerInt(sc, "Nueva distancia (km): ", 1);

        r.setDistanciaKm(nuevaDist);
        System.out.println("✓ Riel modificado: " + r);
    }

    private void listarRieles() {
        Lista etiquetas = red.listarEtiquetas(); // devuelve rieles (con duplicados si es no dirigido)
        if (etiquetas.esVacia()) {
            System.out.println("No hay rieles cargados.");
            return;
        }

        System.out.println("=== LISTA DE RIELES ===");

        java.util.HashSet<String> vistos = new java.util.HashSet<>();
        for (int i = 1; i <= etiquetas.longitud(); i++) {
            Riel r = (Riel) etiquetas.recuperar(i);
            String key = (Math.min(r.getCodEstacionOrigen(), r.getCodEstacionDestino()))
                    + "-"
                    + (Math.max(r.getCodEstacionOrigen(), r.getCodEstacionDestino()));
            if (vistos.add(key)) {
                System.out.println(r);
            }
        }
    }

    private void listarRielesDeEstacion(Scanner sc) {
        int cod = leerInt(sc, "Código de estación: ", 1);

        Estacion est = buscarEstacionPorCodigo(cod);
        if (est == null) {
            System.out.println("No existe estación con código " + cod);
            return;
        }

        Lista rieles = red.listarEtiquetasDeVertice(est);

        if (rieles.esVacia()) {
            System.out.println("No hay rieles conectados a esa estación.");
            return;
        }

        System.out.println("=== RIELES DE LA ESTACIÓN " + cod + " ===");
        for (int i = 1; i <= rieles.longitud(); i++) {
            Riel r = (Riel) rieles.recuperar(i);
            System.out.println(r);
        }
    }

    // ======================
    // ===== CONSULTAS ======
    // ======================
    private void menuConsultas(Scanner sc) {
        boolean volver = false;
        while (!volver) {
            System.out.println("=== CONSULTAS ===");
            System.out.println("1. Listar rieles de una estación");
            System.out.println("2. Cantidad total de estaciones / trenes / rieles");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            int opc;
            try {
                opc = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opc) {
                case 1:

                    break;
                case 2:
                    //mostrarResumen();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // ====== Stubs ======
    private void consultasEstaciones(Scanner sc) {
        System.out.println("[TODO] Consultas de Estaciones...");
    }

    private void consultasViajes(Scanner sc) {
        System.out.println("[TODO] Consultas de Viajes (acá va BFS/DFS/Dijkstra con rieles).");
    }

    private void mostrarSistema() {
        System.out.println("[TODO] Mostrar sistema...");
    }

    private String leerNoVacio(Scanner in, String prompt) {
        String s;
        do {
            System.out.print(prompt);
            s = in.nextLine().trim();
        } while (s.isEmpty());
        return s;
    }

    private String leerOpcional(Scanner in, String prompt) {
        System.out.print(prompt);
        String s = in.nextLine();
        return (s == null) ? "" : s.trim();
    }

    private int leerInt(Scanner in, String prompt, int minInclusive) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try {
                int val = Integer.parseInt(s);
                if (val < minInclusive) {
                    System.out.println("Debe ser >= " + minInclusive);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un entero válido.");
            }
        }
    }

    private Integer leerIntOpcional(Scanner in, String prompt, int minInclusive) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) {
            return null;
        }

        try {
            int val = Integer.parseInt(s);
            if (val < minInclusive) {
                System.out.println("Valor inválido; se ignora el cambio.");
                return null;
            }
            return val;
        } catch (NumberFormatException e) {
        }
        return null;
    }

    public void cargarInicialDesdeArchivo(String fileName) throws IOException {

        List<String[]> lineasPend = new ArrayList<>();
        List<String[]> rielesPend = new ArrayList<>();
        List<String[]> trenesPend = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String raw;
            int nro = 0;

            while ((raw = br.readLine()) != null) {
                nro++;
                String line = raw.replace("\uFEFF", "").trim();

                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                    continue;
                }

                char tipo = Character.toUpperCase(line.charAt(0));
                String[] p = line.split(";");
                for (int i = 0; i < p.length; i++) {
                    p[i] = p[i].trim();
                }

                switch (tipo) {
                    case 'E':
                        cargarEstacionDesdeArchivo(p);
                        break;
                    case 'L':
                        lineasPend.add(p);
                        break;
                    case 'R':
                        rielesPend.add(p);
                        break;
                    case 'T':
                        trenesPend.add(p);
                        break;
                    default:
                        System.out.println("Tipo desconocido en línea " + nro + ": " + line);
                        break;
                }
            }
        }

        for (String[] p : lineasPend) {
            cargarLineaDesdeArchivo(p);
        }
        for (String[] p : rielesPend) {
            cargarRielDesdeArchivo(p);
        }
        for (String[] p : trenesPend) {
            cargarTrenDesdeArchivo(p);
        }

        System.out.println("Carga inicial OK.");
    }

    private void cargarEstacionDesdeArchivo(String[] p) {

        int codigo = Integer.parseInt(p[1]);
        String nombre = p[2];
        String ciudad = p[3];
        String calle = p[4];
        String numero = p[5];
        String cp = p[6];
        int vias = Integer.parseInt(p[7]);
        int plataformas = Integer.parseInt(p[8]);

        // Evitar duplicados
        Object[] res = estaciones.buscar(codigo);
        if ((Boolean) res[0]) {
            return;
        }

        estaciones.insertar(codigo, new Estacion(nombre, calle, numero, ciudad, cp, vias, plataformas));
    }

    private void cargarLineaDesdeArchivo(String[] p) {
        // L;NombreLinea;codEst1;codEst2;...
        String nombreLinea = p[1];
        if (lineas.containsKey(nombreLinea)) {
            return;
        }

        Lista recorrido = new Lista();

        for (int i = 2; i < p.length; i++) {
            int codEst = Integer.parseInt(p[i]);
            Estacion est = buscarEstacionPorCodigo(codEst);
            if (est == null) {
                throw new IllegalStateException("Línea " + nombreLinea + " referencia estación inexistente: " + codEst);
            }
            recorrido.insertar(est, recorrido.longitud() + 1);
            red.insertarVertice(est);
        }

        Linea l = new Linea(nombreLinea, recorrido);
        lineas.put(nombreLinea, l);
    }

    private void cargarRielDesdeArchivo(String[] p) {
        // R;codOri;codDes;dist
        int codOri = Integer.parseInt(p[1]);
        int codDes = Integer.parseInt(p[2]);
        int dist = Integer.parseInt(p[3]);

        Estacion ori = buscarEstacionPorCodigo(codOri);
        Estacion des = buscarEstacionPorCodigo(codDes);

        if (ori == null || des == null) {
            throw new IllegalStateException("Riel referencia estación inexistente: " + codOri + " / " + codDes);
        }

        red.insertarVertice(ori);
        red.insertarVertice(des);

        if (red.existeArco(ori, des) || red.existeArco(des, ori)) {
            return;
        }

        Riel r = new Riel(codOri, codDes, dist);
        red.insertarArco(ori, des, true, r); // true => no dirigido (ida y vuelta)
    }

    private void cargarTrenDesdeArchivo(String[] p) {
        // T;codTren;propulsion;vagPas;vagCar;linea|no-asignado
        int codigo = Integer.parseInt(p[1]);
        String prop = p[2];
        int vagPas = Integer.parseInt(p[3]);
        int vagCar = Integer.parseInt(p[4]);
        String linea = p[5];

        if (!linea.equalsIgnoreCase("no-asignado") && !lineas.containsKey(linea)) {
            throw new IllegalStateException("Tren " + codigo + " referencia línea inexistente: " + linea);
        }

        Object[] res = trenes.buscar(codigo);
        if ((Boolean) res[0]) {
            return;
        }

        trenInsertar(codigo, new Tren(codigo, prop, vagPas, vagCar, linea));
    }

    private void cargarEstacion(String[] p) {
        String nombre = p[1];

        String calle;
        String numero;
        String ciudad;
        String cp;
        int vias;
        int plataformas;

        if (p.length >= 8) {
            calle = p[2];
            numero = p[3];
            ciudad = p[4];
            cp = p[5];
            vias = Integer.parseInt(p[6]);
            plataformas = Integer.parseInt(p[7]);
        } else {
            // formato compacto
            calle = p[2];
            numero = ""; // o intentás extraerlo
            ciudad = p[3];
            cp = p[4];
            vias = Integer.parseInt(p[5]);
            plataformas = Integer.parseInt(p[6]);
        }

        Estacion e = new Estacion(nombre, calle, numero, ciudad, cp, vias, plataformas);
        estaciones.insertar(nombre, e); // clave: nombre (Comparable)
    }


    private boolean existeEstacion(String nombre) {
        Object[] estacion = estaciones.buscar(nombre);
        return estacion[0] instanceof Boolean;
    }

    private Estacion obtenerEstacion(String nombre) {
        Object[] estacion = estaciones.buscar(nombre);
        return (Estacion) estacion[1];
    }
}
