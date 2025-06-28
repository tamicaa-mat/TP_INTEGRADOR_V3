package dominio;

public class Localidad {
    private int idLocalidad;
    private String descripcion;
    private Provincia provincia; 

    public Localidad() {
        // Constructor vacío
    }

    public Localidad(int idLocalidad, String descripcion, Provincia provincia) {
        this.idLocalidad = idLocalidad;
        this.descripcion = descripcion;
        this.provincia = provincia;
    }

    // Getters
    public int getIdLocalidad() {
        return idLocalidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    // Setters
    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @Override
    public String toString() {
        return "Localidad{" +
               "idLocalidad=" + idLocalidad +
               ", descripcion='" + descripcion + '\'' +
               ", provincia=" + (provincia != null ? provincia.getDescripcion() : "N/A") +
               '}';
    }
}