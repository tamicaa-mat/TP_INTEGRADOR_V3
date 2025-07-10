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

	public int getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(int idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getReferencia() {
		return Referencia;
	}

	public void setReferencia(String referencia) {
		Referencia = referencia;
	}

	public BigDecimal getImporte() {
		return Importe;
	}

	public void setImporte(BigDecimal d) {
		Importe = d;
	}

	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public int getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

	@Override
	public String toString() {
		return "Movimiento [idMovimiento=" + idMovimiento + ", fechaHora=" + fechaHora + ", Referencia=" + Referencia
				+ ", Importe=" + Importe + ", tipoMovimiento=" + tipoMovimiento + ", idCuenta=" + idCuenta + "]";
	}

    
  

	

	
}