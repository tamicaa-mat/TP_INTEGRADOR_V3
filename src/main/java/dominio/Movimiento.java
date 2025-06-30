package dominio;

import java.time.LocalDateTime; 
import java.math.BigDecimal; 

public class Movimiento {
    private int idMovimiento;
    private LocalDateTime fechaHora;
    private String Referencia;
    private BigDecimal Importe;
    private TipoMovimiento tipoMovimiento; 
    private int idCuenta; 
  

    public Movimiento() {
        // Constructor vacío
    }

    public Movimiento(int idMovimiento, LocalDateTime fechaHora, String concepto,
                      BigDecimal importe, TipoMovimiento tipoMovimiento,
                      int idCuentaMov) {
        this.idMovimiento = idMovimiento;
        this.fechaHora = fechaHora;
        this.Referencia = concepto;
        this.Importe = importe;
        this.tipoMovimiento = tipoMovimiento;
        this.idCuenta = idCuentaMov;
       
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

    public int getIdCuenta() {
		return idCuenta;
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

    public void setIdCuenta(int idCuentaMov) {
		this.idCuenta = idCuentaMov;
	}

	@Override
	public String toString() {
		return "Movimiento [idMovimiento=" + idMovimiento + ", fechaHora=" + fechaHora + ", Referencia=" + Referencia
				+ ", Importe=" + Importe + ", tipoMovimiento=" + tipoMovimiento + ", idCuenta=" + idCuenta + "]";
	}
    
    
  

	

	
}