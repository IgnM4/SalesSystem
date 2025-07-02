package com.empresa.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una orden de compra emitida por la empresa.
 * Incluye proveedor, fecha y lista de productos con cantidades solicitadas.
 * Puede generar una factura asociada.
 * 
 * @author Ignacio
 */
public class OrdenCompra {

    private final String idOrden;
    private final String proveedor;
    private final LocalDate fechaEmision;
    private final List<ItemOrden> items;

    public OrdenCompra(String idOrden, String proveedor) {
        this.idOrden = idOrden;
        this.proveedor = proveedor;
        this.fechaEmision = LocalDate.now();
        this.items = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int cantidad) {
        items.add(new ItemOrden(producto, cantidad));
    }

    public String getIdOrden() {
        return idOrden;
    }

    public String getProveedor() {
        return proveedor;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public List<ItemOrden> getItems() {
        return items;
    }

    public double calcularTotalEstimado() {
        double total = 0;
        for (ItemOrden item : items) {
            total += item.getProducto().getPrecioCompraConIVA() * item.getCantidad();
        }
        return total;
    }

    /**
     * Convierte esta orden en una factura (crea instancia de FacturaCompra).
     */
    public FacturaCompra generarFacturaDesdeOrden(String numeroFactura) {
        FacturaCompra factura = new FacturaCompra(numeroFactura, proveedor, this);
        for (ItemOrden item : items) {
            factura.agregarProducto(item.getProducto(), item.getCantidad());
        }
        return factura;
    }

    public static class ItemOrden {
        private final Producto producto;
        private final int cantidad;

        public ItemOrden(Producto producto, int cantidad) {
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
