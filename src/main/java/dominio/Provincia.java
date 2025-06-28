package dominio;

public class Provincia {
    private int idProvincia;
    private String Descripcion;

    public Provincia() {
        // Constructor vacío
    }

    public Provincia(int idProvincia, String descripcion) {
        this.idProvincia = idProvincia;
        this.Descripcion = descripcion;
    }

    // Getters
    public int getIdProvincia() {
        return idProvincia;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    // Setters
    public void setIdProvincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }

    public void setDescripcion(String descripcion) {
        this.Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Provincia{" +
               "idProvincia=" + idProvincia +
               ", descripcion='" + Descripcion + '\'' +
               '}';
    }
}