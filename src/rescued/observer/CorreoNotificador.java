package rescued.observer;

public class CorreoNotificador implements ObservadorCancelacion {
    @Override
    public void alCancelarSolicitud(String idSolicitud, String nombreTitular) {
        System.out.println("[CorreoNotificador] Notificacion remitida a " + nombreTitular 
                + " confirmando la cancelacion del tramite " + idSolicitud + ".");
    }
}
