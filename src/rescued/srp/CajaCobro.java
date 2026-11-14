package rescued.srp;

public class CajaCobro {
    public void cobrarTarifa(String nombre, double monto) {
        System.out.printf("[CajaCobro] Cobro registrado por $%.2f a nombre de %s.%n", monto, nombre);
    }
}
