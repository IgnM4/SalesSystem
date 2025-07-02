package com.empresa.modelo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Clase Singleton que representa el inventario de productos disponibles en la empresa.
 * Permite agregar, consultar, actualizar, exportar y buscar productos, así como
 * registrar entradas asociadas a facturas y consultar historial.
 * 
 * Usa una estructura interna ProductoInventariado para asociar producto + stock.
 * También maneja un historial de entradas para trazabilidad.
 * 
 * @author Ignacio
 */
public class Inventario {

    private static Inventario instancia = null;
    private final Map<String, ProductoInventariado> productos;
    private final List<FacturaCompra> historialEntradas;

    private Inventario() {
        productos = new HashMap<>();
        historialEntradas = new ArrayList<>();
    }

    public static Inventario getInstancia() {
        if (instancia == null) {
            instancia = new Inventario();
        }
        return instancia;
    }

    public void agregarProducto(String nombre, String tipo, double precioCompra, double comision, int cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor a 0.");
            return;
        }

        String clave = nombre.toLowerCase();
        if (productos.containsKey(clave)) {
            productos.get(clave).aumentarStock(cantidad);
        } else {
            Producto p = new Producto(nombre, tipo, precioCompra, comision);
            productos.put(clave, new ProductoInventariado(p, cantidad));
        }
    }

    public boolean reducirStock(String nombre, int cantidad) {
        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor a 0.");
            return false;
        }

        String clave = nombre.toLowerCase();
        if (productos.containsKey(clave)) {
            return productos.get(clave).reducirStock(cantidad);
        }
        return false;
    }

    public boolean tieneStockDisponible(String nombre, int cantidad) {
        ProductoInventariado pi = productos.get(nombre.toLowerCase());
        return pi != null && pi.getStock() >= cantidad;
    }

    public Producto getProducto(String nombre) {
        ProductoInventariado pi = productos.get(nombre.toLowerCase());
        return (pi != null) ? pi.getProducto() : null;
    }

    public void mostrarInventarioCompleto() {
        if (productos.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }

        System.out.println("\n======= INVENTARIO ACTUAL =======");
        for (ProductoInventariado pi : productos.values()) {
            Producto p = pi.getProducto();
            System.out.printf("Producto: %-15s | Tipo: %-10s | Stock: %3d | Venta: $%.0f\n",
                    p.getNombre(), p.getTipo(), pi.getStock(), p.getPrecioVentaFinal());
        }
    }

    public void buscarProducto(String criterio) {
        criterio = criterio.toLowerCase();
        boolean encontrado = false;

        System.out.println("\n--- Resultados de búsqueda ---");
        for (ProductoInventariado pi : productos.values()) {
            Producto p = pi.getProducto();
            if (p.getNombre().toLowerCase().contains(criterio) || p.getTipo().toLowerCase().contains(criterio)) {
                System.out.printf("Producto: %-15s | Tipo: %-10s | Stock: %3d | Venta: $%.0f\n",
                        p.getNombre(), p.getTipo(), pi.getStock(), p.getPrecioVentaFinal());
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron productos con ese criterio.");
        }
    }

    public void exportarCSV(String ruta) {
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write("Nombre,Tipo,PrecioCompra,Stock\n");
            for (ProductoInventariado pi : productos.values()) {
                Producto p = pi.getProducto();
                writer.write(String.format("%s,%s,%.0f,%d\n",
                        p.getNombre(), p.getTipo(), p.getPrecioCompraConIVA(), pi.getStock()));
            }
            System.out.println("Inventario exportado exitosamente a: " + ruta);
        } catch (IOException e) {
            System.out.println("Error al exportar CSV: " + e.getMessage());
        }
    }

    public void registrarEntradaConFactura(FacturaCompra factura) {
        for (FacturaCompra.IngresoInventario ingreso : factura.getProductosIngresados()) {
            Producto p = ingreso.getProducto();
            agregarProducto(p.getNombre(), p.getTipo(), p.getPrecioCompraConIVA(), p.getComisionPesos(), ingreso.getCantidad());
        }
        historialEntradas.add(factura);
    }

    public void mostrarHistorialPorFactura(String numero) {
        for (FacturaCompra f : historialEntradas) {
            if (f.getNumeroFactura().equalsIgnoreCase(numero)) {
                f.mostrarDetalle();
                return;
            }
        }
        System.out.println("Factura no encontrada.");
    }

    public void mostrarHistorialPorProducto(String nombreProducto) {
        boolean encontrado = false;
        for (FacturaCompra factura : historialEntradas) {
            for (FacturaCompra.IngresoInventario ingreso : factura.getProductosIngresados()) {
                if (ingreso.getProducto().getNombre().equalsIgnoreCase(nombreProducto)) {
                    System.out.printf("Factura: %s | Fecha: %s | Cantidad: %d\n",
                            factura.getNumeroFactura(), factura.getFecha(), ingreso.getCantidad());
                    encontrado = true;
                }
            }
        }
        if (!encontrado) {
            System.out.println("No se encontraron entradas para ese producto.");
        }
    }

    private static class ProductoInventariado {
        private final Producto producto;
        private int stock;

        public ProductoInventariado(Producto producto, int stockInicial) {
            this.producto = producto;
            this.stock = stockInicial;
        }

        public Producto getProducto() {
            return producto;
        }

        public int getStock() {
            return stock;
        }

        public void aumentarStock(int cantidad) {
            stock += cantidad;
        }

        public boolean reducirStock(int cantidad) {
            if (cantidad <= stock) {
                stock -= cantidad;
                return true;
            }
            return false;
        }
    }
}
