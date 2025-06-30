package dao;

import dominio.Usuario;

public interface UsuarioDao {
	
    public Usuario getUsuario(String username, String password);
    public boolean actualizarPassword(int idUsuario, String nuevaPassword);
    
    
}