package dominio;

public class TipoMovimiento {
    private int idTipoMovimiento;
    private String Descripcion;

    public TipoMovimiento() {
        // Constructor vacío
    }

    public TipoMovimiento(int idTipoMovimiento, String descripcion) {
        this.idTipoMovimiento = idTipoMovimiento;
        this.Descripcion = descripcion;
    }

    // Getters
    public int getIdTipoMovimiento() {
        return idTipoMovimiento;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    // Setters
    public void setIdTipoMovimiento(int idTipoMovimiento) {
        this.idTipoMovimiento = idTipoMovimiento;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoMovimiento{" +
               "idTipoMovimiento=" + idTipoMovimiento +
               ", descripcion='" + Descripcion + '\'' +
               '}';
    }
}