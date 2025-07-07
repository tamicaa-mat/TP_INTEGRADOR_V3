package Negocio;

import java.util.ArrayList;
import java.util.List;

import dominio.Cliente;
import dominio.Cuenta;

public interface CuentaNegocio {


    public Cuenta getCuentaPorCbu(String cbu,Cliente cliente);
    
    public int cuentasActivasPorCliente(String  dniCliente);
    
    public boolean agregarCuenta(Cuenta cuenta,Cliente cliente);


	public List<Cuenta> obtenerCuentasPorIdCliente(int idCliente,Cliente cliente); 

	public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente, Cliente cliente);

}