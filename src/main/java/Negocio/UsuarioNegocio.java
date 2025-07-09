package Negocio;

import java.util.ArrayList;

import dominio.Usuario;

public interface UsuarioNegocio {
    public Usuario obtenerUsuario(String username, String password);
    public boolean actualizarPassword(int idUsuario, String nuevaPassword);
    public boolean altaLogicaUsuario ( int idUsuario);
    public boolean bajaLogicaUsuario(int idUsuario); 
    public boolean insertarUsuario(Usuario usuario, String dniCliente);
    public ArrayList<Usuario> leerTodosLosUsuarios();

}