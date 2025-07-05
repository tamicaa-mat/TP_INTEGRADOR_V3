package Negocio;

import dominio.Usuario;

public interface UsuarioNegocio {
    public Usuario getUsuario(String username, String password);
    public boolean actualizarPassword(int idUsuario, String nuevaPassword);
    
 
    public boolean insert(Usuario usuario, String dniCliente);
}