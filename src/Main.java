import legacy.*;
import rescued.enums.*;
import rescued.observer.*;
import rescued.srp.*;
import rescued.strategy.*;

public class Main {

    public static void main(String[] args) {
        imprimirEncabezado();

        probarCaso1GodObject();
        probarCaso2CopyPaste();
        probarCaso3MagicNumbers();
        probarCaso4Acoplamiento();
        probarCaso5Integracion();

        imprimirTablaCaceriaFinal();
    }

    private static void imprimirEncabezado() {
        System.out.println("===============================================================================");
        System.out.println("            Taller: Antipatrones de Diseno de Software");
        System.out.println("                 La Oficina de Visas del Caos (Java)");
        System.out.println("===============================================================================\n");
    }

    private static void probarCaso1GodObject() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("CASO 1: God Object (OficinaDeTodo vs Clases con Responsabilidad Unica)");
        System.out.println("-------------------------------------------------------------------------------");
        String viajero = "Carlos Mendoza";
        String pasaporte = "AB123456";
        double precio = 150.0;

        System.out.println("[VERSION HEREDADA - Ramiro lo hace todo]:");
        OficinaDeTodo ramiro = new OficinaDeTodo();
        ramiro.procesar(viajero, pasaporte, precio);

        System.out.println();
        System.out.println("[VERSION RESCATADA - Responsabilidad Unica por Componentes]:");
        ValidadorDocumento validador = new ValidadorDocumento();
        CajaCobro caja = new CajaCobro();
        RepositorioExpedientes repositorio = new RepositorioExpedientes();
        NotificadorCorreo notificador = new NotificadorCorreo();

        TramiteVisaServicio tramite = new TramiteVisaServicio(validador, caja, repositorio, notificador);
        tramite.procesarSolicitud(viajero, pasaporte, precio);

