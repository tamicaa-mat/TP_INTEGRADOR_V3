package dao;

import java.util.ArrayList;

import dominio.Cliente;
import dominio.Cuenta;

public interface CuentaDao {

	
    boolean insert(Cuenta cuenta);
    boolean delete(int idCuenta);
    int cuentasActivasPorCliente(String dniCliente); 
    ArrayList<Cuenta> getCuentasPorIdCliente(int idCliente,Cliente cliente);
	ArrayList<Cuenta> getCuentasPorCliente(String dniCliente, Cliente cliente);
	Cuenta getCuentaPorCbu(String cbu, Cliente cliente);
	
	/// DOS MILAGROS
 


}