package excepciones;

public class UsuarioExistenteException extends Exception {
	public UsuarioExistenteException(String mensaje) {
		super(mensaje);
	}
}
