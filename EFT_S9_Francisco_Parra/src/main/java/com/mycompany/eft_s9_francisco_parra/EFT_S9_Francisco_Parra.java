/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.eft_s9_francisco_parra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author fparraa 
 **/

public class EFT_S9_Francisco_Parra {

    private static final Scanner scanner = new Scanner(System.in);
    private static final int FILAS = 3;
    private static final int COLUMNAS = 10;

    private static final String[][] vip = new String[FILAS][COLUMNAS];
    private static final String[][] palco = new String[FILAS][COLUMNAS];
    private static final String[][] plateaBaja = new String[FILAS][COLUMNAS];
    private static final String[][] plateaAlta = new String[FILAS][COLUMNAS];
    private static final String[][] galeria = new String[FILAS][COLUMNAS];

    private static final double PRECIO_VIP = 30000;
    private static final double PRECIO_PALCO = 25000;
    private static final double PRECIO_PLATEA_BAJA = 20000;
    private static final double PRECIO_PLATEA_ALTA = 15000;
    private static final double PRECIO_GALERIA = 10000;

    private static final List<Entrada> carritoCompras = new ArrayList<>();
    private static final List<Entrada> reservas = new ArrayList<>();
    private static final List<String> promociones = List.of("10% Niños", "20% Mujeres", "15% Estudiantes", "25% Tercera Edad");
    private static double ingresosTotales = 0;
    private static final Timer timer = new Timer();

    private static int contadorEntradas = 1;
    private static int contadorClientes = 1;

    public static void main(String[] args) {
        inicializarTeatro();
        inicio();
    }

    private static void inicializarTeatro() {
        for (int i = 0; i < FILAS; i++) {
            Arrays.fill(vip[i], "Disponible");
            Arrays.fill(palco[i], "Disponible");
            Arrays.fill(plateaBaja[i], "Disponible");
            Arrays.fill(plateaAlta[i], "Disponible");
            Arrays.fill(galeria[i], "Disponible");
        }
    }

  private static void inicio() {
        System.out.println("\n=== Teatro Moro ==="); 
        System.out.println("\n=== Sistema de Gestión de Entradas ===");
        menu();
  }
   
