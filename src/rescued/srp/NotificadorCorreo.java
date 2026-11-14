package rescued.srp;

public class NotificadorCorreo {
    public void notificarViajero(String nombre, String mensaje) {
        System.out.println("[NotificadorCorreo] Correo enviado a " + nombre + ": " + mensaje);
    }
}
