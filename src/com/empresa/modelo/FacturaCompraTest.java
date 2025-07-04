package com.empresa.modelo;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link FacturaCompra}.
 */
public class FacturaCompraTest {

    @Test
    public void calcularTotalFactura_sumaPreciosCompra() {
        Producto prod1 = new Producto("Gas 5kg", "gas", 10000, 1000);
        Producto prod2 = new Producto("Regulador", "accesorio", 5000, 500);

        FacturaCompra factura = new FacturaCompra("F001", "Proveedor");
        factura.agregarProducto(prod1, 2); // 2 * 10000
        factura.agregarProducto(prod2, 3); // 3 * 5000

        double esperado = 2 * prod1.getPrecioCompraConIVA() + 3 * prod2.getPrecioCompraConIVA();
        assertEquals(esperado, factura.calcularTotalFactura(), 0.0001);
    }
}
