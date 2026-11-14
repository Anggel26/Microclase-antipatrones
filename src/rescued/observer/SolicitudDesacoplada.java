package rescued.observer;

import java.util.ArrayList;
import java.util.List;

public class SolicitudDesacoplada {
    private final String idSolicitud;
    private final String nombreTitular;
    private String estado;
    private final List<ObservadorCancelacion> observadores;

    public SolicitudDesacoplada(String idSolicitud, String nombreTitular) {
        this.idSolicitud = idSolicitud;
        this.nombreTitular = nombreTitular;
        this.estado = "ACTIVA";
        this.observadores = new ArrayList<>();
    }

    public void agregarObservador(ObservadorCancelacion observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void removerObservador(ObservadorCancelacion observador) {
        observadores.remove(observador);
    }

    public void cancelar() {
        this.estado = "CANCELADA";
        System.out.println("[Solicitud #" + idSolicitud + "] Estado actualizado a: " + estado);
        notificarCancelacion();
    }

    private void notificarCancelacion() {
        for (ObservadorCancelacion obs : observadores) {
            obs.alCancelarSolicitud(this.idSolicitud, this.nombreTitular);
        }
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public String getEstado() {
        return estado;
    }
}
