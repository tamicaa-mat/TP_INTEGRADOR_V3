package dao;

import java.util.ArrayList;

import dominio.Cuenta;

public interface CuentaDao {
	
    boolean insert(Cuenta cuenta);
    boolean delete(int idCuenta);
    int cuentasActivasPorCliente(String dniCliente); 
    ArrayList<Cuenta> getCuentasPorIdCliente(int idCliente);
	Cuenta getCuentaPorCbu(String cbu);
	ArrayList<Cuenta> getCuentasPorCliente(String dniCliente);
 
    
}
   