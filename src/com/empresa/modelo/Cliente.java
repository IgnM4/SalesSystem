package com.empresa.modelo;

/**
 * Clase que representa un cliente de la empresa.
 * Incluye nombre, RUT, teléfono y dirección.
 */
public class Cliente {
    private String nombre;
    private String rut;
    private String telefono;
    private String direccion;

    public Cliente(String nombre, String rut, String telefono, String direccion) {
        this.nombre = nombre;
        this.rut = rut;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public String getRut() {
        return rut;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return String.format("Nombre: %s | RUT: %s | Teléfono: %s | Dirección: %s", nombre, rut, telefono, direccion);
    }
}