  private static void menu() {
        int opcion;
        do {
            System.out.println("\n=== Menu de Opciones  ===");
            System.out.println("1. Comprar Entrada");
            System.out.println("2. Reservar Entrada");
            System.out.println("3. Confirmar Reserva");
            System.out.println("4. Visualizar Promociones");
            System.out.println("5. Visualizar Resumen de Ventas");
            System.out.println("6. Buscar entrada");
            System.out.println("7. Modificar Entrada Comprada");
            System.out.println("8. Imprimir Boleta y Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEnteroConValidacion();

            switch (opcion) {
                case 1 -> comprarEntrada(false);
                case 2 -> comprarEntrada(true);
                case 3 -> confirmarReserva();
                case 4 -> mostrarPromociones();
                case 5 -> visualizarResumenVentas();
                case 6 -> buscarEntrada();
                case 7 -> modificarEntrada();
                case 8 -> imprimirBoleta();
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 7);
    }
    
  private static void comprarEntrada(boolean esReserva) {
        int zona = solicitarZona();
        if (zona == -1) return;
        mostrarAsientos(zona);

        int fila = solicitarFila(zona);
        if (fila == -1) return;

        int columna = solicitarColumna();
        if (columna == -1) return;

        if (!asientoDisponible(zona, fila, columna)) {
            System.out.println("Asiento no disponible.");
            return;
        }

        String nombre = solicitarNombre();
        if (nombre.equals("")) return;

        String genero = solicitarGenero();
        if (genero.equals("")) return;

        int edad = solicitarEdad();
        if (edad == -1) return;

        boolean estudiante = solicitarEstudiante();

        Cliente cliente = new Cliente(nombre, edad, estudiante, genero);

        double precio = obtenerPrecioZona(zona);
        double descuento = obtenerDescuento(cliente);
        double precioFinal = precio * (1 - descuento);

        Entrada entrada = new Entrada(zona, fila, columna, precio, descuento, precioFinal, cliente);

        if (esReserva) {
            reservas.add(entrada);
            bloquearAsiento(zona, fila, columna, "Reservado");
            programarExpiracionReserva(entrada);
            System.out.println("Entrada reservada por 2 minutos.\n" + entrada);
        } else {
            carritoCompras.add(entrada);
            bloquearAsiento(zona, fila, columna, "Comprado");
            ingresosTotales += precioFinal;
            System.out.println("Entrada comprada exitosamente.\n" + entrada);
        }
    }

    private static String solicitarNombre() {
        for (int i = 0; i < 2; i++) {
            System.out.print("Ingrese nombre del cliente: ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) return nombre;
            System.out.println("Nombre inválido. No puede estar vacío.");
        }
        return "";
    }

    private static String solicitarGenero() {
        for (int i = 0; i < 2; i++) {
            System.out.print("Ingrese género (M/F): ");
            String genero = scanner.nextLine().trim().toUpperCase();
            if (genero.equals("M") || genero.equals("F")) return genero;
            System.out.println("Género inválido. Ingrese 'M' o 'F'.");
        }
        return "";
    }

    private static int solicitarZona() {
        for (int i = 0; i < 2; i++) {
            System.out.println("Seleccione zona:\n1. VIP\n2. Palco\n3. Platea Baja\n4. Platea Alta\n5. Galería");
            System.out.print("Seleccione id (numero) de la Zona: ");
            int zona = leerEnteroConValidacion();
            if (zona >= 1 && zona <= 5) return zona;
            System.out.println("Zona inválida.");
        }
        return -1;
    }

    private static int solicitarFila(int zona) {
        for (int i = 0; i < 2; i++) {
            System.out.print("Seleccione fila (letra): ");
            String letra = scanner.nextLine().trim().toUpperCase();
            int fila = convertirFilaALetra(zona, letra);
            if (fila != -1) return fila;
            System.out.println("Fila inválida.");
        }
        return -1;
    }

    private static int solicitarColumna() {
        for (int i = 0; i < 2; i++) {
            System.out.print("Seleccione columna (1-10): ");
            int col = leerEnteroConValidacion() - 1;
            if (col >= 0 && col < COLUMNAS) return col;
            System.out.println("Columna inválida.");
        }
        return -1;
    }

    private static int solicitarEdad() {
        for (int i = 0; i < 2; i++) {
            System.out.print("Ingrese edad: ");
            int edad = leerEnteroConValidacion();
            if (edad >= 0 && edad <= 120) return edad;
            System.out.println("Edad inválida.");
        }
        return -1;
    }

    private static boolean solicitarEstudiante() {
        for (int i = 0; i < 2; i++) {
            System.out.print("¿Es estudiante? (s/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("s")) return true;
            if (input.equals("n")) return false;
            System.out.println("Entrada inválida.");
        }
        return false;
    }

    private static double obtenerDescuento(Cliente c) {
        List<Double> descuentos = new ArrayList<>();
        if (c.esNino) descuentos.add(0.10);
        if (c.genero.equals("F")) descuentos.add(0.20);
        if (c.esEstudiante) descuentos.add(0.15);
        if (c.edad >= 60) descuentos.add(0.25);
        return descuentos.stream().max(Double::compare).orElse(0.0);
    }

    private static double obtenerPrecioZona(int zona) {
        return switch (zona) {
            case 1 -> PRECIO_VIP;
            case 2 -> PRECIO_PALCO;
            case 3 -> PRECIO_PLATEA_BAJA;
            case 4 -> PRECIO_PLATEA_ALTA;
            case 5 -> PRECIO_GALERIA;
            default -> 0;
        };
    }

    private static String obtenerNombreZona(int zona) {
        return switch (zona) {
            case 1 -> "VIP";
            case 2 -> "Palco";
            case 3 -> "Platea Baja";
            case 4 -> "Platea Alta";
            case 5 -> "Galería";
            default -> "Desconocida";
        };
    }

    private static String[][] obtenerZona(int zona) {
        return switch (zona) {
            case 1 -> vip;
            case 2 -> palco;
            case 3 -> plateaBaja;
            case 4 -> plateaAlta;
            case 5 -> galeria;
            default -> new String[0][0];
        };
    }

    private static boolean asientoDisponible(int zona, int fila, int columna) {
        return obtenerZona(zona)[fila][columna].equals("Disponible");
    }

    private static void bloquearAsiento(int zona, int fila, int columna, String estado) {
        obtenerZona(zona)[fila][columna] = estado;
    }

    private static void mostrarAsientos(int zona) {
        String[][] area = obtenerZona(zona);
        System.out.println("Asientos en zona " + obtenerNombreZona(zona) + ":");
        for (int i = 0; i < FILAS; i++) {
            String letra = obtenerLetraFila(zona, i);
            System.out.print("Fila " + letra + ": ");
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(area[i][j].equals("Disponible") ? "[" + (j + 1) + "] " : "[X] ");
            }
            System.out.println();
        }
    }

    private static void mostrarPromociones() {
        System.out.println("Promociones disponibles:");
        promociones.forEach(p -> System.out.println("- " + p));
    }

    private static void confirmarReserva() {
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas activas.");
            return;
        }
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println((i + 1) + ". " + reservas.get(i));
        }
        System.out.print("Seleccione reserva a confirmar: ");
        int index = leerEnteroConValidacion() - 1;
        if (index < 0 || index >= reservas.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Entrada entrada = reservas.remove(index);
        carritoCompras.add(entrada);
        bloquearAsiento(entrada.zona, entrada.fila, entrada.columna, "Comprado");
        ingresosTotales += entrada.precioFinal;
        System.out.println("Reserva confirmada como compra.");
    }

    private static void visualizarResumenVentas() {
    System.out.println("Resumen de ventas:");
    for (Entrada e : carritoCompras) {
        System.out.println(e);
    }

    // Inicializar acumuladores por zona
    int[] cantidadPorZona = new int[5];
    double[] ingresosPorZona = new double[5];

    // Inicializar descuentos acumulados por tipo
    double totalDescuentoNinos = 0;
    double totalDescuentoMujeres = 0;
    double totalDescuentoEstudiantes = 0;
    double totalDescuentoTerceraEdad = 0;

    for (Entrada e : carritoCompras) {
        int zonaIndex = e.zona - 1;
        cantidadPorZona[zonaIndex]++;
        ingresosPorZona[zonaIndex] += e.precioFinal;

        Cliente c = e.cliente;
        double montoDescontado = e.precioBase * e.descuento;

        if (c.esNino && e.descuento == 0.10) totalDescuentoNinos += montoDescontado;
        else if (c.genero.equals("F") && e.descuento == 0.20) totalDescuentoMujeres += montoDescontado;
        else if (c.esEstudiante && e.descuento == 0.15) totalDescuentoEstudiantes += montoDescontado;
        else if (c.edad >= 60 && e.descuento == 0.25) totalDescuentoTerceraEdad += montoDescontado;
    }

    System.out.println("\nCantidad de entradas por zona:");
    for (int i = 0; i < cantidadPorZona.length; i++) {
        System.out.println("- " + obtenerNombreZona(i + 1) + ": " + cantidadPorZona[i]);
    }

    System.out.println("\nIngresos por zona:");
    for (int i = 0; i < ingresosPorZona.length; i++) {
        System.out.println("- " + obtenerNombreZona(i + 1) + ": $" + ingresosPorZona[i]);
    }

    System.out.println("\nTotal descontado por tipo de descuento:");
    System.out.println("- Niños (10%): $" + totalDescuentoNinos);
    System.out.println("- Mujeres (20%): $" + totalDescuentoMujeres);
    System.out.println("- Estudiantes (15%): $" + totalDescuentoEstudiantes);
    System.out.println("- Tercera Edad (25%): $" + totalDescuentoTerceraEdad);

    System.out.println("\nTotal Ingresos Finales (con descuentos aplicados): $" + ingresosTotales);
}

    private static void programarExpiracionReserva(Entrada entrada) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (reservas.remove(entrada)) {
                    bloquearAsiento(entrada.zona, entrada.fila, entrada.columna, "Disponible");
                    System.out.println("Reserva expirada: " + entrada);
                }
            }
        }, 120000);
    }

    private static int leerEnteroConValidacion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String obtenerLetraFila(int zona, int fila) {
        return switch (zona) {
            case 1 -> String.valueOf((char) ('A' + fila));
            case 2 -> String.valueOf((char) ('D' + fila));
            case 3 -> String.valueOf((char) ('G' + fila));
            case 4 -> String.valueOf((char) ('J' + fila));
            case 5 -> String.valueOf((char) ('M' + fila));
            default -> "?";
        };
    }

    private static int convertirFilaALetra(int zona, String letra) {
        char l = letra.charAt(0);
        return switch (zona) {
            case 1 -> (l >= 'A' && l <= 'C') ? l - 'A' : -1;
            case 2 -> (l >= 'D' && l <= 'F') ? l - 'D' : -1;
            case 3 -> (l >= 'G' && l <= 'I') ? l - 'G' : -1;
            case 4 -> (l >= 'J' && l <= 'L') ? l - 'J' : -1;
            case 5 -> (l >= 'M' && l <= 'O') ? l - 'M' : -1;
            default -> -1;
        };
    }

    static class Cliente {
        int idCliente;
        String nombre;
        int edad;
        boolean esEstudiante;
        String genero;
        boolean esNino;

        Cliente(String nombre, int edad, boolean esEstudiante, String genero) {
            this.idCliente = contadorClientes++;
            this.nombre = nombre;
            this.edad = edad;
            this.esEstudiante = esEstudiante;
            this.genero = genero;
            this.esNino = edad < 12;
        }

        @Override
        public String toString() {
            return "ID Cliente: " + idCliente + ", " + nombre + ", Edad: " + edad + ", Estudiante: " + (esEstudiante ? "Sí" : "No") + ", Género: " + genero;
        }
    }

    private static void buscarEntrada() {
    if (carritoCompras.isEmpty()) {
        System.out.println("No hay compras registradas.");
        return;
    }

    System.out.print("Ingrese nombre o ID de cliente: ");
    String input = scanner.nextLine().trim().toLowerCase();

    boolean encontrado = false;
    for (Entrada e : carritoCompras) {
        String nombreCliente = e.cliente.nombre.toLowerCase();
        String idCliente = String.valueOf(e.cliente.idCliente);

        if (nombreCliente.contains(input) || idCliente.equals(input)) {
            System.out.println(e);
            encontrado = true;
        }
    }

    if (!encontrado) {
        System.out.println("No se encontraron coincidencias.");
        return;
    }
    return;
}
    
     private static void modificarEntrada() {
        if (carritoCompras.isEmpty()) {
            System.out.println("No hay entradas para modificar.");
            return;
        }
        for (int i = 0; i < carritoCompras.size(); i++) {
            System.out.println((i + 1) + ". " + carritoCompras.get(i));
        }
        System.out.print("Seleccione entrada a modificar: ");
        int index = leerEnteroConValidacion() - 1;
        if (index < 0 || index >= carritoCompras.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Entrada entrada = carritoCompras.remove(index);
        bloquearAsiento(entrada.zona, entrada.fila, entrada.columna, "Disponible");
        ingresosTotales -= entrada.precioFinal;
        comprarEntrada(false);
    }

    private static void imprimirBoleta() {
    System.out.println("\n=== Boleta ===");
    if (carritoCompras.isEmpty()) {
        System.out.println("No hay ventas registradas.");
        return;
    }

    int boleta = 0;
    for (Entrada e : carritoCompras) {
        var c = e.cliente;
        
        System.out.println("Boleta N°" + (++boleta));
        System.out.println("Cliente:" + " id = " + c.idCliente + " |  Nombre =" + c.nombre);
        System.out.println("Ubicación: " + obtenerNombreZona(e.zona) + ", Fila: " + obtenerLetraFila(e.zona, e.fila) + ", Columna: " + (e.columna + 1));
        System.out.println("Precio Base: $" + e.precioBase);
        System.out.println("Descuento Aplicado: $" + (e.precioBase * e.descuento));

        // Mostrar tipo de descuento
        String tipoDescuento = "Sin descuento";
        
        if (c.esNino && e.descuento == 0.10) tipoDescuento = "Niños (10%)";
        else if (c.genero.equals("F") && e.descuento == 0.20) tipoDescuento = "Mujeres (20%)";
        else if (c.esEstudiante && e.descuento == 0.15) tipoDescuento = "Estudiantes (15%)";
        else if (c.edad >= 60 && e.descuento == 0.25) tipoDescuento = "Tercera Edad (25%)";

        System.out.println("Tipo de Descuento: " + tipoDescuento);
        System.out.println("Precio Final: $" + e.precioFinal);
        System.out.println("----------------------------");
    }
    System.out.println("Gracias por su compra. Hasta pronto!");
}

    static class Entrada {
        int idEntrada;
        int zona, fila, columna;
        double precioBase, descuento, precioFinal;
        Cliente cliente;

        Entrada(int zona, int fila, int columna, double precioBase, double descuento, double precioFinal, Cliente cliente) {
            this.idEntrada = contadorEntradas++;
            this.zona = zona;
            this.fila = fila;
            this.columna = columna;
            this.precioBase = precioBase;
            this.descuento = descuento;
            this.precioFinal = precioFinal;
            this.cliente = cliente;
        }

        @Override
        public String toString() {
            return "ID Entrada: " + idEntrada + ", Zona: " + obtenerNombreZona(zona) + ", Fila: " + obtenerLetraFila(zona, fila) + ", Columna: " + (columna + 1) + ", Precio Final: $" + precioFinal + ", Cliente: [" + cliente + "]";
        }
    }
}
