package rescued.strategy;

public class TarifaTrabajo implements EstrategiaTarifa {
    private static final double GASTO_ADMINISTRATIVO = 10.0;
    private static final double FACTOR_RECARGO = 1.5; // 50% de recargo para permisos laborales

    @Override
    public double calcular(double tarifaBase, int meses) {
        return ((tarifaBase * meses) + GASTO_ADMINISTRATIVO) * FACTOR_RECARGO;
    }

    @Override
    public String getNombre() {
        return "Trabajo";
    }
}
