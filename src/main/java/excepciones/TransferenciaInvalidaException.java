package excepciones;

public class TransferenciaInvalidaException extends RuntimeException {
	public TransferenciaInvalidaException(String mensaje) {
		super(mensaje);
	}
}
