package dominio;

import java.math.BigDecimal;
import java.util.Date;


public class Transferencia {

	private int idTransferencia;
	private int idCuentaOrigen;
	private int idCuentaDestino;
	private double Monto;
	
	
    private Date fechaHora;
    private String numeroCuentaOrigen;
    private String numeroCuentaDestino;
    private String nombreClienteOrigen;
    private String nombreClienteDestino;
	

	public Transferencia() {

	}

	public Transferencia(int idTransferencia, int idCuentaOrigen, int idCuentaDestino, double monto) {
		super();
		this.idTransferencia = idTransferencia;
		this.idCuentaOrigen = idCuentaOrigen;
		this.idCuentaDestino = idCuentaDestino;
		this.Monto = monto;
	}

	public int getIdTransferencia() {
		return idTransferencia;
	}

	public void setIdTransferencia(int idTransferencia) {
		this.idTransferencia = idTransferencia;
	}

	public int getIdCuentaOrigen() {
		return idCuentaOrigen;
	}

	public void setIdCuentaOrigen(int idCuentaOrigen) {
		this.idCuentaOrigen = idCuentaOrigen;
	}

	public int getIdCuentaDestino() {
		return idCuentaDestino;
	}

	public void setIdCuentaDestino(int idCuentaDestino) {
		this.idCuentaDestino = idCuentaDestino;
	}

	public double getMonto() {
		return Monto;
	}

	public void setMonto(double monto) {
		Monto = monto;
	}
	
	
	
	
	public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }
    public String getNumeroCuentaOrigen() { return numeroCuentaOrigen; }
    public void setNumeroCuentaOrigen(String numeroCuentaOrigen) { this.numeroCuentaOrigen = numeroCuentaOrigen; }
    public String getNumeroCuentaDestino() { return numeroCuentaDestino; }
    public void setNumeroCuentaDestino(String numeroCuentaDestino) { this.numeroCuentaDestino = numeroCuentaDestino; }
    public String getNombreClienteOrigen() { return nombreClienteOrigen; }
    public void setNombreClienteOrigen(String nombreClienteOrigen) { this.nombreClienteOrigen = nombreClienteOrigen; }
    public String getNombreClienteDestino() { return nombreClienteDestino; }
    public void setNombreClienteDestino(String nombreClienteDestino) { this.nombreClienteDestino = nombreClienteDestino; }

	@Override
	public String toString() {
		return "Transferencia [idTransferencia=" + idTransferencia + ", idCuentaOrigen=" + idCuentaOrigen
				+ ", idCuentaDestino=" + idCuentaDestino + ", Monto=" + Monto + "]";
	}

}
