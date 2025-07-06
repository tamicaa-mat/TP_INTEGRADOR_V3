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

    
    ////////////////////////////
    @Override
    public Usuario getUsuario(String username, String password) {
        return usuarioDao.getUsuario(username, password);
    }
    
    public boolean actualizarPassword(int idUsuario, String nuevaPassword) {
      
        return usuarioDao.actualizarPassword(idUsuario, nuevaPassword);
    }
    
 

    @Override
    public boolean insert(Usuario usuario, String dniCliente) {
        
        // - se puede verificar que el nombre de usuario no este ya en uso
        // - se puede verificar que la contraseña tenga un mínimo de caracteres por  ejemplo

   
        return usuarioDao.insert(usuario, dniCliente);
    }
    
}