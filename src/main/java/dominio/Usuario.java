package dominio;

public class Usuario {
    private int idUsuario;
    private String NombreUsuario;
    private String Password;
    private TipoUsuario tipoUsuario;
    private boolean Estado; 

    public Usuario() {
        // Constructor vacío
    }

    public Usuario(int idUsuario, String nombreUsuario, String password, TipoUsuario tipoUsuario, boolean estado) {
        this.idUsuario = idUsuario;
        this.NombreUsuario = nombreUsuario;
        this.Password = password;
        this.tipoUsuario = tipoUsuario;
        this.Estado = estado;
    }

    // Getters
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public String getPassword() {
        return Password;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean isEstado() { // Método getter para 'estado'
        return Estado;
    }

    // Setters
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.NombreUsuario = nombreUsuario;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setEstado(boolean estado) { // Método setter para 'estado'
        this.Estado = estado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
               "idUsuario=" + idUsuario +
               ", nombreUsuario='" + NombreUsuario + '\'' +
               ", tipoUsuario=" + (tipoUsuario != null ? tipoUsuario.getDescripcion() : "N/A") +
               ", estado=" + Estado +
               '}';
    }
}