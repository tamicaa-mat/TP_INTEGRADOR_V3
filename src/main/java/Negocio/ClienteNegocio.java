package Negocio;

import java.util.ArrayList;
import dominio.Cliente;

public interface ClienteNegocio {
    public boolean insert(Cliente cliente);
    public boolean delete(String dni);
    public ArrayList<Cliente> readAll();
}