package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dominio.Cliente;
import dominio.Cuenta;

public interface CuentaDao {
	
	
	public List<Cuenta> obtenerCuentasPorCliente(int idCliente);

	public Cuenta obtenerPorId(int idCuenta);
	
	public Cuenta obtenerPorCbu(String cbu);
	
    boolean insert(Cuenta cuenta);
    boolean delete(int idCuenta);
    int cuentasActivasPorCliente(String dniCliente); 
    ArrayList<Cuenta> getCuentasPorIdCliente(int idCliente,Cliente cliente);
    
    ArrayList<Cuenta> getCuentasPorIdCliente2OTRA(int idCliente,Cliente cliente);
	ArrayList<Cuenta> getCuentasPorCliente(Cliente cliente);
	Cuenta getCuentaPorCbu(String cbu, Cliente cliente);
	
	int contarCuentasCreadasEntreFechas(Date desde, Date hasta);
	double obtenerSaldoTotalCuentasCreadasEntreFechas(Date desde, Date hasta);
	
	
	
	int getIdCuentaPorNumeroCuenta(String numeroCuenta);

	public Cuenta buscarCuentaPorIdDao2(int idCuenta, Connection conn);  
	public Cuenta buscarCuentaPorIdDao(int idCuenta);
	public boolean actualizarSaldo(int idCuenta, BigDecimal nuevoSaldo, Connection conn);
	boolean actualizarSaldo(int idCuenta, double nuevoSaldo);

	
}