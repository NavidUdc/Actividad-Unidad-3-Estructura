
import modelo.PacienteUrgencia;
import servicio.GestorUrgencia;

import java.util.Scanner;

public class main {

    private static final GestorUrgencia gestor = new GestorUrgencia();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   SISTEMA DE GESTION - URGENCIAS");
        System.out.println("   Colecciones SDK Java | Actividad 3");
        System.out.println("   Universidad de Cartagena");
        System.out.println("==============================================");

        int opcion = 0;
        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                ejecutar(opcion);
            } catch (NumberFormatException e) {
                System.out.println("\n  Opción inválida. Ingrese un número del 1 al 14.");
            } catch (Exception e) {
                System.out.println("\n  Error: " + e.getMessage());
            }
        } while (opcion != 14);
    }

    private static void mostrarMenu() {
        System.out.println("\n----------------------------------------------");
        System.out.println("   1.  Registrar paciente");
        System.out.println("   2.  Ver todos los pacientes");
        System.out.println("   3.  Ver pacientes pendientes");
        System.out.println("   4.  Atender siguiente paciente");
        System.out.println("   5.  Ver historial de atendidos");
        System.out.println("   6.  Buscar por código de triage");
        System.out.println("   7.  Buscar por nombre ");
        System.out.println("   8.  Filtrar pacientes");
        System.out.println("   9.  Ordenar pacientes");
        System.out.println("  10.  Estadísticas");
        System.out.println("  11.  Agrupamientos");
        System.out.println("  12.  Cancelar paciente pendiente");
        System.out.println("  13.  Deshacer última atención");
        System.out.println("  14.  Salir");
        System.out.println("----------------------------------------------");
        System.out.print("  Seleccione una opción: ");
    }

    private static void ejecutar(int opcion) throws Exception {
        switch (opcion) {
            case 1 -> registrarPaciente();
            case 2 -> gestor.verTodos();
            case 3 -> gestor.verPendientes();
            case 4 -> gestor.atenderSiguiente();
            case 5 -> gestor.verHistorial();
            case 6 -> buscarPorTriage();
            case 7 -> buscarPorNombre();
            case 8 -> filtrar();
            case 9 -> ordenar();
            case 10 -> gestor.verEstadisticas();
            case 11 -> gestor.verAgrupamientos();
            case 12 -> cancelar();
            case 13 -> gestor.deshacer();
            case 14 -> System.out.println("\n  Sistema cerrado. Hasta luego.");
            default -> System.out.println("\n  Opción no válida. Elija entre 1 y 14.");
        }
    }

    // ── Helpers de entrada ───────────────────────────────────────────────────

    private static void registrarPaciente() throws Exception {
        System.out.println("\n  --- REGISTRAR PACIENTE ---");

        System.out.print("  Código de triage (ej: TRI-001): ");
        String codigoTriage = scanner.nextLine().trim().toUpperCase();
        if (codigoTriage.isEmpty())
            throw new IllegalArgumentException("El código de triage no puede estar vacío");

        System.out.print("  Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío");

        System.out.print("  Número de identificación: ");
        String identificacion = scanner.nextLine().trim();
        if (identificacion.isEmpty())
            throw new IllegalArgumentException("La identificación no puede estar vacía");

        System.out.println("  Niveles de urgencia:");
        System.out.println("    1. ROJO   (Atención inmediata)");
        System.out.println("    2. NARANJA (Muy urgente)");
        System.out.println("    3. AMARILLO (Urgente)");
        System.out.println("    4. VERDE  (Poco urgente)");
        System.out.println("    5. AZUL   (No urgente)");
        System.out.print("  Seleccione nivel (1-5): ");
        String op = scanner.nextLine().trim();

        String urgencia;
        switch (op) {
            case "1" -> urgencia = "ROJO";
            case "2" -> urgencia = "NARANJA";
            case "3" -> urgencia = "AMARILLO";
            case "4" -> urgencia = "VERDE";
            case "5" -> urgencia = "AZUL";
            default -> urgencia = "VERDE";
        }

        System.out.print("  Teléfono de contacto: ");
        String contacto = scanner.nextLine().trim();
        if (contacto.isEmpty())
            throw new IllegalArgumentException("El contacto no puede estar vacío");

        gestor.registrar(codigoTriage, nombre, identificacion, urgencia, contacto);
    }

    private static void buscarPorTriage() throws Exception {
        System.out.print("\n  Código de triage (ej: TRI-001): ");
        String codigo = scanner.nextLine().trim();
        if (codigo.isEmpty())
            throw new IllegalArgumentException("El código no puede estar vacío");
        gestor.buscarPorTriage(codigo);
    }

    private static void buscarPorNombre() {
        System.out.print("\n  Nombre del paciente: ");
        String nombre = scanner.nextLine().trim();
        gestor.buscarPorNombre(nombre);
    }

    private static void filtrar() {
        System.out.println("\n  Filtrar por:");
        System.out.println("    1. Nivel de urgencia (ROJO / NARANJA / AMARILLO / VERDE / AZUL)");
        System.out.println("    2. Identificación");
        System.out.print("  Elija (1-2): ");
        String op = scanner.nextLine().trim();

        if (op.equals("1")) {
            System.out.print("  Nivel de urgencia: ");
            gestor.filtrar("urgencia", scanner.nextLine().trim().toUpperCase());
        } else {
            System.out.print("  Número de identificación: ");
            gestor.filtrar("identificacion", scanner.nextLine().trim());
        }
    }

    private static void ordenar() {
        System.out.println("\n  Ordenar por:");
        System.out.println("    1. Nombre (A-Z)");
        System.out.println("    2. Código triage descendente");
        System.out.println("    3. Nivel de urgencia (A-Z)");
        System.out.println("    4. Código triage ascendente");
        System.out.print("  Elija (1-4): ");
        try {
            gestor.ordenar(Integer.parseInt(scanner.nextLine().trim()));
        } catch (NumberFormatException e) {
            gestor.ordenar(4);
        }
    }

    private static void cancelar() throws Exception {
        System.out.print("\n  Código de triage a cancelar (ej: TRI-002): ");
        String codigo = scanner.nextLine().trim();
        if (codigo.isEmpty())
            throw new IllegalArgumentException("El código no puede estar vacío");
        gestor.cancelar(codigo);
    }
}
