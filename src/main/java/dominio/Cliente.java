package dominio;

import java.time.LocalDate; // Importar para FechaNacimiento

public class Cliente {
    private int idCliente;
    private String dni;
    private String cuil;
    private String nombre;
    private String apellido;
    private String sexo;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String correoElectronico; 
    private String telefono; 
    private Localidad localidad; 
    private Usuario usuario;
    private boolean estado; 

    public Cliente() {
        // Constructor vacío
    }

    public Cliente(int idCliente, String dni, String cuil, String nombre, String apellido, String sexo,
                   String nacionalidad, LocalDate fechaNacimiento, String direccion, Localidad localidad,
                   String correoElectronico, String telefono, Usuario usuario, boolean estado) {
        this.idCliente = idCliente;
        this.dni = dni;
        this.cuil = cuil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.sexo = sexo;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.localidad = localidad;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.usuario = usuario;
        this.estado = estado;
    }

    // Getters
    public int getIdCliente() {
        return idCliente;
    }

    public String getDni() {
        return dni;
    }

    public String getCuil() {
        return cuil;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getSexo() {
        return sexo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean isEstado() {
        return estado;
    }

    // Setters
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
               "idCliente=" + idCliente +
               ", dni='" + dni + '\'' +
               ", nombre='" + nombre + " " + apellido + '\'' +
               ", usuario=" + (usuario != null ? usuario.getNombreUsuario() : "N/A") +
               ", estado=" + estado +
               '}';
    }
}