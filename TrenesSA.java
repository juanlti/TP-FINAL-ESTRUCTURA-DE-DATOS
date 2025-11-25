package trenes;

import trenes.Tren;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class TrenesSA {

    // Almacén provisorio (sin AVL aún)
    private final Map<Integer, Tren> trenes = new TreeMap<Integer, Tren>();
    private final Map<Integer, Estacion> estaciones = new TreeMap<Integer, Estacion>();

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

    /**
     * Procesa UNA opción y devuelve true si se debe salir
     */
    public boolean procesarOpcion(int opc, Scanner sc) {
        switch (opc) {
            case 1:
                cargarInicial(sc);    // TODO (StringTokenizer)
                break;
            case 2:
                abmTrenes(sc);       // ABM manual
                break;
            case 3:
                abmEstaciones(sc);   // TODO
                break;
            case 4:
                abmLineas(sc);       // TODO
                break;
            case 5:
                abmRieles(sc);       // TODO
                break;
            case 6:
                consultasTrenes(sc); // TODO
                break;
            case 7:
                consultasEstaciones(sc); // TODO
                break;
            case 8:
                consultasViajes(sc); // TODO
                break;
            case 9:
                mostrarSistema();    // TODO
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
                + "6) Consultas de Trenes\n"
                + "7) Consultas de Estaciones\n"
                + "8) Consultas de Viajes\n"
                + "9) Mostrar sistema\n"
                + "0) Salir\n"
                + "------------------------------------------------\n"
                + "Opción: "
        );
    }

    // ====== Opción 1 (stub por ahora) ======
    private void cargarInicial(Scanner sc) {
        System.out.println("[TODO] Carga inicial (StringTokenizer)...");
    }

    // ====== Opción 2: ABM Trenes (manual) ======
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
            if ("1".equals(op)) {
                altaTren(in);
            } else if ("2".equals(op)) {
                bajaTren(in);
            } else if ("3".equals(op)) {
                modificarTren(in);
            } else if ("4".equals(op)) {
                listarTrenes();
            } else if ("5".equals(op)) {
                buscarTren(in);
            } else if ("0".equals(op)) {
                volver = true;
            } else {
                System.out.println("Opción inválida.");
            }
        }
    }

    private void altaTren(Scanner in) {
        int codigo = leerInt(in, "Código (entero positivo): ", 1);
        if (trenes.containsKey(codigo)) {
            System.out.println("✗ Ya existe un tren con código " + codigo);
            return;
        }
        String propulsion = leerNoVacio(in, "Propulsión (electrico/diesel/otro): ");
        int vagPas = leerInt(in, "Vagones de pasajeros (>=0): ", 0);
        int vagCar = leerInt(in, "Vagones de carga (>=0): ", 0);
        String linea = leerOpcional(in, "Línea (Enter para 'no-asignado'): ");
        if (linea == null || linea.trim().isEmpty()) {
            linea = "no-asignado";
        }

        trenes.put(codigo, new Tren(codigo, propulsion, vagPas, vagCar, linea));
        System.out.println("✓ Alta OK");
    }

    private void bajaTren(Scanner in) {
        int codigo = leerInt(in, "Código del tren a eliminar: ", 1);
        if (trenes.remove(codigo) != null) {
            System.out.println("✓ Baja OK");
        } else {
            System.out.println("✗ No existe el tren " + codigo);
        }
    }

    private void modificarTren(Scanner in) {
        int codigo = leerInt(in, "Código del tren a modificar: ", 1);
        Tren t = trenes.get(codigo);
        if (t == null) {
            System.out.println("✗ No existe el tren " + codigo);
            return;
        }
        System.out.println("Actual: " + t);

        String propulsion = leerOpcional(in, "Nueva propulsión (Enter mantiene): ");
        Integer vagPas = leerIntOpcional(in, "Nuevos vagones pasajeros (Enter mantiene): ", 0);
        Integer vagCar = leerIntOpcional(in, "Nuevos vagones carga (Enter mantiene): ", 0);
        String linea = leerOpcional(in, "Nueva línea (Enter mantiene): ");

        if (propulsion != null && !propulsion.trim().isEmpty()) {
            t.setPropulsion(propulsion);
        }
        if (vagPas != null) {
            t.setCantidadVagonesPasajeros(vagPas.intValue());
        }
        if (vagCar != null) {
            t.setCantidadVagonesCarga(vagCar.intValue());
        }
        if (linea != null && !linea.trim().isEmpty()) {
            t.setLinea(linea);
        }

        System.out.println("✓ Modificación OK");
    }

    private void listarTrenes() {
        if (trenes.isEmpty()) {
            System.out.println("== Trenes == (sin trenes)");
            return;
        }
        System.out.println("== Trenes ==");
        for (Tren t : trenes.values()) {
            System.out.println(" - " + t);
        }
    }

    private void buscarTren(Scanner in) {
        int codigo = leerInt(in, "Código a buscar: ", 1);
        Tren t = trenes.get(codigo);
        if (t != null) {
            System.out.println(t);
        } else {
            System.out.println("✗ No existe el tren " + codigo);
        }
    }

    // ====== Otras opciones (stubs) ======
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
            if ("1".equals(op)) {
                altaEstacion(in);
            } else if ("2".equals(op)) {

                bajaEstacion(in);
            } else if ("3".equals(op)) {
                modificarEstacion(in);
            } else if ("4".equals(op)) {
                listarEstaciones();
            } else if ("5".equals(op)) {
                buscarEstacion(in);
            } else if ("0".equals(op)) {
                volver = true;
            } else {
                System.out.println("Opción inválida.");
            }

        }
    }

    private void buscarEstacion(Scanner in) {
        int codigo = leerInt(in, "Código a buscar: ", 1);
        Estacion unaEstacion = estaciones.get(codigo);

        if (unaEstacion != null) {
            System.out.println(unaEstacion);
        } else {
            System.out.println("✗ No existe la estacion " + codigo);
        };

    }

    private void listarEstaciones() {
        if (estaciones.isEmpty()) {
            System.out.println("== Estaciones == (sin estaciones)");
            return;
        }
        System.out.println("== Estaciones ==");
        for (Estacion estacion : estaciones.values()) {
            System.out.println(" - " + estacion);
        }

    }

    private void modificarEstacion(Scanner in) {
        int codigo = leerInt(in, "Código de la estacion a modificar: ", 1);
        Estacion estacion = estaciones.get(codigo);
        if (estacion == null) {
            System.out.println("✗ No existe la estacion " + codigo);
            return;
        }
        System.out.println("Actual: " + estacion);

        Integer numero = leerIntOpcional(in, "Nueva altura del domicilio (Enter mantiene): ", 0);
        String calle = leerOpcional(in, "Nueva calle (Enter mantiene): ");
        String ciudad = leerOpcional(in, "Nueva ciudad (Enter mantiene): ");
        String codigoPostal = leerOpcional(in, "Nuevo codigo (Enter mantiene): ");
        if (numero != null && numero > -1) {
            estacion.setNumero(numero);
        }
        if (calle != null && !calle.trim().isEmpty()) {
            estacion.setCalle(calle);
        }
        if (ciudad != null && !ciudad.trim().isEmpty()) {
            estacion.setCiudad(calle);
        }
        if (codigoPostal != null && !codigoPostal.trim().isEmpty()) {
            estacion.setCodigoPostal(calle);
        }

        System.out.println("✓ Modificación OK");
    }

    private void bajaEstacion(Scanner in) {

        int codigo = leerInt(in, "Código de la estacion a eliminar: ", 1);
        if (estaciones.remove(codigo) != null) {
            System.out.println("✓ Baja OK");
        } else {
            System.out.println("✗ No existe la estacion " + codigo);
        }

    }

    private void altaEstacion(Scanner in) {
        int codigo = leerInt(in, "Código de estación (entero positivo): ", 1);
        if (estaciones.containsKey(codigo)) {
            System.out.println("✗ Ya existe una estación con código " + codigo);
            return;
        }
        String nombre = leerNoVacio(in, "Nombre de la estación: ");
        String ciudad = leerNoVacio(in, "Ciudad: ");
        String calle = leerNoVacio(in, "Calle de la estación: ");
        int numero = leerInt(in, "numero de la calle: ", 0);
        String cp = leerNoVacio(in, "Codigo Postal: ");
        int cantVias = leerInt(in, "cantidad de vias: ", 0);
        int cantPlataformas = leerInt(in, "Cantidad de plataformas: ", 0);
        // public void Estacion(String nombre, String calle, int numero, String ciudad, String cp, int cantVias, int cantPlataformas) {

        estaciones.put(codigo, new Estacion(nombre, calle, numero, ciudad, cp, cantVias, cantPlataformas));
        // trenes.put(codigo, new Tren(codigo, propulsion, vagPas, vagCar, linea));
        System.out.println("✓ Alta OK");

        //   estaciones.put(codigo, new Estacion(codigo, nombre, ciudad));
        System.out.println("✓ Alta de estación OK");
    }

    private void abmLineas(Scanner sc) {
        System.out.println("[TODO] ABM Líneas...");
    }

    private void abmRieles(Scanner sc) {
        System.out.println("[TODO] ABM Rieles...");
    }

    private void consultasTrenes(Scanner sc) {
        System.out.println("[TODO] Consultas de Trenes...");
    }

    private void consultasEstaciones(Scanner sc) {
        System.out.println("[TODO] Consultas de Estaciones...");
    }

    private void consultasViajes(Scanner sc) {
        System.out.println("[TODO] Consultas de Viajes...");
    }

    private void mostrarSistema() {
        System.out.println("[TODO] Mostrar sistema...");
    }

    // ====== Helpers ======
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
        return s == null ? "" : s.trim();
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

    /**
     * Devuelve null si el usuario aprieta Enter (mantener)
     */
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
            return Integer.valueOf(val);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido; se ignora el cambio.");
            return null;
        }
    }
}
