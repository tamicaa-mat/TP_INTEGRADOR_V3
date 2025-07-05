package dominio;

import java.time.LocalDate; // Importar para FechaCreacion

public class Cuenta {

	  private int idCuenta;
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

	    public Cuenta(int idCuenta, int IdCliente, LocalDate fechaCreacion, int tipoCuenta,
	                  String numeroCuenta, String cbu, double saldo, boolean estado) {
	        this.idCuenta = idCuenta;
	       
	        this.fechaCreacion = fechaCreacion;
	        this.tipoCuenta = tipoCuenta;
	        this.numeroCuenta = numeroCuenta;
	        this.cbu = cbu;
	        this.saldo = saldo;
	        this.estado = estado;
	    }

		public int getIdCuenta() {
			return idCuenta;
		}

		public void setIdCuenta(int idCuenta) {
			this.idCuenta = idCuenta;
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
			return "Cuenta [idCuenta=" + idCuenta + ", IdCliente=" + IdCliente + ", fechaCreacion=" + fechaCreacion
					+ ", tipoCuenta=" + tipoCuenta + ", numeroCuenta=" + numeroCuenta + ", cbu=" + cbu + ", saldo=" + saldo
					+ ", estado=" + estado + "]";
		}

	
	
	
	
	
	
	
	
	
	
	
}