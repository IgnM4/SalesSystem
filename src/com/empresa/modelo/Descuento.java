package com.empresa.modelo;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que aplica descuentos fijos en pesos chilenos según el nombre del producto
 * y el formato de venta ("local" o "domicilio").
 * 
 * Si hay más de un valor posible, el sistema debe recibir el índice del descuento elegido.
 * No hay selección aleatoria.
 * 
 * Por ejemplo:
 * gas 15kg - local → {5500, 6000} → usuario debe elegir uno (0 ó 1)
 * gas 11kg - domicilio → {1500, 3500} → usuario elige índice
 * 
 * @author Ignacio
 */
public class Descuento {

    private static final Map<String, int[]> descuentoLocal = new HashMap<>();
    private static final Map<String, int[]> descuentoDomicilio = new HashMap<>();

    static {
        // LOCAL
        descuentoLocal.put("gas 5kg", new int[]{2500});
        descuentoLocal.put("gas 11kg", new int[]{4000});
        descuentoLocal.put("gas 15kg", new int[]{5500, 6000});

        // DOMICILIO
        descuentoDomicilio.put("gas 5kg", new int[]{700, 1500});
        descuentoDomicilio.put("gas 11kg", new int[]{1500, 3500});
        descuentoDomicilio.put("gas 15kg", new int[]{3000, 5000});
        descuentoDomicilio.put("gas 45kg", new int[]{6000, 7000});
    }

    /**
     * Retorna los descuentos posibles para un producto y formato.
     *
     * @param producto Producto
     * @param formatoVenta "local" o "domicilio"
     * @return Arreglo de descuentos disponibles (puede tener 1 o más)
     */
    public static int[] obtenerDescuentosDisponibles(Producto producto, String formatoVenta) {
        String nombre = producto.getNombre().toLowerCase();
        if (formatoVenta.equalsIgnoreCase("local")) {
            return descuentoLocal.getOrDefault(nombre, new int[]{0});
        } else if (formatoVenta.equalsIgnoreCase("domicilio")) {
            return descuentoDomicilio.getOrDefault(nombre, new int[]{0});
        }
        return new int[]{0};
    }

    /**
     * Aplica el descuento seleccionado por el usuario según su índice en la lista disponible.
     *
     * @param producto Producto
     * @param formatoVenta "local" o "domicilio"
     * @param opcionSeleccionada Índice del descuento elegido
     * @return Precio final con descuento aplicado
     */
    public static double aplicarDescuentoSeleccionado(Producto producto, String formatoVenta, int opcionSeleccionada) {
        int[] descuentos = obtenerDescuentosDisponibles(producto, formatoVenta);

        if (opcionSeleccionada >= 0 && opcionSeleccionada < descuentos.length) {
            double descuento = descuentos[opcionSeleccionada];
            return producto.getPrecioVentaFinal() - descuento;
        }

        return producto.getPrecioVentaFinal(); // No aplica descuento si selección inválida
    }
}
