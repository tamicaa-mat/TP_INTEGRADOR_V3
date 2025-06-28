package dao;

import java.util.ArrayList;
import dominio.Cuenta;

public interface CuentaDao {


    public boolean insert(Cuenta cuenta);

  
    public boolean delete(int idCuenta);


    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente);

    public int countCuentasActivasPorCliente(String dniCliente);
    

    public Cuenta getCuentaPorCbu(String cbu);

}