package rescued.observer;

public class CajaReembolsos implements ObservadorCancelacion {
    @Override
    public void alCancelarSolicitud(String idSolicitud, String nombreTitular) {
        System.out.println("[CajaReembolsos] Orden de devolucion y reembolso generada para el titular " 
                + nombreTitular + " (Solicitud: " + idSolicitud + ").");
    }
}
