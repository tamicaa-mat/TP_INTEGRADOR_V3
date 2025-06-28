package dominio;

import java.time.LocalDateTime; 
import java.math.BigDecimal; 

public class Movimiento {
    private int idMovimiento;
    private LocalDateTime fechaHora;
    private String Referencia;
    private BigDecimal Importe;
    private TipoMovimiento tipoMovimiento; 
    private Cuenta cuentaOrigen; 
    private Cuenta cuentaDestino; 

    public Movimiento() {
        // Constructor vacío
    }

    public Movimiento(int idMovimiento, LocalDateTime fechaHora, String concepto,
                      BigDecimal importe, TipoMovimiento tipoMovimiento,
                      Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        this.idMovimiento = idMovimiento;
        this.fechaHora = fechaHora;
        this.Referencia = concepto;
        this.Importe = importe;
        this.tipoMovimiento = tipoMovimiento;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
    }

    // Getters
    public int getIdMovimiento() {
        return idMovimiento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getConcepto() {
        return Referencia;
    }

    public BigDecimal getImporte() {
        return Importe;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    
    
    
    
    // Setters
    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setConcepto(String concepto) {
        this.Referencia = concepto;
    }

    public void setImporte(BigDecimal importe) {
        this.Importe = importe;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    
    
    
    @Override
    public String toString() {
        return "Movimiento{" +
               "idMovimiento=" + idMovimiento +
               ", fechaHora=" + fechaHora +
               ", concepto='" + Referencia + '\'' +
               ", importe=" + Importe +
               ", tipoMovimiento=" + (tipoMovimiento != null ? tipoMovimiento.getDescripcion() : "N/A") +
               ", cuentaOrigen=" + (cuentaOrigen != null ? cuentaOrigen.getNumeroCuenta() : "N/A") +
               ", cuentaDestino=" + (cuentaDestino != null ? cuentaDestino.getNumeroCuenta() : "N/A") +
               '}';
    }
}