package rescued.srp;

public class TramiteVisaServicio {
    private final ValidadorDocumento validador;
    private final CajaCobro caja;
    private final RepositorioExpedientes repositorio;
    private final NotificadorCorreo notificador;

    public TramiteVisaServicio(ValidadorDocumento validador,
                              CajaCobro caja,
                              RepositorioExpedientes repositorio,
                              NotificadorCorreo notificador) {
        this.validador = validador;
        this.caja = caja;
        this.repositorio = repositorio;
        this.notificador = notificador;
    }

    public boolean procesarSolicitud(String nombre, String pasaporte, double tarifa) {
        if (!validador.validarPasaporte(pasaporte)) {
            return false;
        }
        caja.cobrarTarifa(nombre, tarifa);
        repositorio.archivarExpediente(nombre, pasaporte);
        notificador.notificarViajero(nombre, "su visa va en camino");
        return true;
    }
}
