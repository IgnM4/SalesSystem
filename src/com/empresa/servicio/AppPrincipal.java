package com.empresa.servicio;

import com.empresa.modelo.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.Locale;
import java.util.InputMismatchException;

public class AppPrincipal {

    private static final Scanner sc = new Scanner(System.in);
    private static final Inventario inventario = Inventario.getInstancia();
    private static final List<OrdenCompra> listaOrdenes = new ArrayList<>();
    private static final List<FacturaCompra> listaFacturas = new ArrayList<>();
    private static final HistorialVentas historialVentas = HistorialVentas.getInstancia();
    private static final GestorClientes gestorClientes = GestorClientes.getInstancia();
    private static final NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "CL"));
    private static final int MAX_LONGITUD_FACTURA = 25;

    public static void main(String[] args) {
        mostrarPortada();
    }

    private static void mostrarPortada() {
        System.out.println("=========================================");
        System.out.println("   BIENVENIDO AL SISTEMA DE VENTAS       ");
        System.out.println("         EMPRESA DISTRIBUIDORA           ");
        System.out.println("=========================================");
        System.out.println("1. Ingresar al sistema");
        System.out.println("2. Salir");
        int opcion = leerEnteroSeguro("Seleccione una opción: ");

        if (opcion == 1) {
            mostrarMenuPrincipal();
        } else {
            System.out.println("¡Gracias por utilizar el sistema!");
            System.exit(0);
        }
    }

    private static void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n========= MENÚ PRINCIPAL =========");
            System.out.println("0. Probar clase Producto (test)");
            System.out.println("1. Mi empresa");
            System.out.println("2. Crear orden de compra y generar factura");
            System.out.println("3. Ingreso de facturas (manual)");
            System.out.println("4. Inventario");
            System.out.println("5. Cuentas / Saldos");
            System.out.println("6. Venta en Local");
            System.out.println("7. Venta en Domicilio");
            System.out.println("8. Registro de clientes y direcciones");
            System.out.println("9. Estadísticas y visualizaciones");
            System.out.println("10. Consultar historial de órdenes y facturas");
            System.out.println("11. Salir");
            System.out.println("12. Historial de Ventas");
            System.out.println("13. Exportar Ventas a CSV");
            System.out.println("14. Importar Clientes desde Excel");
            opcion = leerEnteroSeguro("Seleccione una opción: ");

            switch (opcion) {
                case 0 -> probarProductos();
                case 1 -> System.out.println("Mi Empresa (en desarrollo)");
                case 2 -> crearOrdenYGenerarFactura();
                case 3 -> ingresoManualFactura();
                case 4 -> gestionarInventario();
                case 5 -> System.out.println("Cuentas / Saldos (en desarrollo)");
                case 6 -> realizarVenta("local");
                case 7 -> realizarVenta("domicilio");
                case 8 -> gestionarClientes();
                case 9 -> System.out.println("Estadísticas y visualizaciones (en desarrollo)");
                case 10 -> consultarHistorial();
                case 11 -> System.out.println("Saliendo del sistema...");
                case 12 -> historialVentas.mostrarHistorial();
                case 13 -> {
                    System.out.print("Ruta del archivo CSV (ej: ventas.csv): ");
                    String ruta = sc.nextLine().trim();
                    historialVentas.exportarCSV(ruta);
                }
                case 14 -> {
                    System.out.print("Ruta del archivo Excel (.xlsx): ");
                    String ruta = sc.nextLine().trim();
                    gestorClientes.importarClientesDesdeExcel(ruta);
                }
                default -> System.out.println("Opción no válida, intente nuevamente.");
            }
        } while (opcion != 11);
    }

    private static void gestionarClientes() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE CLIENTES Y DIRECCIONES ---");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Ver todos los clientes");
            System.out.println("3. Buscar cliente por RUT");
            System.out.println("4. Eliminar cliente");
            System.out.println("5. Volver al menú principal");
            opcion = leerEnteroSeguro("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("RUT: ");
                    String rut = sc.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = sc.nextLine();
                    System.out.print("Dirección: ");
                    String direccion = sc.nextLine();
                    gestorClientes.registrarCliente(new Cliente(nombre, rut, telefono, direccion));
                }
                case 2 -> gestorClientes.listarClientes();
                case 3 -> {
                    System.out.print("Ingrese RUT a buscar: ");
                    String rut = sc.nextLine();
                    Cliente encontrado = gestorClientes.buscarClientePorRUT(rut);
                    System.out.println(encontrado != null ? encontrado : "Cliente no encontrado.");
                }
                case 4 -> {
                    System.out.print("Ingrese RUT del cliente a eliminar: ");
                    String rut = sc.nextLine();
                    gestorClientes.eliminarCliente(rut);
                }
                case 5 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    private static void consultarHistorial() {
        int opcion;
        do {
            System.out.println("\n--- CONSULTA DE HISTORIALES ---");
            System.out.println("1. Ver órdenes de compra");
            System.out.println("2. Ver facturas de compra");
            System.out.println("3. Buscar orden por ID");
            System.out.println("4. Buscar factura por número");
            System.out.println("5. Volver al menú principal");
            opcion = leerEnteroSeguro("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> listaOrdenes.forEach(oc ->
                    System.out.println("Orden ID: " + oc.getIdOrden() + " | Proveedor: " + oc.getProveedor() + " | Total estimado: " + formatoMoneda.format(oc.calcularTotalEstimado())));
                case 2 -> listaFacturas.forEach(FacturaCompra::mostrarDetalle);
                case 3 -> {
                    System.out.print("Ingrese ID de orden: ");
                    String id = sc.nextLine();
                    listaOrdenes.stream()
                            .filter(oc -> oc.getIdOrden().equalsIgnoreCase(id))
                            .findFirst()
                            .ifPresentOrElse(
                                    oc -> oc.getItems().forEach(item -> System.out.printf("Producto: %s | Cantidad: %d\n", item.getProducto().getNombre(), item.getCantidad())),
                                    () -> System.out.println("Orden no encontrada."));
                }
                case 4 -> {
                    System.out.print("Ingrese número de factura: ");
                    String num = sc.nextLine();
                    listaFacturas.stream()
                            .filter(f -> f.getNumeroFactura().equalsIgnoreCase(num))
                            .findFirst()
                            .ifPresentOrElse(FacturaCompra::mostrarDetalle, () -> System.out.println("Factura no encontrada."));
                }
                case 5 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    private static void crearOrdenYGenerarFactura() {
        System.out.print("ID de orden de compra: ");
        String idOrden = sc.nextLine();
        System.out.print("Nombre del proveedor: ");
        String proveedor = sc.nextLine();
        OrdenCompra orden = new OrdenCompra(idOrden, proveedor);

        boolean agregar = true;
        while (agregar) {
            Producto producto = crearProductoDesdeInput();
            int cantidad = leerEnteroSeguro("Cantidad: ");
            orden.agregarProducto(producto, cantidad);
            System.out.print("¿Agregar otro producto? (s/n): ");
            agregar = sc.nextLine().equalsIgnoreCase("s");
        }

        listaOrdenes.add(orden);

        System.out.print("Ingrese número de factura: ");
        String numeroFactura = sc.nextLine().replaceAll("[^a-zA-Z0-9\\-]", "").trim();
        if (numeroFactura.length() > MAX_LONGITUD_FACTURA) {
            numeroFactura = numeroFactura.substring(0, MAX_LONGITUD_FACTURA);
        }

        FacturaCompra factura = orden.generarFacturaDesdeOrden(numeroFactura);
        inventario.registrarEntradaConFactura(factura);
        listaFacturas.add(factura);
        System.out.println("\nFactura generada e inventario actualizado correctamente.");
        factura.mostrarDetalle();
    }

    private static void ingresoManualFactura() {
        System.out.print("Número de factura: ");
        String numero = sc.nextLine().replaceAll("[^a-zA-Z0-9\\-]", "").trim();
        if (numero.length() > MAX_LONGITUD_FACTURA) {
            numero = numero.substring(0, MAX_LONGITUD_FACTURA);
        }
        System.out.print("Proveedor: ");
        String proveedor = sc.nextLine();

        FacturaCompra factura = new FacturaCompra(numero, proveedor);

        boolean agregar = true;
        while (agregar) {
            Producto producto = crearProductoDesdeInput();
            int cantidad = leerEnteroSeguro("Cantidad: ");
            factura.agregarProducto(producto, cantidad);
            System.out.print("¿Agregar otro producto? (s/n): ");
            agregar = sc.nextLine().equalsIgnoreCase("s");
        }

        inventario.registrarEntradaConFactura(factura);
        listaFacturas.add(factura);
        System.out.println("Factura ingresada correctamente.");
        factura.mostrarDetalle();
    }

    private static void gestionarInventario() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE INVENTARIO ---");
            System.out.println("1. Ver inventario completo");
            System.out.println("2. Agregar producto o aumentar stock");
            System.out.println("3. Buscar producto");
            System.out.println("4. Exportar a CSV");
            System.out.println("5. Volver al menú principal");
            opcion = leerEnteroSeguro("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> inventario.mostrarInventarioCompleto();
                case 2 -> {
                    Producto producto = crearProductoDesdeInput();
                    int cantidad = leerEnteroSeguro("Cantidad a agregar: ");
                    inventario.agregarProducto(producto.getNombre(), producto.getTipo(), producto.getPrecioCompra(), producto.getComision(), cantidad);
                    System.out.println("Producto agregado o actualizado correctamente.");
                }
                case 3 -> {
                    System.out.print("Ingrese nombre o tipo para buscar: ");
                    String criterio = sc.nextLine();
                    inventario.buscarProducto(criterio);
                }
                case 4 -> {
                    System.out.print("Nombre del archivo CSV (ej: inventario.csv): ");
                    String archivo = sc.nextLine();
                    inventario.exportarCSV(archivo);
                }
                case 5 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    private static void realizarVenta(String formato) {
        System.out.println("\n--- INICIO DE VENTA EN " + formato.toUpperCase() + " ---");
        System.out.print("Ingrese RUT del cliente: ");
        String rutCliente = sc.nextLine();
        Cliente cliente = gestorClientes.buscarClientePorRUT(rutCliente);

        if (cliente == null) {
            System.out.println("Cliente no encontrado. Por favor, registre al cliente primero.");
            return;
        }

        Venta venta = new Venta(formato, cliente);

        boolean agregarOtro = true;
        while (agregarOtro) {
            System.out.print("Nombre del producto (exacto): ");
            String nombre = sc.nextLine();

            if (!inventario.tieneStockDisponible(nombre, 1)) {
                System.out.println("Producto no existe o sin stock.");
                continue;
            }

            Producto producto = inventario.getProducto(nombre);
            int cantidad = leerEnteroSeguro("¿Cantidad a vender?: ");

            if (!inventario.tieneStockDisponible(nombre, cantidad)) {
                System.out.println("Stock insuficiente.");
                continue;
            }

            for (int i = 0; i < cantidad; i++) {
                venta.agregarProducto(producto, sc);
            }

            inventario.reducirStock(nombre, cantidad);
            System.out.print("¿Desea agregar otro producto? (s/n): ");
            agregarOtro = sc.nextLine().equalsIgnoreCase("s");
        }

        historialVentas.agregarVenta(venta);
        venta.mostrarResumenVenta();
        System.out.println("TOTAL A PAGAR: " + formatoMoneda.format(venta.calcularTotalNeto()));
    }

    private static void probarProductos() {
        System.out.println("\n--- PRUEBA DE PRODUCTOS Y DESCUENTO ---");
        Producto p = new Producto("Gas 15kg", "gas", 25000, 3000);
        p.mostrarDetalleProducto();

        System.out.print("\n¿Formato de venta? (local/domicilio): ");
        String formato = sc.nextLine().toLowerCase();

        int[] descuentos = Descuento.obtenerDescuentosDisponibles(p, formato);
        if (descuentos.length == 0) {
            System.out.println("No hay descuentos disponibles.");
        } else if (descuentos.length == 1) {
            double precioFinal = Descuento.aplicarDescuentoSeleccionado(p, formato, 0);
            System.out.printf("Descuento aplicado: $%d | Precio final: $%.0f\n", descuentos[0], precioFinal);
        } else {
            System.out.println("Seleccione descuento:");
            for (int i = 0; i < descuentos.length; i++) {
                System.out.printf("[%d] $%d\n", i, descuentos[i]);
            }
            int opcion = leerEnteroSeguro("Opción: ");
            if (opcion >= 0 && opcion < descuentos.length) {
                double precioFinal = Descuento.aplicarDescuentoSeleccionado(p, formato, opcion);
                System.out.printf("Precio final: $%.0f\n", precioFinal);
            } else {
                System.out.println("Opción inválida.");
            }
        }
    }

    private static Producto crearProductoDesdeInput() {
        System.out.print("Nombre del producto: ");
        String nombre = sc.nextLine();
        System.out.print("Tipo: ");
        String tipo = sc.nextLine();
        double precioCompra = leerDoubleSeguro("Precio compra con IVA: ");
        double comision = leerDoubleSeguro("Comisión (en pesos): ");
        return new Producto(nombre, tipo, precioCompra, comision);
    }

    private static int leerEnteroSeguro(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = sc.nextInt();
                sc.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Intente nuevamente.");
                sc.nextLine();
            }
        }
    }

    private static double leerDoubleSeguro(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = sc.nextDouble();
                sc.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Intente nuevamente.");
                sc.nextLine();
            }
        }
    }
}
