package dao;

import dominio.Usuario;

public interface UsuarioDao {
	
	    // boolean insert(Usuario usuario);
	    
	    
	    public  boolean bajaLogicaUsuario(int IdUsuario); // dará de baja logica
	    
	  
	    // ArrayList<Usuario> readAll();
	  
	   
	   // boolean update(Usuario usuario);
	    
       Usuario obtenerUsuario(String username, String password);
       boolean actualizarPassword(int idUsuario, String nuevaPassword);
       public boolean insertarUsuario(Usuario usuario, String dniCliente);
    
    
}