package Negocio;

import java.util.ArrayList;
import dominio.Cuenta;

public interface CuentaNegocio {

 
    public boolean insert(Cuenta cuenta);

    public boolean delete(int idCuenta);

    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente);
    
    
    public Cuenta getCuentaPorCbu(String cbu);
}