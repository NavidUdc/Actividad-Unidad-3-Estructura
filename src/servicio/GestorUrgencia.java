package servicio;

import modelo.PacienteUrgencia;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

public class GestorUrgencia {
    
    private final List<PacienteUrgencia>        elementos       = new ArrayList<>();
    private final Queue<PacienteUrgencia>       pendientes      = new LinkedList<>();
    private final Deque<PacienteUrgencia>       historial       = new ArrayDeque<>();
    private final Map<String, PacienteUrgencia> indicePorTriage = new HashMap<>();


    
    // 1. Registrar

    public void registrar(String codigoTriage, String nombre,
                          String identificacion, String urgencia,
                          String contacto) throws Exception {

        if (indicePorTriage.containsKey(codigoTriage.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Ya existe un paciente con el código de triage " + codigoTriage);
        }

        PacienteUrgencia paciente = new PacienteUrgencia(
                codigoTriage.toUpperCase(), nombre, identificacion, urgencia, contacto);

        elementos.add(paciente);
        pendientes.offer(paciente);
        indicePorTriage.put(paciente.getCodigoTriage(), paciente);

        System.out.println("\n  OK Paciente registrado:");
        System.out.println("  " + paciente);
    }

    // 2. Ver todo con for each
    public void verTodos() {
        System.out.println("\n  === LISTA GENERAL — List (" + elementos.size() + ") ===");
        if (elementos.isEmpty()) { System.out.println("  [Vacía]"); return; }
        elementos.forEach(p -> System.out.println("  " + p));
    }

    // 3. Ver los pedientes Queue con peek()
    
    public void verPendientes() {
        System.out.println("\n  === PENDIENTES — Queue (" + pendientes.size() + ") ===");
        if (pendientes.isEmpty()) { System.out.println("  [Cola vacía]"); return; }
        System.out.println("  Siguiente (peek): " + pendientes.peek());
        System.out.println("  ---");
        pendientes.forEach(p -> System.out.println("  " + p));
    }

    
    // 4. Atender al siguiente con Queue.poll() + Deque.push()

    public void atenderSiguiente() throws Exception {
        PacienteUrgencia atendido = pendientes.poll();
        if (atendido == null) {
            throw new IllegalStateException("No hay pacientes pendientes para atender.");
        }
        atendido.setUrgencia("ATENDIDO");
        historial.push(atendido);
        System.out.println("\n  OK Paciente atendido: " + atendido);
    }

    
    // 5. Ver historial con Deque con peek()

    public void verHistorial() {
        System.out.println("\n  === HISTORIAL — Deque (" + historial.size() + ") ===");
        if (historial.isEmpty()) { System.out.println("  [Historial vacío]"); return; }
        System.out.println("  Último atendido (peek): " + historial.peek());
        System.out.println("  ---");
        historial.forEach(p -> System.out.println("  " + p));
    }

    
    // 6. Buscar por triage con Map.get() O(1)
    public void buscarPorTriage(String codigoTriage) throws Exception {
        if (!indicePorTriage.containsKey(codigoTriage.toUpperCase())) {
            throw new IllegalArgumentException(
                    "No existe paciente con código de triage " + codigoTriage);
        }
        PacienteUrgencia encontrado = indicePorTriage.get(codigoTriage.toUpperCase());
        System.out.println("\n  OK Encontrado con Map.get():");
        System.out.println("  " + encontrado);
    }


    // 7. Buscar por nombre con Stream filter() + findFirst()
    public void buscarPorNombre(String nombre) {
        Optional<PacienteUrgencia> resultado = elementos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst();

        if (resultado.isPresent()) {
            System.out.println("\n  OK Encontrado con Stream.filter().findFirst():");
            System.out.println("  " + resultado.get());
        } else {
            System.out.println("\n  No se encontró paciente con nombre: " + nombre);
        }
    }

    // 8. Filtrar por Stream filter() + toList()
    // criterio: urgenciao o identificacion

    public void filtrar(String criterio, String valor) {
        List<PacienteUrgencia> filtrados;

        if (criterio.equalsIgnoreCase("urgencia")) {
            filtrados = elementos.stream()
                    .filter(p -> p.getUrgencia().equalsIgnoreCase(valor))
                    .toList();
        } else {
            filtrados = elementos.stream()
                    .filter(p -> p.getIdentificacion().equalsIgnoreCase(valor))
                    .toList();
        }

        System.out.println("\n  === FILTRO Stream.filter() — "
                + criterio + " = " + valor + " (" + filtrados.size() + ") ===");
        if (filtrados.isEmpty()) {
            System.out.println("  [Sin resultados]");
        } else {
            filtrados.forEach(p -> System.out.println("  " + p));
        }
    }

    
    // 9. Ordenar  Stream sorted() + Comparator
    
    public void ordenar(int criterio) {
        List<PacienteUrgencia> ordenados;
        String etiqueta;

        switch (criterio) {
            case 1 -> {
                ordenados = elementos.stream()
                        .sorted(Comparator.comparing(PacienteUrgencia::getNombre))
                        .toList();
                etiqueta = "NOMBRE (A-Z)";
            }
            case 2 -> {
                ordenados = elementos.stream()
                        .sorted(Comparator.comparing(PacienteUrgencia::getCodigoTriage).reversed())
                        .toList();
                etiqueta = "CÓDIGO TRIAGE descendente";
            }
            case 3 -> {
                ordenados = elementos.stream()
                        .sorted(Comparator.comparing(PacienteUrgencia::getUrgencia))
                        .toList();
                etiqueta = "URGENCIA (A-Z)";
            }
            default -> {
                ordenados = elementos.stream()
                        .sorted(Comparator.comparing(PacienteUrgencia::getCodigoTriage))
                        .toList();
                etiqueta = "CÓDIGO TRIAGE ascendente";
            }
        }

        System.out.println("\n  === ORDENADO Stream.sorted() — "
                + etiqueta + " (" + ordenados.size() + ") ===");
        ordenados.forEach(p -> System.out.println("  " + p));
    }

    // 10. Estadisticas Stream + Collectors + anyMatch/allMatch/noneMatch
    public void verEstadisticas() {
        System.out.println("\n  === ESTADÍSTICAS — Stream + Map ===");

        // groupingBy + counting por urgencia
        Map<String, Long> porUrgencia = elementos.stream()
                .collect(Collectors.groupingBy(
                        PacienteUrgencia::getUrgencia, Collectors.counting()));
        System.out.println("  Cantidad por nivel de urgencia:");
        porUrgencia.forEach((u, t) -> System.out.println("    " + u + ": " + t));

        // filter + count
        long pendientesCount = pendientes.size();
        long atendidosCount  = historial.size();

        System.out.println("  En cola (pendientes) : " + pendientesCount);
        System.out.println("  Atendidos (historial): " + atendidosCount);
        System.out.println("  Total registrados    : " + elementos.size());

        // anyMatch / allMatch / noneMatch
        boolean hayPendientes = !pendientes.isEmpty();
        boolean todosConTriage = elementos.stream()
                .allMatch(p -> p.getCodigoTriage() != null
                        && !p.getCodigoTriage().isBlank());
        boolean sinContactoVacio = elementos.stream()
                .noneMatch(p -> p.getContacto() == null
                        || p.getContacto().isBlank());

        System.out.println("  anyMatch  - Hay pendientes          : " + hayPendientes);
        System.out.println("  allMatch  - Todos tienen triage     : " + todosConTriage);
        System.out.println("  noneMatch - Sin contacto vacío      : " + sinContactoVacio);
    }

    // 11. Agrupamiento Collectors.groupingBy + Collectors.toMap
    
    public void verAgrupamientos() {
        System.out.println("\n  === AGRUPAMIENTO POR URGENCIA — Collectors.groupingBy ===");
        Map<String, List<PacienteUrgencia>> porUrgencia = elementos.stream()
                .collect(Collectors.groupingBy(PacienteUrgencia::getUrgencia));
        porUrgencia.forEach((urg, lista) -> {
            System.out.println("  Urgencia: " + urg + " (" + lista.size() + ")");
            lista.forEach(p -> System.out.println("    " + p));
        });

        // Collectors.toMap: reconstruir índice desde la lista
        Map<String, PacienteUrgencia> mapaReconstruido = elementos.stream()
                .collect(Collectors.toMap(
                        PacienteUrgencia::getCodigoTriage,
                        p -> p,
                        (existente, repetido) -> existente));
        System.out.println("\n  Collectors.toMap — mapa reconstruido: "
                + mapaReconstruido.size() + " entradas");

        // Map.entrySet() ordenado por clave
        System.out.println("  Map.entrySet() ordenado:");
        indicePorTriage.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.println("    "
                        + e.getKey() + " -> " + e.getValue().getNombre()));
    }

    
    // 12. Cancelar — Map.get() + removeIf() en Queue
    // Queda en list y map como evidencia

