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
        System.out.println("Servlet ejecutado (GET)");
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario != null) {
            String cuentaSeleccionadaStr = request.getParameter("cuentaSeleccionada");
            String importeStr = request.getParameter("importe");
            String plazoStr = request.getParameter("plazo");

            if (cuentaSeleccionadaStr != null && importeStr != null && plazoStr != null) {
                // El usuario está enviando la solicitud de préstamo
                try {
                    int idCuenta = Integer.parseInt(cuentaSeleccionadaStr);
                    double importe = Double.parseDouble(importeStr);
                    int plazo = Integer.parseInt(plazoStr);

                    System.out.println("Solicitud recibida:");
                    System.out.println("- Cuenta: " + idCuenta);
                    System.out.println("- Importe: " + importe);
                    System.out.println("- Plazo: " + plazo + " meses");

                    // Aquí podés hacer lógica de negocio, guardar el préstamo, etc.
                    // Por ejemplo: PrestamoNegocio.agregarPrestamo(...)

                    request.setAttribute("mensajeExito", "¡Solicitud enviada con éxito!");

                } catch (NumberFormatException ex) {
                    request.setAttribute("mensajeError", "Datos inválidos.");
                }

            } else {
                // Primera vez que entra: solo cargar cuentas
                ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());
                Cliente cliente = clienteNegocio.obtenerClienteConCuentasPorUsuario(usuario.getIdUsuario());

                if (cliente != null) {
                    session.setAttribute("clienteLogueado", cliente);
                    List<Cuenta> cuentas = cliente.getCuentas();
                    request.setAttribute("cuentas", cuentas);
                }
            }
        }

        request.getRequestDispatcher("/CLIENTEsolicitarPrestamos.jsp").forward(request, response);
    }

    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); 
    }
}
