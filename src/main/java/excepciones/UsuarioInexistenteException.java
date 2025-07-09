package excepciones;

public class UsuarioInexistenteException extends Exception {
    public UsuarioInexistenteException(String mensaje) {
        super(mensaje);
    }
}
