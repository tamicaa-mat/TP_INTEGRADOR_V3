package dominio;

import java.math.BigDecimal;

public class Transferencia {

	private int idTransferencia;
	private int idCuentaOrigen;
	private int idCuentaDestino;
	private double Monto;

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

	@Override
	public String toString() {
		return "Transferencia [idTransferencia=" + idTransferencia + ", idCuentaOrigen=" + idCuentaOrigen
				+ ", idCuentaDestino=" + idCuentaDestino + ", Monto=" + Monto + "]";
	}

}
