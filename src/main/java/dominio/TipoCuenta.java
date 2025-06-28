package dominio;

public class TipoCuenta {
    private int idTipoCuenta;
    private String Descripcion;

    public TipoCuenta() {
        // Constructor vacío
    }

    public TipoCuenta(int idTipoCuenta, String descripcion) {
        this.idTipoCuenta = idTipoCuenta;
        this.Descripcion = descripcion;
    }

    // Getters
    public int getIdTipoCuenta() {
        return idTipoCuenta;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    // Setters
    public void setIdTipoCuenta(int idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoCuenta{" +
               "idTipoCuenta=" + idTipoCuenta +
               ", descripcion='" + Descripcion + '\'' +
               '}';
    }
}