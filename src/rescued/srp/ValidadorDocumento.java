package rescued.srp;

public class ValidadorDocumento {
    public boolean validarPasaporte(String pasaporte) {
        if (pasaporte == null || pasaporte.trim().length() < 6) {
            System.out.println("[ValidadorDocumento] Error: Pasaporte invalido (nulo o menor a 6 caracteres).");
            return false;
        }
        System.out.println("[ValidadorDocumento] Pasaporte verificado con exito: " + pasaporte);
        return true;
    }
}
