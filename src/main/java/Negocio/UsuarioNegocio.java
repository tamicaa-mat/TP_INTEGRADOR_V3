package Negocio;

import dominio.Usuario;

public interface UsuarioNegocio {
    public Usuario getUsuario(String username, String password);
    public boolean actualizarPassword(int idUsuario, String nuevaPassword);
}