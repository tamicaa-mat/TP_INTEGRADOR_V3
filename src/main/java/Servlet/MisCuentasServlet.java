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

import dominio.Cliente;
import dominio.Cuenta;
import Negocio.CuentaNegocio;
import NegocioImpl.CuentaNegocioImpl;

@WebServlet("/MisCuentasServlet")
public class MisCuentasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MisCuentasServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        // Seguridad: si no hay un cliente en la sesión, lo redirigimos al login.
        if (clienteLogueado == null) {
            response.sendRedirect("login.jsp?error=1");
            return;
        }

        // 1. Llamamos a la capa de negocio para obtener las cuentas del cliente.
        CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
        List<Cuenta> listaCuentas = cuentaNegocio.obtenerCuentasPorIdCliente(clienteLogueado.getIdCliente(), clienteLogueado);

        // 2. Guardamos la lista en el request para que el JSP pueda accederla.
        request.setAttribute("listaCuentas", listaCuentas);

        // 3. Redirigimos la petición al JSP para que muestre los datos.
        RequestDispatcher rd = request.getRequestDispatcher("/clienteMisCuentas.jsp");
        rd.forward(request, response);
    }
}
