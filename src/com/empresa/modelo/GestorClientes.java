package com.empresa.modelo;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Clase singleton que gestiona el registro de clientes.
 * Permite registrar, buscar, listar y eliminar clientes por RUT.
 * Además permite importar clientes desde un archivo Excel.
 */
public class GestorClientes {
    private static final GestorClientes instancia = new GestorClientes();
    private final Map<String, Cliente> clientesPorRut;

    private GestorClientes() {
        clientesPorRut = new HashMap<>();
    }

    public static GestorClientes getInstancia() {
        return instancia;
    }

    public void registrarCliente(Cliente cliente) {
        if (clientesPorRut.containsKey(cliente.getRut())) {
            System.out.println("Ya existe un cliente con este RUT: " + cliente.getRut());
        } else {
            clientesPorRut.put(cliente.getRut(), cliente);
            System.out.println("Cliente registrado: " + cliente.getNombre());
        }
    }

    public Cliente buscarClientePorRUT(String rut) {
        return clientesPorRut.get(rut);
    }

    public void listarClientes() {
        if (clientesPorRut.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        System.out.println("\n--- LISTA DE CLIENTES ---");
        for (Cliente c : clientesPorRut.values()) {
            System.out.println(c);
        }
    }

    public void eliminarCliente(String rut) {
        if (clientesPorRut.remove(rut) != null) {
            System.out.println("Cliente eliminado correctamente.");
        } else {
            System.out.println("No se encontró un cliente con ese RUT.");
        }
    }

    /**
     * Importa clientes desde un archivo Excel (.xlsx) con columnas:
     * Nombre cuenta | Teléfono (ignorado) | Dirección | Día Último (ignorado)
     *
     * @param rutaArchivo Ruta al archivo Excel
     */
    public void importarClientesDesdeExcel(String rutaArchivo) {
        try (FileInputStream fis = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet hoja = workbook.getSheetAt(0);
            int contador = 0;

            for (int i = 1; i <= hoja.getLastRowNum(); i++) { // Saltar encabezado
                Row fila = hoja.getRow(i);
                if (fila == null) continue;

                Cell celdaNombre = fila.getCell(0); // Columna 0: Nombre cuenta
                Cell celdaDireccion = fila.getCell(2); // Columna 2: Dirección

                String nombre = obtenerTextoDesdeCelda(celdaNombre);
                String direccion = obtenerTextoDesdeCelda(celdaDireccion);
                String rutFicticio = "GEN" + i;
                String telefono = ""; // Ignorado

                if (!nombre.isEmpty() && !direccion.isEmpty()) {
                    Cliente nuevo = new Cliente(nombre, rutFicticio, telefono, direccion);
                    registrarCliente(nuevo);
                    contador++;
                }
            }

            System.out.println("✅ Importación finalizada. Clientes importados: " + contador);

        } catch (IOException e) {
            System.out.println("❌ Error al leer el archivo Excel: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para extraer texto desde una celda (soporta String y Numeric).
     */
    private String obtenerTextoDesdeCelda(Cell celda) {
        if (celda == null) return "";
        return switch (celda.getCellType()) {
            case STRING -> celda.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) celda.getNumericCellValue()).trim();
            case BOOLEAN -> String.valueOf(celda.getBooleanCellValue());
            default -> "";
        };
    }
}
