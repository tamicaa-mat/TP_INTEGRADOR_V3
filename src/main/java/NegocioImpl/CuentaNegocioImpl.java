package NegocioImpl;

import java.util.ArrayList;

import dao.CuentaDao;
import daoImpl.CuentaDaoImpl;
import dominio.Cuenta;
import Negocio.CuentaNegocio;

public class CuentaNegocioImpl implements CuentaNegocio {

    private CuentaDao cuentaDao;

    public CuentaNegocioImpl() {
        this.cuentaDao = new CuentaDaoImpl();
    }

    @Override
    public boolean insert(Cuenta cuenta) {
        // Regla de Negocio: No se puede agregar una cuenta si el cliente ya tiene 3.
        int cantidadCuentas = cuentaDao.countCuentasActivasPorCliente(cuenta.getCliente().getDni());
        if (cantidadCuentas >= 3) {
            return false; // No se puede insertar, se alcanzó el límite.
        }
        
        return cuentaDao.insert(cuenta);
    }

    @Override
    public boolean delete(int idCuenta) {
        return cuentaDao.delete(idCuenta);
    }

    @Override
    public ArrayList<Cuenta> getCuentasPorCliente(String dniCliente) {
        return cuentaDao.getCuentasPorCliente(dniCliente);
    }

    @Override
    public Cuenta getCuentaPorCbu(String cbu) {
        return cuentaDao.getCuentaPorCbu(cbu);
    }
}