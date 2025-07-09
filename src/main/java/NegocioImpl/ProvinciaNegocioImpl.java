package NegocioImpl;

import java.util.ArrayList;
import dao.ProvinciaDao;
import daoImpl.ProvinciaDaoImpl;
import dominio.Provincia;
import Negocio.ProvinciaNegocio;

public class ProvinciaNegocioImpl implements ProvinciaNegocio {

    private ProvinciaDao provinciaDao;

    public ProvinciaNegocioImpl() {
        this.provinciaDao = new ProvinciaDaoImpl();
    }

    @Override
    public ArrayList<Provincia> leerTodasLasProvincias() {
        return provinciaDao.leerTodasLasProvincias();
    }
}