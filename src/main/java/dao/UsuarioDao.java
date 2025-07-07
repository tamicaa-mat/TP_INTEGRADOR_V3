package dao;

import dominio.Usuario;

public interface UsuarioDao {
	
	    // boolean insert(Usuario usuario);
	    
	    
	    public  boolean delete(int IdUsuario); // dará de baja logica
	    
	  
	    // ArrayList<Usuario> readAll();
	  
	   
	   // boolean update(Usuario usuario);
	    
       Usuario getUsuario(String username, String password);
       boolean actualizarPassword(int idUsuario, String nuevaPassword);
       public boolean insert(Usuario usuario, String dniCliente);
    
    
}