package Negocio;

import java.util.ArrayList;

import dominio.Usuario;
import excepciones.ClaveIncorrectaException;
import excepciones.UsuarioInactivoException;
import excepciones.UsuarioInexistenteException;

public interface UsuarioNegocio {
    public Usuario obtenerUsuario(String username, String password);
    public boolean actualizarPassword(int idUsuario, String nuevaPassword);
    public boolean altaLogicaUsuario ( int idUsuario);
    public boolean bajaLogicaUsuario(int idUsuario); 
    public boolean insertarUsuario(Usuario usuario, String dniCliente);
    public ArrayList<Usuario> leerTodosLosUsuarios();
    Usuario login(String username, String password) throws UsuarioInexistenteException, ClaveIncorrectaException, UsuarioInactivoException;

    
    
    public boolean resetearPasswordUsuario(int idUsuario);
    public boolean cambiarEstadoUsuario(int idUsuario, boolean nuevoEstado);
}