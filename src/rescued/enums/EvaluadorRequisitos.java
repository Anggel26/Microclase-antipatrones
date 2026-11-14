package rescued.enums;

public class EvaluadorRequisitos {

    public String evaluar(TipoVisa tipo, int edad) {
        if (tipo == null) {
            return "RECHAZADA: Tipo de visa no reconocido o invalido.";
        }

        boolean edadValida = edad > tipo.getEdadMinimaExclusiva() && edad < tipo.getEdadMaximaExclusiva();

        if (edadValida) {
            return "APROBADA";
        } else {
            return "RECHAZADA: La edad (" + edad + " anos) no cumple los rangos permitidos para " 
                    + tipo.getDescripcion() + " (Mayor a " + tipo.getEdadMinimaExclusiva() 
                    + (tipo.getEdadMaximaExclusiva() < Integer.MAX_VALUE ? " y menor a " + tipo.getEdadMaximaExclusiva() : "") + ").";
        }
    }

    public String evaluarPorCodigo(int codigoTipo, int edad) {
        TipoVisa tipo = TipoVisa.porCodigo(codigoTipo);
        if (tipo == null) {
            return "RECHAZADA: Codigo de visa " + codigoTipo + " no existe en el sistema consular.";
        }
        return evaluar(tipo, edad);
    }
}
