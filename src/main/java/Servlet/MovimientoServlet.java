package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Negocio.ClienteNegocio;
import Negocio.MovimientoNegocio;
import NegocioImpl.ClienteNegocioImpl;
import NegocioImpl.MovimientoNegocioImpl;
import daoImpl.ClienteDaoImpl;
import daoImpl.MovimientoDaoImpl;
import dominio.Cliente;
import dominio.Movimiento;
import dominio.Usuario;

@WebServlet("/MovimientoServlet")
public class MovimientoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl(new MovimientoDaoImpl());
    private ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MovimientoServlet ejecutado");

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Obtener el cliente completo con sus cuentas
        Cliente cliente = clienteNegocio.obtenerClienteConCuentasPorUsuario(usuario.getIdUsuario());

        if (cliente == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Leer filtros
        String cuentaStr = request.getParameter("cuenta");
        String tipoStr = request.getParameter("tipo");

        int idCuenta = (cuentaStr != null && !cuentaStr.isEmpty()) ? Integer.parseInt(cuentaStr) : 0;
        int idTipo = (tipoStr != null && !tipoStr.isEmpty()) ? Integer.parseInt(tipoStr) : 0;

        List<Movimiento> movimientos = new ArrayList<>();
        if (idCuenta > 0) {
            movimientos = movimientoNegocio.listarMovimientos(idCuenta, idTipo);
        }

        // Enviar a la vista
        request.setAttribute("cuentas", cliente.getCuentas());
        request.setAttribute("movimientos", movimientos);
        request.setAttribute("filtroCuenta", idCuenta);
        request.setAttribute("filtroTipo", idTipo);

        System.out.println("Cliente en servlet movimiento: " + cliente.getIdCliente());
        List<Movimiento> todosLosMovimientos = movimientoNegocio.obtenerMovimientosPorCliente(cliente.getIdCliente());
        request.setAttribute("todosLosMovimientos", todosLosMovimientos);
        
        request.getRequestDispatcher("ClienteListaMovimientos.jsp").forward(request, response);
        
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
