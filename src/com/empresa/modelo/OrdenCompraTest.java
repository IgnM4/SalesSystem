package com.empresa.modelo;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link OrdenCompra}.
 */
public class OrdenCompraTest {

    @Test
    public void calcularTotalEstimado_sumaPreciosCompra() {
        Producto prod1 = new Producto("Gas 5kg", "gas", 10000, 1000);
        Producto prod2 = new Producto("Regulador", "accesorio", 5000, 500);

        OrdenCompra orden = new OrdenCompra("O001", "Proveedor");
        orden.agregarProducto(prod1, 2); // 2 * 10000
        orden.agregarProducto(prod2, 3); // 3 * 5000

        double esperado = 2 * prod1.getPrecioCompraConIVA() + 3 * prod2.getPrecioCompraConIVA();
        assertEquals(esperado, orden.calcularTotalEstimado(), 0.0001);
    }
}
