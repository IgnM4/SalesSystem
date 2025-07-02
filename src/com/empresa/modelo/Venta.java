package com.empresa.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que representa una venta, ya sea en local o domicilio.
 * Permite agregar productos, aplicar descuentos seleccionados y calcular totales.
 * Incluye fecha/hora, cliente asociado y permite múltiples productos en una misma venta.
 * 
 * @author Ignacio
 */
public class Venta {

    private final List<Producto> productosVendidos;
    private final List<Integer> descuentosAplicados; // en pesos
    private final String formatoVenta; // "local" o "domicilio"
    private final LocalDateTime fechaHora;
    private final Cliente cliente; // Cliente asociado a la venta

    /**
     * Constructor que define el tipo de venta y el cliente.
     * 
     * @param formatoVenta "local" o "domicilio"
     * @param cliente Cliente que realiza la compra
     */
    public Venta(String formatoVenta, Cliente cliente) {
        this.formatoVenta = formatoVenta.toLowerCase();
        this.productosVendidos = new ArrayList<>();
        this.descuentosAplicados = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
        this.cliente = cliente;
    }

    /**
     * Agrega un producto a la venta, mostrando descuentos disponibles y solicitando selección.
     *
     * @param producto Producto a vender
     * @param sc Scanner para entrada del usuario
     */
    public void agregarProducto(Producto producto, Scanner sc) {
        int[] descuentos = Descuento.obtenerDescuentosDisponibles(producto, formatoVenta);
        int descuentoSeleccionado = 0;

        System.out.println("\nProducto: " + producto.getNombre());
        producto.mostrarDetalleProducto();

        if (descuentos.length == 1) {
            descuentoSeleccionado = descuentos[0];
            System.out.println("Descuento fijo aplicado: $" + descuentoSeleccionado);
        } else if (descuentos.length > 1) {
            System.out.println("Seleccione un descuento:");
            for (int i = 0; i < descuentos.length; i++) {
                System.out.printf("[%d] $%d\n", i, descuentos[i]);
            }
            System.out.print("Ingrese opción: ");
            int opcion = sc.nextInt();
            sc.nextLine(); // consumir el salto de línea pendiente
            if (opcion >= 0 && opcion < descuentos.length) {
                descuentoSeleccionado = descuentos[opcion];
            } else {
                System.out.println("Opción inválida. No se aplicó descuento.");
            }
        } else {
            System.out.println("No hay descuentos disponibles para este producto.");
        }

        productosVendidos.add(producto);
        descuentosAplicados.add(descuentoSeleccionado);
    }

    /**
     * Calcula el total sin descuentos.
     */
    public double calcularTotalBruto() {
        double total = 0;
        for (Producto p : productosVendidos) {
            total += p.getPrecioVentaFinal();
        }
        return total;
    }

    /**
     * Suma todos los descuentos aplicados.
     */
    public int calcularTotalDescuento() {
        int totalDescuento = 0;
        for (int d : descuentosAplicados) {
            totalDescuento += d;
        }
        return totalDescuento;
    }

    /**
     * Total a pagar con descuentos aplicados.
     */
    public double calcularTotalNeto() {
        return calcularTotalBruto() - calcularTotalDescuento();
    }

    /**
     * Muestra el resumen completo de la venta.
     */
    public void mostrarResumenVenta() {
        System.out.println("\n====== RESUMEN DE VENTA (" + formatoVenta.toUpperCase() + ") ======");
        System.out.println("Cliente: " + (cliente != null ? cliente.getNombre() + " (" + cliente.getRut() + ")" : "No registrado"));
        for (int i = 0; i < productosVendidos.size(); i++) {
            Producto p = productosVendidos.get(i);
            int d = descuentosAplicados.get(i);
            System.out.printf("Producto: %-15s | Precio: $%.0f | Descuento: $%d | Final: $%.0f\n",
                    p.getNombre(), p.getPrecioVentaFinal(), d, p.getPrecioVentaFinal() - d);
        }
        System.out.println("---------------------------------------------");
        System.out.printf("TOTAL BRUTO:     $%.0f\n", calcularTotalBruto());
        System.out.printf("TOTAL DESCUENTO: $%d\n", calcularTotalDescuento());
        System.out.printf("TOTAL A PAGAR:   $%.0f\n", calcularTotalNeto());
        System.out.println("Fecha/Hora:      " + fechaHora);
        System.out.println("Cliente: " + cliente.getNombre() + " | RUT: " + cliente.getRut());

    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getFormatoVenta() {
        return formatoVenta;
    }

    public List<Producto> getProductosVendidos() {
        return productosVendidos;
    }

    public List<Integer> getDescuentosAplicados() {
        return descuentosAplicados;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
