package Negocio;

import java.util.ArrayList;
import java.util.List;

import dominio.Cuenta;

public interface CuentaNegocio {

 

    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente);
    
    
    public Cuenta getCuentaPorCbu(String cbu);
    
    public int cuentasActivasPorCliente(String  dniCliente);
    
    public boolean agregarCuenta(Cuenta cuenta);


	public List<Cuenta> getCuentasPorIdCliente(int idCliente);


	public List<Cuenta> ObtenerCuentasPorIdCliente(int idCliente); 



}