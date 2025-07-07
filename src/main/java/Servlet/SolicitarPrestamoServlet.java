package Servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Negocio.ClienteNegocio;
import NegocioImpl.ClienteNegocioImpl;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Usuario;


@WebServlet("/SolicitarPrestamoServlet")
public class SolicitarPrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public SolicitarPrestamoServlet() {
        super();
    
    }

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("Servlet ejecutado");

        ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl()); 
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario != null) {
            System.out.println("Usuario logueado: " + usuario.getIdUsuario());

            Cliente cliente = clienteNegocio.obtenerClienteConCuentasPorUsuario(usuario.getIdUsuario());

            if (cliente != null) {
                session.setAttribute("clienteLogueado", cliente);
                List<Cuenta> cuentas = cliente.getCuentas();

                if (cuentas == null) {
                    System.out.println("cuentas == null");
                } else if (cuentas.isEmpty()) {
                    System.out.println("cuentas vacía");
                } else {
                    System.out.println("Cuentas obtenidas: " + cuentas.size());
                    for (Cuenta c : cuentas) {
                        System.out.println("- " + c.getIdCuenta() + " | " + c.getNumeroCuenta());
                    }
                }

                request.setAttribute("cuentas", cuentas);
            } else {
                System.out.println("Cliente es null");
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher("/CLIENTEsolicitarPrestamos.jsp");
        rd.forward(request, response);
    }

  
  

}
