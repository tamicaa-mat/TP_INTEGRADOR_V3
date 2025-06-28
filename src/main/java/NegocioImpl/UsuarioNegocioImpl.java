package NegocioImpl;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;
import Negocio.UsuarioNegocio;

public class UsuarioNegocioImpl implements UsuarioNegocio {
    private UsuarioDao usuarioDao;

    public UsuarioNegocioImpl() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    @Override
    public Usuario getUsuario(String username, String password) {
        return usuarioDao.getUsuario(username, password);
    }
}