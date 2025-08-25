package excepciones;

public class UsuarioInactivoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioInactivoException(String message) {
		super(message);
	}
}
