package NegocioImpl;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;

import java.util.ArrayList;

import Negocio.UsuarioNegocio;

public class UsuarioNegocioImpl implements UsuarioNegocio {
   
	private UsuarioDao usuarioDao;

    public UsuarioNegocioImpl() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    
    ////////////////////////////
    @Override
    public Usuario obtenerUsuario(String username, String password) {
        return usuarioDao.obtenerUsuario(username, password);
    }
    
    public boolean actualizarPassword(int idUsuario, String nuevaPassword) {
      
        return usuarioDao.actualizarPassword(idUsuario, nuevaPassword);
    }
    
    
    
    public ArrayList<Usuario> leerTodosLosUsuarios(){
    	 return usuarioDao.leerTodosLosUsuarios();
    }
    
 

    @Override
    public boolean insertarUsuario(Usuario usuario, String dniCliente) {
    
        return usuarioDao.insertarUsuario(usuario, dniCliente);
    }
    
    
    
    
    @Override
    public boolean altaLogicaUsuario(int idUsuario) {
      
        if (usuarioDao != null) {
            return usuarioDao.altaLogicaUsuario(idUsuario);
        }
        return false;
    }
    
    @Override
    public boolean bajaLogicaUsuario(int idUsuario) {

        return usuarioDao.bajaLogicaUsuario(idUsuario);
    }
    
}