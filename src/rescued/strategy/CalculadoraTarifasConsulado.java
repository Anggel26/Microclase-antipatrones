package rescued.strategy;

public class CalculadoraTarifasConsulado {
    // Unico punto de cambio de la tarifa base
    private double tarifaBase;
    private int contadorEdiciones;

    public CalculadoraTarifasConsulado(double tarifaBase) {
        this.tarifaBase = tarifaBase;
        this.contadorEdiciones = 0;
    }

    public void setTarifaBase(double nuevaTarifa) {
        this.tarifaBase = nuevaTarifa;
        this.contadorEdiciones++; // Se modifica exactamente en 1 solo lugar
    }

    public double getTarifaBase() {
        return tarifaBase;
    }

    public int getContadorEdiciones() {
        return contadorEdiciones;
    }

    public double calcularPrecio(EstrategiaTarifa estrategia, int meses) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de tarifa no puede ser nula.");
        }
        return estrategia.calcular(tarifaBase, meses);
    }
}