    public void cancelar(String codigoTriage) throws Exception {
        PacienteUrgencia paciente = indicePorTriage.get(codigoTriage.toUpperCase());
        if (paciente == null) {
            throw new IllegalArgumentException(
                    "No existe paciente con código de triage " + codigoTriage);
        }
        paciente.setUrgencia("CANCELADO");
        pendientes.removeIf(p -> p.getCodigoTriage().equalsIgnoreCase(codigoTriage));
        System.out.println("\n  OK Cancelado: " + paciente);
    }

    // 13. Deshacer Deque.pop() + Queue.offer()
    // Para devolver al paciente a la cola
    public void deshacer() throws Exception {
        if (historial.isEmpty()) {
            throw new IllegalStateException("No hay operaciones para deshacer.");
        }
        PacienteUrgencia ultimo = historial.pop();
        ultimo.setUrgencia("PENDIENTE");
        pendientes.offer(ultimo);
        System.out.println("\n  OK Deshecho. Paciente devuelto a la cola:");
        System.out.println("  " + ultimo);
    }

    // 14. Ver cantidades size() + Stream map()
    public void verCantidades() {
        System.out.println("\n  === CANTIDADES ===");
        System.out.println("  List  elementos.size()        : " + elementos.size());
        System.out.println("  Queue pendientes.size()       : " + pendientes.size());
        System.out.println("  Deque historial.size()        : " + historial.size());
        System.out.println("  Map   indicePorTriage.size()  : " + indicePorTriage.size());

        // Stream map() para extraer solo los códigos de triage
        List<String> codigos = elementos.stream()
                .map(PacienteUrgencia::getCodigoTriage)
                .toList();
        System.out.println("  Códigos triage (Stream.map): " + codigos);

        // groupingBy + counting por urgencia
        Map<String, Long> conteo = elementos.stream()
                .collect(Collectors.groupingBy(
                        PacienteUrgencia::getUrgencia, Collectors.counting()));
        conteo.forEach((u, t) ->
                System.out.println("  Stream conteo " + u + ": " + t));

        // Map.keySet() y Map.values()
        System.out.println("  Map.keySet()  : " + indicePorTriage.keySet());
        System.out.println("  Map.values()  : "
                + indicePorTriage.values().stream()
                .map(PacienteUrgencia::getNombre).toList());
    }
}
