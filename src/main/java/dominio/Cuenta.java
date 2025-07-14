package dominio;

import java.math.BigDecimal;

import java.time.LocalDate; // Importar para FechaCreacion
import java.time.format.DateTimeFormatter;

public class Cuenta {

	    private int idCuenta;
	    Cliente cliente;
	    int IdCliente;
	    private LocalDate fechaCreacion;
	    private TipoCuenta tipoCuenta; // ÚNICO atributo para el tipo de cuenta
	    private String numeroCuenta;
	    private String cbu;
	    private BigDecimal saldo; // Usar BigDecimal para dinero por precisión
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

		public TipoCuenta getTipoCuenta() {
			return tipoCuenta;
		}

		public void setTipoCuenta(TipoCuenta tipoCuenta) {
			this.tipoCuenta = tipoCuenta;
		}

		public TipoCuenta getTipoCuentaObjeto() {
			return tipoCuenta;
		}

		public String getFechaCreacionFormateada() {
		    return fechaCreacion != null ? fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
		}
		
		
		
		public void setTipoCuentaObjeto(TipoCuenta tipoCuenta) {
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

		public BigDecimal getSaldo() {
			return saldo;
		}

		public void setSaldo(BigDecimal d) {
			this.saldo = d;
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
					+ ", fechaCreacion=" + fechaCreacion + ", tipoCuenta=" + tipoCuenta + ", tipoCuentaObjeto="
					+ tipoCuenta + ", numeroCuenta=" + numeroCuenta + ", cbu=" + cbu + ", saldo=" + saldo
					+ ", estado=" + estado + "]";
		}

	
	
	
	
	
}