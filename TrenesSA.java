package trenes;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Consumer;

public class TrenesSA {

    // ===== Estructuras =====
    private final AVL trenes = new AVL(); // TERMINADO Y REVISADO
    private final AVL estaciones = new AVL(); // TERMINADO Y REVISADO
    private final HashMap<String, String> lineas = new HashMap<>();// PENDIENTE REVISAR
    private final Map<String, Riel> rieles = new TreeMap<>();  //grafo etiquetado PENDIENTE

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
        // EJEMPLOS posibles:
        // return trenes.insertar(codigo, tren);
        // return trenes.put(codigo, tren);  // si tu AVL simula Map
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

    /*
    private int trenSize() {
        // return trenes.size();
        return trenes.size();
    }


    private void trenInOrder(Consumer<Tren> visit) {
        // trenes.inOrder(visit);
        trenes.inOrder(visit);
    }
     */
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
                System.out.println(response[1]);
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
        int numero = leerInt(in, "Número: ", 0);
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

            Integer numero = leerIntOpcional(in, "Nuevo número (Enter mantiene): ", 0);
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
    // =======================
    // ====== ABM LÍNEAS =====
    // =======================

    private void abmLineas(Scanner sc) {
        boolean volver = false;
        while (!volver) {
            imprimirMenuRieles();

            String linea = sc.nextLine().trim();
            int opc;
            try {
                opc = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opc) {
                case 1:
                    altaLinea(sc);
                    break;
                case 2:
                    bajaLinea(sc);
                    break;
                case 3:
                    modificarLinea(sc);
                    break;
                case 4:
                    listarLineas();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void imprimirMenuRieles() {
        System.out.println("=== ABM RIELES ===");
        System.out.println("1. Alta de riel");
        System.out.println("2. Baja de riel");
        System.out.println("3. Modificación de riel");
        System.out.println("4. Listar rieles");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
    }

    private void altaLinea(Scanner sc) {
        try {
            System.out.print("Código estación origen: ");
            int codOri = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Código estación destino: ");
            int codDes = Integer.parseInt(sc.nextLine().trim());

            if (codOri == codDes) {
                System.out.println("No se puede crear un riel de una estación consigo misma.");
                return;
            }
            Object[] response = estaciones.buscar(codOri);
            if (response[0] instanceof Boolean) {
                if (!(Boolean) response[0]) {
                    System.out.println("No existe estación con código " + codOri);
                    return;
                }
            }

            if (response[0] instanceof Boolean) {
                if (!(Boolean) response[0]) {
                    System.out.println("No existe estación con código " + codDes);
                    return;
                }
            }

            String clave = claveRiel(codOri, codDes);
            if (rieles.containsKey(clave)) {
                System.out.println("Ya existe un riel entre esas dos estaciones.");
                return;
            }

            System.out.print("Distancia (km): ");
            int distancia = Integer.parseInt(sc.nextLine().trim());

            Riel r = new Riel(codOri, codDes, distancia);
            rieles.put(clave, r);

            System.out.println("Riel agregado: " + r);
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        }
    }

    private void bajaLinea(Scanner sc) {
        try {
            System.out.print("Código linea origen: ");
            int codOri = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Código estación destino: ");
            int codDes = Integer.parseInt(sc.nextLine().trim());

            String clave = claveRiel(codOri, codDes);
            Riel eliminado = rieles.remove(clave);

            if (eliminado == null) {
                System.out.println("No existe riel entre esas dos estaciones.");
            } else {
                System.out.println("Riel eliminado: " + eliminado);
            }

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        }
    }

    private void modificarLinea(Scanner sc) {
        try {

            System.out.println("Linea actual: " + r);
            System.out.print("Nueva distancia (km): ");
            int nuevaDist = Integer.parseInt(sc.nextLine().trim());

            r.setDistanciaKm(nuevaDist);
            System.out.println("Linea modificado: " + r);

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        }
    }

    private void listarLineas() {
        if (rieles.isEmpty()) {
            System.out.println("No hay lineas cargados.");
            return;
        }
        System.out.println("=== LISTA DE LINEAS ===");
        for (Riel r : rieles.values()) {
            System.out.println(r);
        }
    }

    /*
    private void abmLineas(Scanner in) {
        System.out.println("[TODO] ABM Líneas (tenés el Map lineas listo; falta implementar alta/baja/modif/listar/buscar).");
    }
     */
    // =======================
    // ====== ABM RIELES =====
    // =======================
    private void abmRieles(Scanner sc) {
        boolean volver = false;
        while (!volver) {
            imprimirMenuRieles();

            String linea = sc.nextLine().trim();
            int opc;
            try {
                opc = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opc) {
                case 1:
                    altaRiel(sc);
                    break;
                case 2:
                    bajaRiel(sc);
                    break;
                case 3:
                    modificarRiel(sc);
                    break;
                case 4:
                    listarRieles();
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void imprimirMenuRieles() {
        System.out.println("=== ABM RIELES ===");
        System.out.println("1. Alta de riel");
        System.out.println("2. Baja de riel");
        System.out.println("3. Modificación de riel");
        System.out.println("4. Listar rieles");
        System.out.println("0. Volver");
        System.out.print("Opción: ");
    }

    private void altaRiel(Scanner sc) {
        try {
            System.out.print("Código estación origen: ");
            int codOri = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Código estación destino: ");
            int codDes = Integer.parseInt(sc.nextLine().trim());

            if (codOri == codDes) {
                System.out.println("No se puede crear un riel de una estación consigo misma.");
                return;
            }
            Object[] response = estaciones.buscar(codOri);
            if (response[0] instanceof Boolean) {
                if (!(Boolean) response[0]) {
                    System.out.println("No existe estación con código " + codOri);
                    return;
                }
            }

            if (response[0] instanceof Boolean) {
                if (!(Boolean) response[0]) {
                    System.out.println("No existe estación con código " + codDes);
                    return;
                }
            }

            String clave = claveRiel(codOri, codDes);
            if (rieles.containsKey(clave)) {
                System.out.println("Ya existe un riel entre esas dos estaciones.");
                return;
            }

            System.out.print("Distancia (km): ");
            int distancia = Integer.parseInt(sc.nextLine().trim());

            Riel r = new Riel(codOri, codDes, distancia);
            rieles.put(clave, r);

            System.out.println("Riel agregado: " + r);
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        }
    }

    private void bajaRiel(Scanner sc) {
        try {
            System.out.print("Código estación origen: ");
            int codOri = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Código estación destino: ");
            int codDes = Integer.parseInt(sc.nextLine().trim());

            String clave = claveRiel(codOri, codDes);
            Riel eliminado = rieles.remove(clave);

            if (eliminado == null) {
                System.out.println("No existe riel entre esas dos estaciones.");
            } else {
                System.out.println("Riel eliminado: " + eliminado);
            }

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        }
    }

    private void modificarRiel(Scanner sc) {
        try {
            System.out.print("Código estación origen: ");
            int codOri = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Código estación destino: ");
            int codDes = Integer.parseInt(sc.nextLine().trim());

            String clave = claveRiel(codOri, codDes);
            Riel r = rieles.get(clave);

            if (r == null) {
                System.out.println("No existe riel entre esas dos estaciones.");
                return;
            }

            System.out.println("Riel actual: " + r);
            System.out.print("Nueva distancia (km): ");
            int nuevaDist = Integer.parseInt(sc.nextLine().trim());

            r.setDistanciaKm(nuevaDist);
            System.out.println("Riel modificado: " + r);

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        }
    }

    private void listarRieles() {
        if (rieles.isEmpty()) {
            System.out.println("No hay rieles cargados.");
            return;
        }
        System.out.println("=== LISTA DE RIELES ===");
        for (Riel r : rieles.values()) {
            System.out.println(r);
        }
    }

    private String claveRiel(int codOri, int codDes) {
        return (codOri < codDes) ? (codOri + "-" + codDes) : (codDes + "-" + codOri);
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
                    listarRielesDeEstacion(sc);
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

    private void listarRielesDeEstacion(Scanner sc) {
        try {
            System.out.print("Código de estación: ");
            int cod = Integer.parseInt(sc.nextLine().trim());

            Object[] response = estaciones.buscar(cod);
            if (response[0] instanceof Boolean) {
                if (!(Boolean) response[0]) {
                    System.out.println("No existe estación con código " + cod);
                    return;
                }
            }

            System.out.println("Rieles que involucran a la estación " + cod + ":");
            boolean alguno = false;

            for (Riel r : rieles.values()) {
                if (r.getCodEstacionOrigen() == cod || r.getCodEstacionDestino() == cod) {
                    System.out.println(r);
                    alguno = true;
                }
            }

            if (!alguno) {
                System.out.println("No hay rieles para esa estación.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido.");
        }
    }

    /*
    private void mostrarResumen() {
        System.out.println("Estaciones: " + estaciones.size());
        // System.out.println("Trenes: " + trenes.size());
        System.out.println("Rieles: " + rieles.size());
    }
     */
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

    // ===== Helpers =====
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
}
