package dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.sql.Date; 


public class Movimiento {
	
    private int idMovimiento;
    private LocalDateTime fechaHora;
    private String Referencia;
    private BigDecimal Importe;
    private TipoMovimiento tipoMovimiento; 
    private Cuenta Cuenta; 
  

    public Movimiento() {
        // Constructor vacío
    }

    public Movimiento(int idMovimiento, LocalDateTime fechaHora, String concepto,
                      BigDecimal importe, TipoMovimiento tipoMovimiento,
                      Cuenta Cuenta) {
        this.idMovimiento = idMovimiento;
        this.fechaHora = fechaHora;
        this.Referencia = concepto;
        this.Importe = importe;
        this.tipoMovimiento = tipoMovimiento;
        this.Cuenta= Cuenta;
       
    }
    
    
    public String getFechaHoraFormateada() {
        if (this.fechaHora == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return this.fechaHora.format(formatter);
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

	   public Cuenta getCuenta() {
	        return Cuenta;
	    }

	    public void setCuenta(Cuenta cuenta) {
	        this.Cuenta = cuenta;
	    }

	@Override
	public String toString() {
		return "Movimiento [idMovimiento=" + idMovimiento + ", fechaHora=" + fechaHora + ", Referencia=" + Referencia
				+ ", Importe=" + Importe + ", tipoMovimiento=" + tipoMovimiento + ", idCuenta=" + Cuenta + "]";
	}


    
  

	

	
}