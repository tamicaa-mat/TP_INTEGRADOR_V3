package dominio;

public class Transferencia {

	private int idTransferencia;
	private int idCuentaOrigen;
	private int idCuentaDestino;
	
	
	public Transferencia() {
		
		
	}
	
	public Transferencia(int idTransferencia, int idCuentaOrigen, int idCuentaDestino) {
		super();
		this.idTransferencia = idTransferencia;
		this.idCuentaOrigen = idCuentaOrigen;
		this.idCuentaDestino = idCuentaDestino;
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


	@Override
	public String toString() {
		return "Transferencia [idTransferencia=" + idTransferencia + ", idCuentaOrigen=" + idCuentaOrigen
				+ ", idCuentaDestino=" + idCuentaDestino + "]";
	}
	
	
	
	
	
}
