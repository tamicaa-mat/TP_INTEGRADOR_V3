package Negocio;

import dominio.Usuario;

public interface UsuarioNegocio {
    public Usuario getUsuario(String username, String password);
}