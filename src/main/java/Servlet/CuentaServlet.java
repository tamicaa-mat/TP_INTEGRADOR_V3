package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dominio.Cliente;
import dominio.Cuenta;
import dominio.TipoCuenta;
import Negocio.CuentaNegocio;
import NegocioImpl.CuentaNegocioImpl;

@WebServlet("/CuentaServlet")
public class CuentaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CuentaServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && action.equals("listar")) {
            String dniCliente = request.getParameter("dni");
            
            CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
            ArrayList<Cuenta> listaCuentas = cuentaNegocio.getCuentasPorCliente(dniCliente);
            
            request.setAttribute("listaCuentas", listaCuentas);
            request.setAttribute("dniCliente", dniCliente); // Para saber a quién pertenece la cuenta
            
            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
            rd.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();

        if (action != null && action.equals("agregar")) {
            String dniCliente = request.getParameter("dniCliente");
            int idTipoCuenta = Integer.parseInt(request.getParameter("idTipoCuenta"));

            Cuenta nuevaCuenta = new Cuenta();
            Cliente cliente = new Cliente();
            cliente.setDni(dniCliente);
            nuevaCuenta.setCliente(cliente);
            
            TipoCuenta tipoCuenta = new TipoCuenta();
            tipoCuenta.setIdTipoCuenta(idTipoCuenta);
            nuevaCuenta.setTipoCuenta(tipoCuenta);

            boolean seAgrego = cuentaNegocio.insert(nuevaCuenta);

            if (!seAgrego) {
                // Si no se agregó (porque ya tiene 3 cuentas), mandamos un mensaje de error
                request.setAttribute("errorLimiteCuentas", "El cliente ya alcanzó el límite de 3 cuentas activas.");
            }
            // Hacemos el forward para mostrar la lista actualizada (con o sin el error)
            ArrayList<Cuenta> listaCuentas = cuentaNegocio.getCuentasPorCliente(dniCliente);
            request.setAttribute("listaCuentas", listaCuentas);
            request.setAttribute("dniCliente", dniCliente);
            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
            rd.forward(request, response);
        }

        if (action != null && action.equals("eliminar")) {
            int idCuenta = Integer.parseInt(request.getParameter("idCuenta"));
            String dniCliente = request.getParameter("dniCliente");
            
            cuentaNegocio.delete(idCuenta);

            // Redirigimos de vuelta a la lista de cuentas de ese cliente
            response.sendRedirect(request.getContextPath() + "/CuentaServlet?action=listar&dni=" + dniCliente);
        }
    }
}