        System.out.println("\n>> CONCLUSION CASO 1: En la heredada Ramiro asume validacion, cobro, archivo");
        System.out.println("   y notificaciones. En la rescatada cada clase tiene una unica razon para cambiar.\n");
    }

    private static void probarCaso2CopyPaste() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("CASO 2: Copy-Paste Programming (CalculadoraDeTarifas vs Patron Strategy)");
        System.out.println("-------------------------------------------------------------------------------");
        int meses = 6;
        int[] contadorEdicionesHeredado = new int[]{0};

        System.out.println("[VERSION HEREDADA - Tarifas clonadas]:");
        CalculadoraDeTarifas calcHeredada = new CalculadoraDeTarifas();
        System.out.println("Calculando tarifas originales (Base 50):");
        System.out.println(" - Turista:   $" + calcHeredada.visaTurista(meses));
        System.out.println(" - Estudiante:$" + calcHeredada.visaEstudiante(meses));
        System.out.println(" - Trabajo:   $" + calcHeredada.visaTrabajo(meses));

        System.out.println("Subiendo tarifa base de 50 a 60 en la version clonada...");
        double costoTuristaHeredado = calcHeredada.visaTuristaTarifaActualizada(meses, contadorEdicionesHeredado);
        double costoEstudianteHeredado = calcHeredada.visaEstudianteTarifaActualizada(meses, contadorEdicionesHeredado);
        double costoTrabajoHeredado = calcHeredada.visaTrabajoTarifaActualizada(meses, contadorEdicionesHeredado);

        System.out.println("Lugares que tuvieron que editarse en el codigo heredado: " + contadorEdicionesHeredado[0]);
        System.out.println("Resultados actualizados: Turista=$" + costoTuristaHeredado 
                + ", Estudiante=$" + costoEstudianteHeredado + ", Trabajo=$" + costoTrabajoHeredado);

        System.out.println();
        System.out.println("[VERSION RESCATADA - Patron Strategy con Tarifa Base Centralizada]:");
        CalculadoraTarifasConsulado consulado = new CalculadoraTarifasConsulado(50.0);
        EstrategiaTarifa turista = new TarifaTurista();
        EstrategiaTarifa estudiante = new TarifaEstudiante();
        EstrategiaTarifa trabajo = new TarifaTrabajo();

        System.out.println("Calculando tarifas originales (Base 50):");
        System.out.println(" - Turista:   $" + consulado.calcularPrecio(turista, meses));
        System.out.println(" - Estudiante:$" + consulado.calcularPrecio(estudiante, meses));
        System.out.println(" - Trabajo:   $" + consulado.calcularPrecio(trabajo, meses));

        System.out.println("Subiendo tarifa base de 50 a 60 en la version Strategy...");
        consulado.setTarifaBase(60.0);

        double costoTuristaStrategy = consulado.calcularPrecio(turista, meses);
        double costoEstudianteStrategy = consulado.calcularPrecio(estudiante, meses);
        double costoTrabajoStrategy = consulado.calcularPrecio(trabajo, meses);

        System.out.println("Lugares editados en la version Strategy: " + consulado.getContadorEdiciones());
        System.out.println("Resultados actualizados: Turista=$" + costoTuristaStrategy 
                + ", Estudiante=$" + costoEstudianteStrategy + ", Trabajo=$" + costoTrabajoStrategy);

        System.out.println("\nComparacion de precios finales:");
        System.out.println(" - Turista:    Heredada=" + costoTuristaHeredado + " | Rescatada=" + costoTuristaStrategy + " (Coinciden: " + (costoTuristaHeredado == costoTuristaStrategy) + ")");
        System.out.println(" - Estudiante: Heredada=" + costoEstudianteHeredado + " | Rescatada=" + costoEstudianteStrategy + " (Coinciden: " + (costoEstudianteHeredado == costoEstudianteStrategy) + ")");
        System.out.println(" - Trabajo:    Heredada=" + costoTrabajoHeredado + " | Rescatada=" + costoTrabajoStrategy + " (Coinciden: " + (costoTrabajoHeredado == costoTrabajoStrategy) + ")");

        System.out.println("\n>> CONCLUSION CASO 2: En la version clonada se requirieron 3 ediciones propensas");
        System.out.println("   a error. En la version Strategy solo se modifico 1 unico punto y los precios coinciden.\n");
    }

    private static void probarCaso3MagicNumbers() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("CASO 3: Magic Numbers (EvaluadorMagico vs Enums con Constantes)");
        System.out.println("-------------------------------------------------------------------------------");
        EvaluadorMagico magico = new EvaluadorMagico();
        EvaluadorRequisitos evaluadorRescatado = new EvaluadorRequisitos();

        System.out.println("[PRUEBA A: Tipo 2 y Edad 20]");
        String resultadoHeredadoA = magico.evaluar(2, 20);
        String resultadoRescatadoA = evaluadorRescatado.evaluar(TipoVisa.ESTUDIANTE, 20);
        System.out.println(" - Version Heredada (tipo == 2, edad 20):        " + resultadoHeredadoA);
        System.out.println(" - Version Rescatada (TipoVisa.ESTUDIANTE, 20): " + resultadoRescatadoA);
        System.out.println(" -> Ambos resultados coinciden: " + resultadoHeredadoA.equals(resultadoRescatadoA));

        System.out.println("\n[PRUEBA B: Tipo 9 Invalido / Inexistente]");
        String resultadoHeredadoB = magico.evaluar(9, 20);
        String resultadoRescatadoB = evaluadorRescatado.evaluarPorCodigo(9, 20);
        System.out.println(" - Version Heredada (tipo == 9): " + resultadoHeredadoB + " (Rechazo silencioso, sin explicacion)");
        System.out.println(" - Version Rescatada (tipo == 9): " + resultadoRescatadoB);

        System.out.println("\n>> CONCLUSION CASO 3: Se reemplazaron numeros magicos (1, 2, 3, 17, 31, 66) por");
        System.out.println("   tipos expresivos y claros con retroalimentacion detallada ante valores invalidos.\n");
    }

    private static void probarCaso4Acoplamiento() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("CASO 4: Acoplamiento Fuerte (SolicitudAcoplada vs Patron Observer)");
        System.out.println("-------------------------------------------------------------------------------");

        System.out.println("[VERSION HEREDADA - Cancelacion con dependencias fijas y olvido de caja]:");
        SolicitudAcoplada acoplada = new SolicitudAcoplada();
        acoplada.cancelar();
        System.out.println("--> EVIDENCIA DE FALLA: No se invoco PagosServicio. El reembolso quedo en el limbo!");

        System.out.println();
        System.out.println("[VERSION RESCATADA - Solicitud Desacoplada con Patron Observer]:");
        SolicitudDesacoplada solicitud = new SolicitudDesacoplada("SOL-2026-981", "Elena Rios");

        // Suscripcion de los 3 servicios interesados sin que la clase Solicitud los acople
        solicitud.agregarObservador(new CorreoNotificador());
        solicitud.agregarObservador(new AgendaCitas());
        solicitud.agregarObservador(new CajaReembolsos());

        solicitud.cancelar();

        System.out.println("\n>> CONCLUSION CASO 4: La version con Observer notifica a correo, citas y caja");
        System.out.println("   de pagos de manera desacoplada y automatica, garantizando que nadie pierda su reembolso.\n");
    }

    private static void probarCaso5Integracion() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("CASO 5: Integracion de Punta a Punta con la Version Rescatada");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Simulando viaje completo de solicitud, evaluacion, cobro y eventual cancelacion...\n");

        String nombre = "Mateo Salazar";
        String pasaporte = "CO789654";
        int edad = 24;
        TipoVisa tipo = TipoVisa.ESTUDIANTE;
        int mesesEstudio = 12;

        System.out.println("Paso 1: Evaluacion de requisitos para " + nombre + " (" + tipo.getDescripcion() + ", " + edad + " anos):");
        EvaluadorRequisitos evaluador = new EvaluadorRequisitos();
        String evaluacion = evaluador.evaluar(tipo, edad);
        System.out.println("        Resultado: " + evaluacion);

        if (!"APROBADA".equals(evaluacion)) {
            System.out.println("Tramite no puede continuar.");
            return;
        }

        System.out.println("\nPaso 2: Calculo de la tarifa oficial usando Strategy:");
        CalculadoraTarifasConsulado consulado = new CalculadoraTarifasConsulado(60.0);
        EstrategiaTarifa estrategia = new TarifaEstudiante();
        double costoTotal = consulado.calcularPrecio(estrategia, mesesEstudio);
        System.out.printf("        Tarifa calculada: $%.2f (Base $%.2f, %d meses, descuento estudiante)%n", 
                costoTotal, consulado.getTarifaBase(), mesesEstudio);

        System.out.println("\nPaso 3: Tramite de ventanilla aplicando SRP:");
        TramiteVisaServicio tramite = new TramiteVisaServicio(
                new ValidadorDocumento(),
                new CajaCobro(),
                new RepositorioExpedientes(),
                new NotificadorCorreo()
        );
        tramite.procesarSolicitud(nombre, pasaporte, costoTotal);

        System.out.println("\nPaso 4: El viajero cancela su solicitud; Observer orquesta los servicios:");
        SolicitudDesacoplada solicitud = new SolicitudDesacoplada("SOL-2026-999", nombre);
        solicitud.agregarObservador(new CorreoNotificador());
        solicitud.agregarObservador(new AgendaCitas());
        solicitud.agregarObservador(new CajaReembolsos());
        solicitud.cancelar();

        System.out.println("\n>> CONCLUSION CASO 5: Todo el flujo consular funciono de forma limpia,");
        System.out.println("   modular, mantenible y sin antipatrones.\n");
    }

    private static void imprimirTablaCaceriaFinal() {
        System.out.println("=========================================================================================================");
        System.out.println("                                RESUMEN FINAL: CACERIA DE ANTIPATRONES");
        System.out.println("=========================================================================================================");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", "FRAGMENTO", "ANTIPATRON", "SINTOMA DELATOR", "CONSECUENCIA EN EL CONSULADO");
        System.out.println("-------------------+------------------+-------------------------------------+-----------------------------");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", 
                "Frag 1: Ventanilla", "God Object", "OficinaDeTodo valida, cobra, archiva", "Si Ramiro se enferma, todo el");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", 
                "de Ramiro", "", "y notifica en un solo metodo", "consulado se paraliza");
        System.out.println("-------------------+------------------+-------------------------------------+-----------------------------");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", 
                "Frag 2: Tarifa", "Copy-Paste", "Formula duplicada en 3 metodos con", "Tarifas inconsistentes al");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", 
                "clonada", "Programming", "valores fijos (50 * meses + 10)", "actualizar una y olvidar otra");
        System.out.println("-------------------+------------------+-------------------------------------+-----------------------------");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", 
                "Frag 3: Numeros", "Magic Numbers", "Literales 1, 2, 3, 17, 31, 66 y", "Imposible de mantener; codigos");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", 
                "misteriosos", "", "multiplicadores sin nombre semantico", "desconocidos fallan en silencio");
        System.out.println("-------------------+------------------+-------------------------------------+-----------------------------");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", 
                "Frag 4: Cancelacion", "Acoplamiento", "Solicitud crea y llama manualmente", "Si olvidan llamar a un servicio");
        System.out.printf("%-18s | %-16s | %-35s | %-28s%n", 
                "en cadena", "Fuerte", "servicios; olvido de caja de pagos", "(como pagos), se pierde plata");
        System.out.println("=========================================================================================================\n");
    }
}
