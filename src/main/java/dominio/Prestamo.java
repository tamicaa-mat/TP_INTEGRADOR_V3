package dominio;

import java.time.LocalDate; // Importar para FechaAlta


public class Prestamo {
    private int idPrestamo;
    private Cliente cliente; 
    private Cuenta cuentaAsociada; 
    private LocalDate fechaAlta;
    private double importePedido;
    private int plazoMeses;
    private double importePorMes;
    private double interes; 
    private int CantidadCuotas;
    private String Estado; 

    public Prestamo() {
        // Constructor vacío
    }

    public Prestamo(int idPrestamo, Cliente cliente, Cuenta cuentaAsociada, LocalDate fechaAlta,
                     double importePedido, int plazoMeses, double importePorMes,
                     double interes, int cantidadCuotas, String estado) {
        this.idPrestamo = idPrestamo;
        this.cliente = cliente;
        this.cuentaAsociada = cuentaAsociada;
        this.fechaAlta = fechaAlta;
        this.importePedido = importePedido;
        this.plazoMeses = plazoMeses;
        this.importePorMes = importePorMes;
        this.interes = interes;
        this.CantidadCuotas = cantidadCuotas;
        this.Estado = estado;
    }

    // Getters
    public int getIdPrestamo() {
        return idPrestamo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Cuenta getCuentaAsociada() {
        return cuentaAsociada;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public double getImportePedido() {
        return importePedido;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public double getImportePorMes() {
        return importePorMes;
    }

    public double getInteres() {
        return interes;
    }

    public int getCantidadCuotas() {
        return CantidadCuotas;
    }

    public String getEstado() {
        return Estado;
    }

    // Setters
    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setCuentaAsociada(Cuenta cuentaAsociada) {
        this.cuentaAsociada = cuentaAsociada;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setImportePedido(double importePedido) {
        this.importePedido = importePedido;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public void setImportePorMes(Double importePorMes) {
        this.importePorMes = importePorMes;
    }

    public void setInteres(Double interes) {
        this.interes = interes;
    }

    public void setCantidadCuotas(int cantidadCuotas) {
        this.CantidadCuotas = cantidadCuotas;
    }

    public void setEstado(String estado) {
        this.Estado = estado;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
               "idPrestamo=" + idPrestamo +
               ", importePedido=" + importePedido +
               ", plazoMeses=" + plazoMeses +
               ", estado='" + Estado + '\'' +
               ", cliente=" + (cliente != null ? cliente.getNombre() : "N/A") +
               '}';
    }
}