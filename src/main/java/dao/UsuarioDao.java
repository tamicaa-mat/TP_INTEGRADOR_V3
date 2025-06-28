package dao;

import dominio.Usuario;

public interface UsuarioDao {
    public Usuario getUsuario(String username, String password);
}