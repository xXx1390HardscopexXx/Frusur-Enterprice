package Modelo;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   INICIO DE PRUEBAS - SISTEMA FRUSUR");
        System.out.println("==========================================\n");

        // ------------------------------------------------------------
        // 1. CREACIÓN DE RUTS (Usando tu método estático 'of')
        // ------------------------------------------------------------
        System.out.println("--> [1] Creando Identificadores (RUTs)...");
        Rut rutAgro = Rut.of("15.111.111-1");
        Rut rutProd = Rut.of("12.222.222-2");
        Rut rutEst  = Rut.of("18.333.333-K");
        Rut rutSup  = Rut.of("9.444.444-5");

        System.out.println("    Ruts generados correctamente.");

        // ------------------------------------------------------------
        // 2. INSTANCIANDO ACTORES (HERENCIA DE PERSONA)
        // ------------------------------------------------------------
        System.out.println("\n--> [2] Contratando Personal y Registrando Productores...");

        // Crear Agrónomo
        Agronomo agronomo = new Agronomo(rutAgro, "Ignacio Fuentes", "ignacio@frusur.cl");

        // Crear Productor
        Productor productor = new Productor(rutProd, "Juan Pérez (Agricola Sur)", "+569999999");

        // Crear Supervisor
        Supervisor supervisor = new Supervisor(rutSup, "Claudio Muñoz", "claudio@frusur.cl");

        // Crear Estadístico
        Estadistico estadistico = new Estadistico(rutEst, "Javiera Duarte", "javiera@frusur.cl");

        System.out.println("    Actores creados: " + agronomo.getNombre() + ", " + productor.getNombre());

        // ------------------------------------------------------------
        // 3. FLUJO DE COMPRAS (Agrónomo <-> Productor)
        // ------------------------------------------------------------
        System.out.println("\n--> [3] Simulando Proceso de Compra...");

        // El agrónomo contacta al productor
        agronomo.contactarProductor(productor);

        // El productor trae Materia Prima
        MateriaPrima loteFruta = productor.llevarMateriaPrima("Frambuesa Heritage", 5000.0);
        System.out.println("    RECEPCIÓN: " + loteFruta.toString());

        // ------------------------------------------------------------
        // 4. FLUJO DE INVENTARIO (Estadístico <-> Planilla)
        // ------------------------------------------------------------
        System.out.println("\n--> [4] Simulando Proceso de Estadístico...");

        // 4.1 Configurar al estadístico (Asignarle su supervisor)
        estadistico.setSupervisorTurno(supervisor);

        // 4.2 El estadístico crea una nueva planilla
        estadistico.desarrollarPlanilla();
        estadistico.agregarInfoLinea("Línea 1 - Congelado");

        // 4.3 Crear Tarjas (simulando etiquetas de cajas)
        Tarja t1 = new Tarja("Lote-A1 (100kg)");
        Tarja t2 = new Tarja("Lote-A2 (150kg)");

        // 4.4 Agregar las tarjas a la planilla
        estadistico.agregarTarjas(t1);
        estadistico.agregarTarjas(t2);
        System.out.println("    Tarjas agregadas a la planilla del turno.");

        // 4.5 Calcular totales
        estadistico.calcularTotalKilos();

        // ------------------------------------------------------------
        // 5. CIERRE DE TURNO
        // ------------------------------------------------------------
        System.out.println("\n--> [5] Cierre de Turno...");

        // El estadístico entrega la planilla al supervisor
        estadistico.entregarPlanillaASupervisor();

        System.out.println("\n==========================================");
        System.out.println("   PRUEBA FINALIZADA CON ÉXITO");
        System.out.println("==========================================");
    }
}