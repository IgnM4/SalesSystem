package com.empresa.modelo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Singleton que gestiona el historial completo de ventas realizadas.
 * Permite agregar ventas, consultar el historial y exportarlo a CSV.
 */
public class HistorialVentas {

    private static HistorialVentas instancia = null;
    private final List<Venta> ventas;

    private HistorialVentas() {
        ventas = new ArrayList<>();
    }

    public static HistorialVentas getInstancia() {
        if (instancia == null) {
            instancia = new HistorialVentas();
        }
        return instancia;
    }

    public void agregarVenta(Venta venta) {
        ventas.add(venta);
    }

    public void mostrarHistorial() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        System.out.println("\n===== HISTORIAL DE VENTAS =====");
        for (Venta venta : ventas) {
            venta.mostrarResumenVenta();
            System.out.println("---------------------------------------------");
        }
    }

    public void exportarCSV(String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write("FechaHora,TipoVenta,Producto,PrecioUnitario,Descuento,TotalLinea\n");

            for (Venta venta : ventas) {
                List<Producto> productos = venta.getProductosVendidos();
                List<Integer> descuentos = venta.getDescuentosAplicados();

                for (int i = 0; i < productos.size(); i++) {
                    Producto p = productos.get(i);
                    int desc = descuentos.get(i);
                    double total = p.getPrecioVentaFinal() - desc;

                    writer.write(String.format("%s,%s,%s,%.0f,%d,%.0f\n",
                            venta.getFechaHora(), venta.getFormatoVenta(),
                            p.getNombre(), p.getPrecioVentaFinal(), desc, total));
                }
            }

            System.out.println("Historial exportado exitosamente a: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al exportar CSV: " + e.getMessage());
        }
    }
}
