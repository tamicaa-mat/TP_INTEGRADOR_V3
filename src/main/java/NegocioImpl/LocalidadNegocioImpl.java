
package NegocioImpl;

import java.util.ArrayList;
import dao.LocalidadDao;
import daoImpl.LocalidadDaoImpl;
import dominio.Localidad;
import Negocio.LocalidadNegocio;

public class LocalidadNegocioImpl implements LocalidadNegocio {

    private LocalidadDao localidadDao;

    public LocalidadNegocioImpl() {
        this.localidadDao = new LocalidadDaoImpl();
    }

    @Override
    public ArrayList<Localidad> leerTodasLasLocalidades() {
        return localidadDao.leerTodasLasLocalidades();
    }
}