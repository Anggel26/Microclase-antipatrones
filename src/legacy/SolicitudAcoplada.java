package legacy;

public class SolicitudAcoplada {
    private String estado = "ACTIVA";
    private CorreoServicio correo = new CorreoServicio();
    private CitasServicio citas = new CitasServicio();
    private PagosServicio pagos = new PagosServicio();

    public void cancelar() {
        estado = "CANCELADA";
        System.out.println("Solicitud cambiada a: " + estado);
        correo.avisarCancelacion();
        citas.liberarCupo();
        // OJO: En el legado se les olvido llamar a pagos.procesarReembolso()!
        // Un viernes se olvidaron de la caja, y a un viajero se le perdio el reembolso.
    }

    public String getEstado() {
        return estado;
    }
}
