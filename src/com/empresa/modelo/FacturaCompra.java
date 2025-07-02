package com.empresa.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una factura de ingreso de productos.
 * Incluye proveedor, productos, y opcionalmente vínculo a una orden de compra.
 * 
 * @author Ignacio
 */
public class FacturaCompra {

    private final String numeroFactura;
    private final LocalDate fecha;
    private final String proveedor;
    private final List<IngresoInventario> productosIngresados;
    private final OrdenCompra ordenAsociada;

    public FacturaCompra(String numeroFactura, String proveedor, OrdenCompra ordenAsociada) {
        this.numeroFactura = numeroFactura;
        this.proveedor = proveedor;
        this.ordenAsociada = ordenAsociada;
        this.fecha = LocalDate.now();
        this.productosIngresados = new ArrayList<>();
    }

    public FacturaCompra(String numeroFactura, String proveedor) {
        this(numeroFactura, proveedor, null);
    }

    public void agregarProducto(Producto producto, int cantidad) {
        productosIngresados.add(new IngresoInventario(producto, cantidad));
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getProveedor() {
        return proveedor;
    }

    public List<IngresoInventario> getProductosIngresados() {
        return productosIngresados;
    }

    public OrdenCompra getOrdenAsociada() {
        return ordenAsociada;
    }

    public double calcularTotalFactura() {
        double total = 0;
        for (IngresoInventario ingreso : productosIngresados) {
            total += ingreso.getProducto().getPrecioCompraConIVA() * ingreso.getCantidad();
        }
        return total;
    }

    public void mostrarDetalle() {
        System.out.printf("\nFactura: %s | Fecha: %s | Proveedor: %s\n", numeroFactura, fecha, proveedor);
        if (ordenAsociada != null) {
            System.out.println("Asociada a orden: " + ordenAsociada.getIdOrden());
        }
        for (IngresoInventario ingreso : productosIngresados) {
            Producto p = ingreso.getProducto();
            System.out.printf("Producto: %-15s | Cantidad: %d | PrecioCompra: $%.0f\n",
                    p.getNombre(), ingreso.getCantidad(), p.getPrecioCompraConIVA());
        }
        System.out.printf("TOTAL FACTURA: $%.0f\n", calcularTotalFactura());
    }

    public static class IngresoInventario {
        private final Producto producto;
        private final int cantidad;

        public IngresoInventario(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        public Producto getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }
    }
}
