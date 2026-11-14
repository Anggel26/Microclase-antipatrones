package rescued.strategy;

public class TarifaEstudiante implements EstrategiaTarifa {
    private static final double GASTO_ADMINISTRATIVO = 10.0;
    private static final double FACTOR_DESCUENTO = 0.5; // 50% de descuento para estudiantes

    @Override
    public double calcular(double tarifaBase, int meses) {
        return ((tarifaBase * meses) + GASTO_ADMINISTRATIVO) * FACTOR_DESCUENTO;
    }

    @Override
    public String getNombre() {
        return "Estudiante";
    }
}
