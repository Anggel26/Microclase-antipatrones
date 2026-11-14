package rescued.srp;

public class RepositorioExpedientes {
    public void archivarExpediente(String nombre, String pasaporte) {
        System.out.println("[RepositorioExpedientes] Expediente archivado digitalmente para " + nombre + " (Doc: " + pasaporte + ").");
    }
}
