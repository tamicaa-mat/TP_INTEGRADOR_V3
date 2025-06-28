package dominio;

import java.time.LocalDate; // Importar para FechaPago


public class Cuota {
    private int idCuota;
    private Prestamo prestamo; // Objeto de composición
    private int numeroCuota;
    private double monto;
    private LocalDate fechaPago; // Puede ser null si la cuota no ha sido pagada
    private String Estado; // Coincide con 'EstadoPago VARCHAR(20)' en tu DDL actual

    public Cuota() {
        // Constructor vacío
    }

    public Cuota(int idCuota, Prestamo prestamo, int numeroCuota, double monto,
                 LocalDate fechaPago, String estadoPago) {
        this.idCuota = idCuota;
        this.prestamo = prestamo;
        this.numeroCuota = numeroCuota;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.Estado = estadoPago;
    }

    // Getters
    public int getIdCuota() {
        return idCuota;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public String getEstadoPago() {
        return Estado;
    }

    // Setters
    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.Estado = estadoPago;
    }

    @Override
    public String toString() {
        return "Cuota{" +
               "idCuota=" + idCuota +
               ", numeroCuota=" + numeroCuota +
               ", monto=" + monto +
               ", estadoPago='" + Estado + '\'' +
               ", prestamoId=" + (prestamo != null ? prestamo.getIdPrestamo() : "N/A") +
               '}';
    }
}