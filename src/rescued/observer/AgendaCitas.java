package rescued.observer;

public class AgendaCitas implements ObservadorCancelacion {
    @Override
    public void alCancelarSolicitud(String idSolicitud, String nombreTitular) {
        System.out.println("[AgendaCitas] Cupo de cita consular presencial liberado para solicitud " + idSolicitud + ".");
    }
}
