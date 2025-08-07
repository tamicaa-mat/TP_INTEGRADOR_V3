package excepciones;

/**
 * Excepción personalizada que se lanza cuando una operación de negocio
 * no se puede completar porque viola una regla específica (ej: intentar
 * desactivar un usuario con préstamos activos).
 */
public class OperacionInvalidaException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje que describe por qué la operación es inválida.
     */
    public OperacionInvalidaException(String message) {
        super(message);
    }
}
