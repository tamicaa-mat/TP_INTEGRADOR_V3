package dominio;

import java.time.LocalDate; // Importar para FechaCreacion

    public class Cuenta {
    	
    private int idCuenta;
    private Cliente cliente; // Objeto de composición
    private LocalDate fechaCreacion;
    private int tipoCuenta; 
    private String numeroCuenta;
    private String cbu;
    private double saldo; // Usar BigDecimal para dinero por precisión
    private boolean estado; // Coincide con 'Estado' en tu DDL actual

    public Cuenta() {
        // Constructor vacío
    }

    public Cuenta(int idCuenta, Cliente cliente, LocalDate fechaCreacion, int tipoCuenta,
                  String numeroCuenta, String cbu, double saldo, boolean estado) {
        this.idCuenta = idCuenta;
        this.cliente = cliente;
        this.fechaCreacion = fechaCreacion;
        this.tipoCuenta = tipoCuenta;
        this.numeroCuenta = numeroCuenta;
        this.cbu = cbu;
        this.saldo = saldo;
        this.estado = estado;
    }

    // Getters
    public int getIdCuenta() {
        return idCuenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public int getTipoCuenta() {
        return tipoCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getCbu() {
        return cbu;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isEstado() {
        return estado;
    }

    // Setters
    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setTipoCuenta(int tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

	@Override
	public String toString() {
		return "Cuenta [idCuenta=" + idCuenta + ", cliente=" + cliente + ", fechaCreacion=" + fechaCreacion
				+ ", tipoCuenta=" + tipoCuenta + ", numeroCuenta=" + numeroCuenta + ", cbu=" + cbu + ", saldo=" + saldo
				+ ", estado=" + estado + "]";
	}

  
  
}