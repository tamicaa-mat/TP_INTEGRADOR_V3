package dominio;

import java.math.BigDecimal;
import java.util.Date; 
import java.time.LocalDate;

public class Cuota {
    private int idCuota;
    private Prestamo prestamo; 
    private int numeroCuota;
    private BigDecimal monto;
    private LocalDate fechaPago;
    private boolean estado; // true=Pagada, false=No Pagada.
    private Date fechaVencimiento; 

   
    public Cuota() {}

    // Getters y Setters 

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    // Métodos correctos para el estado de tipo boolean
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return "Cuota{" + "idCuota=" + idCuota + ", numeroCuota=" + numeroCuota + ", monto=" + monto + ", estado="
                + estado + ", prestamoId=" + (prestamo != null ? prestamo.getIdPrestamo() : "N/A") + '}';
    }
}