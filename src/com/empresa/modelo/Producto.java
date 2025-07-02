/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.empresa.modelo;

/**
 * Clase que representa un producto que vende la empresa.
 * Contiene atributos relacionados al precio de compra (con IVA),
 * comisión en pesos chilenos y cálculo automático del precio de venta.
 * 
 * @author Ignacio
 */
public class Producto {
    private String nombre;
    private String tipo; // Por ejemplo: "gas", "accesorio", etc.
    private double precioCompraConIVA;
    private double comisionPesos;
    private double precioVentaFinal;

    /**
     * Constructor para crear un producto con sus datos base.
     * Calcula automáticamente el precio de venta.
     * 
     * @param nombre Nombre del producto
     * @param tipo Tipo o categoría del producto
     * @param precioCompraConIVA Precio de compra incluyendo IVA
     * @param comisionPesos Comisión en pesos chilenos para el producto
     */
    public Producto(String nombre, String tipo, double precioCompraConIVA, double comisionPesos) {
        setNombre(nombre);
        setTipo(tipo);
        setPrecioCompraConIVA(precioCompraConIVA);
        setComisionPesos(comisionPesos);
        calcularPrecioVenta(); // Se calcula automáticamente
    }

    // ======================== VALIDACIONES Y SETTERS ========================

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo del producto no puede estar vacío.");
        }
        this.tipo = tipo;
    }

    public void setPrecioCompraConIVA(double precioCompraConIVA) {
        if (precioCompraConIVA <= 0) {
            throw new IllegalArgumentException("El precio de compra debe ser mayor a cero.");
        }
        this.precioCompraConIVA = precioCompraConIVA;
    }

    public void setComisionPesos(double comisionPesos) {
        if (comisionPesos < 0) {
            throw new IllegalArgumentException("La comisión no puede ser negativa.");
        }
        this.comisionPesos = comisionPesos;
    }

    /**
     * Calcula el precio de venta como la suma del precio de compra con IVA y la comisión.
     */
    public void calcularPrecioVenta() {
        this.precioVentaFinal = this.precioCompraConIVA + this.comisionPesos;
    }

    // ======================== GETTERS ========================

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPrecioCompraConIVA() {
        return precioCompraConIVA;
    }

    public double getComisionPesos() {
        return comisionPesos;
    }

    public double getPrecioVentaFinal() {
        return precioVentaFinal;
    }

    // ======================== MÉTODOS ADICIONALES ========================

    /**
     * Muestra los detalles del producto en formato legible.
     */
    public void mostrarDetalleProducto() {
        System.out.println("Producto: " + nombre);
        System.out.println("Tipo: " + tipo);
        System.out.printf("Precio compra c/IVA: $%.0f\n", precioCompraConIVA);
        System.out.printf("Comisión: $%.0f\n", comisionPesos);
        System.out.printf("Precio venta final: $%.0f\n", precioVentaFinal);
    }
    // Métodos simplificados para compatibilidad
public double getPrecioCompra() {
    return getPrecioCompraConIVA();
}

public double getComision() {
    return getComisionPesos();
}

}
