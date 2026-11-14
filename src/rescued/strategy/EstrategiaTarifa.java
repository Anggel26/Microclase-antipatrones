package rescued.strategy;

public interface EstrategiaTarifa {
    double calcular(double tarifaBase, int meses);
    String getNombre();
}
