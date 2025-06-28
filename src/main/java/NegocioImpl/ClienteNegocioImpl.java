package NegocioImpl;

import java.util.ArrayList;
import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import Negocio.ClienteNegocio;

public class ClienteNegocioImpl implements ClienteNegocio {
    
    private ClienteDao cdao = new ClienteDaoImpl();

    @Override
    public boolean insert(Cliente cliente) {
        return cdao.insert(cliente);
    }

    @Override
    public boolean delete(String dni) {
        return cdao.delete(dni);
    }

    @Override
    public ArrayList<Cliente> readAll() {
        return cdao.readAll();
    }
}