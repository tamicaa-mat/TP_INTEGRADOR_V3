package dao;

import java.util.ArrayList;

import dominio.Usuario;

public interface UsuarioDao {

	public boolean bajaLogicaUsuario(int IdUsuario);

	public boolean altaLogicaUsuario(int IdUsuario);

	public ArrayList<Usuario> leerTodosLosUsuarios();

	Usuario obtenerUsuario(String username, String password);

	boolean actualizarPassword(int idUsuario, String nuevaPassword);

	public boolean insertarUsuario(Usuario usuario, String dniCliente);

	Usuario obtenerUsuarioPorUsername(String username);

	boolean cambiarEstadoUsuario(int idUsuario, boolean nuevoEstado);

	boolean resetearPassword(int idUsuario, String nuevaPassword);

	boolean clienteTieneUsuario(String dniCliente);
}