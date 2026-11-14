package rescued.enums;

public enum TipoVisa {
    TURISTA(1, 18, Integer.MAX_VALUE, "Visa de Turismo"),
    ESTUDIANTE(2, 16, 30, "Visa de Estudiante"),
    TRABAJO(3, 18, 65, "Visa de Trabajo");

    private final int codigo;
    private final int edadMinimaExclusiva;
    private final int edadMaximaExclusiva;
    private final String descripcion;

    TipoVisa(int codigo, int edadMinimaExclusiva, int edadMaximaExclusiva, String descripcion) {
        this.codigo = codigo;
        this.edadMinimaExclusiva = edadMinimaExclusiva;
        this.edadMaximaExclusiva = edadMaximaExclusiva;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getEdadMinimaExclusiva() {
        return edadMinimaExclusiva;
    }

    public int getEdadMaximaExclusiva() {
        return edadMaximaExclusiva;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoVisa porCodigo(int codigo) {
        for (TipoVisa tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        return null;
    }
}
