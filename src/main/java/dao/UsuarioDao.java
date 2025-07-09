package dao;

import java.util.ArrayList;

import dominio.Usuario;

public interface UsuarioDao {
	
	    // boolean insert(Usuario usuario);
	    
	    
	    public  boolean bajaLogicaUsuario(int IdUsuario); // dará de baja logica
	    public boolean altaLogicaUsuario (int IdUsuario); // cambia el estado a 1
	    
	  
	     public ArrayList<Usuario> leerTodosLosUsuarios();
	  
	   
	   // boolean update(Usuario usuario);
	    
       Usuario obtenerUsuario(String username, String password);
       boolean actualizarPassword(int idUsuario, String nuevaPassword);
       public boolean insertarUsuario(Usuario usuario, String dniCliente);
    
    
}