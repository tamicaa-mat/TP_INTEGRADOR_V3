package Servlet; // O "servelt" según la estructura de tu proyecto

import java.io.IOException;
import java.math.BigDecimal;
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
import dominio.Movimiento; // Importación necesaria
import Negocio.CuentaNegocio;
import Negocio.MovimientoNegocio; // Importación necesaria
import Negocio.TransferenciaNegocio;
import NegocioImpl.CuentaNegocioImpl;
import NegocioImpl.MovimientoNegocioImpl; // Importación necesaria
import NegocioImpl.TransferenciaNegocioImpl;

@WebServlet("/TransferenciaServlet")
public class TransferenciaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TransferenciaServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        // Seguridad: si no hay cliente en sesión, se va al login.
        if (clienteLogueado == null) {
            response.sendRedirect("login.jsp?mensaje=Debe iniciar sesion");
            return;
        }

        String action = request.getParameter("action");

        // Si la acción es mostrar el formulario para una NUEVA transferencia...
        if ("mostrarFormulario".equals(action)) {
            cargarFormulario(request, response, clienteLogueado);
        } 
        // Si no, por defecto, se muestra el HISTORIAL de movimientos.
        else {
            mostrarHistorial(request, response, clienteLogueado);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        if (clienteLogueado == null) {
            response.sendRedirect("login.jsp?mensaje=Debe iniciar sesion");
            return;
        }
        
        String action = request.getParameter("action");
        
        // Procesa el envío del formulario de nueva transferencia
        if ("transferir".equals(action)) {
            realizarTransferencia(request, response, clienteLogueado);
        } else {
            // Por si acaso, si llega un POST raro, lo mandamos al historial.
            mostrarHistorial(request, response, clienteLogueado);
        }
    }

    private void mostrarHistorial(HttpServletRequest request, HttpServletResponse response, Cliente cliente) throws ServletException, IOException {
        try {
            MovimientoNegocio movNegocio = new MovimientoNegocioImpl();
            List<Movimiento> historial = movNegocio.obtenerMovimientosPorCliente(cliente.getIdCliente());
            
            request.setAttribute("historialTransferencias", historial);
            
            RequestDispatcher rd = request.getRequestDispatcher("/CLIENTEtransferencias.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de error si falla la carga del historial
        }
    }

    private void cargarFormulario(HttpServletRequest request, HttpServletResponse response, Cliente cliente) throws ServletException, IOException {
        try {
            CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
            List<Cuenta> listaCuentas = cuentaNegocio.obtenerCuentasPorCliente(cliente.getIdCliente());
            
            request.setAttribute("cuentas", listaCuentas);
            
            RequestDispatcher rd = request.getRequestDispatcher("/ClienteNuevaTransferencia.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de error si falla la carga de cuentas
        }
    }

    private void realizarTransferencia(HttpServletRequest request, HttpServletResponse response, Cliente cliente) throws ServletException, IOException {
        String mensaje = null;
        try {
            int idCuentaOrigen = Integer.parseInt(request.getParameter("idCuentaOrigen"));
            String cbuDestino = request.getParameter("cbuDestino");
            BigDecimal monto = new BigDecimal(request.getParameter("monto"));

            TransferenciaNegocio transferenciaNegocio = new TransferenciaNegocioImpl();
            boolean exito = transferenciaNegocio.realizarTransferencia(idCuentaOrigen, cbuDestino, monto);

            if (exito) {
                mensaje = "¡Transferencia realizada con éxito!";
            } else {
                mensaje = "No se pudo realizar la transferencia. Verifique los datos.";
            }
        } catch (Exception e) {
            mensaje = e.getMessage(); // Mostramos el mensaje de error específico (ej. "Saldo insuficiente")
        }
        
        // Después de intentar la transferencia, volvemos al formulario para mostrar el mensaje
        request.setAttribute("mensaje", mensaje);
        cargarFormulario(request, response, cliente);
    }
}
