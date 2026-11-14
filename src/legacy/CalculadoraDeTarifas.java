package legacy;

public class CalculadoraDeTarifas {

    // Simulación del código clonado original
    public double visaTurista(int meses) {
        double total = 50 * meses;
        total = total + 10;
        return total;
    }

    public double visaEstudiante(int meses) {
        double total = 50 * meses;
        total = total + 10;
        return total * 0.5;
    }

    public double visaTrabajo(int meses) {
        double total = 50 * meses;
        total = total + 10;
        return total * 1.5;
    }

    // Método que demuestra el problema cuando se actualiza la tarifa base a 60
    // En el sistema heredado, para actualizar a 60 hubo que cambiar manualmente los 3 métodos
    public double visaTuristaTarifaActualizada(int meses, int[] contadorEdiciones) {
        contadorEdiciones[0]++; // Edición 1
        double total = 60 * meses;
        total = total + 10;
        return total;
    }

    public double visaEstudianteTarifaActualizada(int meses, int[] contadorEdiciones) {
        contadorEdiciones[0]++; // Edición 2
        double total = 60 * meses;
        total = total + 10;
        return total * 0.5;
    }

    public double visaTrabajoTarifaActualizada(int meses, int[] contadorEdiciones) {
        contadorEdiciones[0]++; // Edición 3
        double total = 60 * meses;
        total = total + 10;
        return total * 1.5;
    }
}
