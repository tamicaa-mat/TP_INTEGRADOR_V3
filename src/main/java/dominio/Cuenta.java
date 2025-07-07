package dominio;

import java.time.LocalDate; // Importar para FechaCreacion

public class Cuenta {

	    private int idCuenta;
	    Cliente cliente;
	    int IdCliente;
	    private LocalDate fechaCreacion;
	    private int tipoCuenta; 
	    private String numeroCuenta;
	    private String cbu;
	    private double saldo; // Usar BigDecimal para dinero por precisión
	    private boolean estado; // Coincide con 'Estado' en tu DDL actual

	    public Cuenta() {
	        // Constructor vacío
	    }

		public int getIdCuenta() {
			return idCuenta;
		}

		public void setIdCuenta(int idCuenta) {
			this.idCuenta = idCuenta;
		}

		public Cliente getCliente() {
			return cliente;
		}

		public void setCliente(Cliente cliente) {
			this.cliente = cliente;
		}

		public int getIdCliente() {
			return IdCliente;
		}

		public void setIdCliente(int idCliente) {
			IdCliente = idCliente;
		}

		public LocalDate getFechaCreacion() {
			return fechaCreacion;
		}

		public void setFechaCreacion(LocalDate fechaCreacion) {
			this.fechaCreacion = fechaCreacion;
		}

		public int getTipoCuenta() {
			return tipoCuenta;
		}

		public void setTipoCuenta(int tipoCuenta) {
			this.tipoCuenta = tipoCuenta;
		}

		public String getNumeroCuenta() {
			return numeroCuenta;
		}

		public void setNumeroCuenta(String numeroCuenta) {
			this.numeroCuenta = numeroCuenta;
		}

		public String getCbu() {
			return cbu;
		}

		public void setCbu(String cbu) {
			this.cbu = cbu;
		}

		public double getSaldo() {
			return saldo;
		}

		public void setSaldo(double saldo) {
			this.saldo = saldo;
		}

		public boolean isEstado() {
			return estado;
		}

		public void setEstado(boolean estado) {
			this.estado = estado;
		}

		@Override
		public String toString() {
			return "Cuenta [idCuenta=" + idCuenta + ", cliente=" + cliente + ", IdCliente=" + IdCliente
					+ ", fechaCreacion=" + fechaCreacion + ", tipoCuenta=" + tipoCuenta + ", numeroCuenta="
					+ numeroCuenta + ", cbu=" + cbu + ", saldo=" + saldo + ", estado=" + estado + "]";
		}

	 
	
	
	
	
}