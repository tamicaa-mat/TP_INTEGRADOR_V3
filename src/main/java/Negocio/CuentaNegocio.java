package Negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dominio.Cliente;
import dominio.Cuenta;

public interface CuentaNegocio {

	
	
	public List<Cuenta> obtenerCuentasPorCliente(int idCliente);

    public Cuenta getCuentaPorCbu(String cbu,Cliente cliente);
    
    public int cuentasActivasPorCliente(String  dniCliente);
    
    public boolean agregarCuenta(Cuenta cuenta,Cliente cliente);


	public List<Cuenta> obtenerCuentasPorIdCliente(int idCliente,Cliente cliente); 

	public ArrayList<Cuenta> getCuentasPorCliente(Cliente cliente);
	
	int contarCuentasCreadasEntreFechas(Date desde, Date hasta);
	
    double obtenerSaldoTotalCuentasCreadasEntreFechas(Date desde, Date hasta);
    
    
    public String generarNumeroCuenta(String dniCliente);

	public String generarNumeroCbu(String numeroCuenta);

	public boolean darDeBajaLogicaCuentas(int idCuenta);
	
	public int obtenerIdCuentaPorNumero(String numeroCuenta);

	public Cuenta buscarCuentaPorId(int idCuenta);
	
	boolean actualizarSaldo(int idCuenta, double nuevoSaldo);

}