package rescued.strategy;

public class TarifaTurista implements EstrategiaTarifa {
    private static final double GASTO_ADMINISTRATIVO = 10.0;

    @Override
    public double calcular(double tarifaBase, int meses) {
        return (tarifaBase * meses) + GASTO_ADMINISTRATIVO;
    }

    @Override
    public String getNombre() {
        return "Turista";
    }
}
