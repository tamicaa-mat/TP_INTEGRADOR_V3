package NegocioImpl;

import dao.UsuarioDao;
import dao.ClienteDao;
import daoImpl.UsuarioDaoImpl;
import daoImpl.ClienteDaoImpl;
import dominio.Usuario;
import excepciones.ClaveIncorrectaException;
import excepciones.UsuarioInactivoException;
import excepciones.UsuarioInexistenteException;

import java.util.ArrayList;

import Negocio.UsuarioNegocio;

public class UsuarioNegocioImpl implements UsuarioNegocio {

	private UsuarioDao usuarioDao = new UsuarioDaoImpl();
	private ClienteDao clienteDao = new ClienteDaoImpl();

	public UsuarioNegocioImpl() {
		this.usuarioDao = new UsuarioDaoImpl();
		this.clienteDao = new ClienteDaoImpl();
	}

	@Override
	public Usuario obtenerUsuario(String username, String password) {
		return usuarioDao.obtenerUsuario(username, password);
	}

	public boolean actualizarPassword(int idUsuario, String nuevaPassword) {

		return usuarioDao.actualizarPassword(idUsuario, nuevaPassword);
	}

	public ArrayList<Usuario> leerTodosLosUsuarios() {
		return usuarioDao.leerTodosLosUsuarios();
	}

	@Override
	public boolean insertarUsuario(Usuario usuario, String dniCliente) {
		return usuarioDao.insertarUsuario(usuario, dniCliente);
	}

	@Override
	public boolean altaLogicaUsuario(int idUsuario) {

		if (usuarioDao != null) {
			return usuarioDao.altaLogicaUsuario(idUsuario);
		}
		return false;
	}

	@Override
	public boolean bajaLogicaUsuario(int idUsuario) {

		return usuarioDao.bajaLogicaUsuario(idUsuario);
	}

	@Override
	public Usuario login(String username, String password)
			throws UsuarioInexistenteException, ClaveIncorrectaException, UsuarioInactivoException {
		Usuario usuario = usuarioDao.obtenerUsuarioPorUsername(username);
		if (usuario == null) {
			throw new UsuarioInexistenteException("El usuario no existe.");
		}
		if (!usuario.getPassword().equals(password)) {
			throw new ClaveIncorrectaException("La contrase�a es incorrecta.");
		}
		if (!usuario.isEstado()) {
			throw new UsuarioInactivoException("Tu cuenta se encuentra inactiva.");
		}
		return usuario;
	}

	@Override
	public boolean cambiarEstadoUsuario(int idUsuario, boolean nuevoEstado) {
		return usuarioDao.cambiarEstado(idUsuario, nuevoEstado);
	}

	@Override
	public boolean resetearPasswordUsuario(int idUsuario) {
		String nuevaPassword = "nuevoPass123";
		return usuarioDao.resetearPassword(idUsuario, nuevaPassword);
	}

	@Override
	public boolean existeUsuario(String nombreUsuario) {
		Usuario usuario = usuarioDao.obtenerUsuarioPorUsername(nombreUsuario);
		return usuario != null;
	}

	@Override
	public boolean clienteTieneUsuario(String dniCliente) {
		return usuarioDao.clienteTieneUsuario(dniCliente);
	}
}