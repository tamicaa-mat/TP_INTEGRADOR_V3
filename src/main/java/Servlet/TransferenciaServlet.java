package Servlet; // 

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Negocio.CuentaNegocio;
import Negocio.TransferenciaNegocio;
import NegocioImpl.CuentaNegocioImpl;
import NegocioImpl.TransferenciaNegocioImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.TransferenciaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Transferencia;
import excepciones.TransferenciaInvalidaException;

@WebServlet("/TransferenciaServlet")
public class TransferenciaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TransferenciaServlet() {
        super();
    }

    //  GET: Carga el formulario
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("GET TransferenciaServlet ejecutado");

        HttpSession session = request.getSession();
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        if (clienteLogueado == null) {
            response.sendRedirect("login.jsp?mensaje=Debe iniciar sesión");
            return;
        }

        CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
        List<Cuenta> cuentas = cuentaNegocio.getCuentasPorCliente(clienteLogueado);
        request.setAttribute("cuentas", cuentas);

        request.getRequestDispatcher("ClienteNuevaTransferencia.jsp").forward(request, response);
    }

    //  POST: Procesa la transferencia
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("POST TransferenciaServlet ejecutado");

        HttpSession session = request.getSession();
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        if (clienteLogueado == null) {
            response.sendRedirect("login.jsp?mensaje=Debe iniciar sesión");
            return;
        }

        CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
        List<Cuenta> cuentas = cuentaNegocio.getCuentasPorCliente(clienteLogueado);
        request.setAttribute("cuentas", cuentas);

        // Leer parámetros
        String numeroCuentaOrigenStr = request.getParameter("idCuentaOrigen");
        String numeroCuentaDestino = request.getParameter("numeroCuentaDestino");
        String montoStr = request.getParameter("monto");

        if (numeroCuentaOrigenStr != null && montoStr != null && numeroCuentaDestino != null &&
            !numeroCuentaOrigenStr.isEmpty() && !montoStr.isEmpty() && !numeroCuentaDestino.isEmpty()) {
            
            try {
               
                BigDecimal monto = new BigDecimal(montoStr);

                TransferenciaNegocio transferenciaNeg = new TransferenciaNegocioImpl(new TransferenciaDaoImpl());

                // Ejecutar transferencia
                transferenciaNeg.realizarTransferencia(numeroCuentaOrigenStr, numeroCuentaDestino, monto);
                request.setAttribute("mensajeExito", "Transferencia realizada correctamente.");

                //request.setAttribute("numeroCuentaDestino",numeroCuentaDestino); no tengo en transferencia numero cuenta, tengo solo el id
                //request.setAttribute("numeroCuentaOrigen",numeroCuentaOrigenStr);
                
                
                
                
                // Mostrar historial
                List<Transferencia> transferencias = transferenciaNeg.listarTransferenciasPorCuenta(numeroCuentaOrigenStr);
                request.setAttribute("transferencias", transferencias);

            } catch (TransferenciaInvalidaException e) {
                request.setAttribute("mensajeError", e.getMessage());
            } catch (NumberFormatException e) {
                request.setAttribute("mensajeError", "Formato de número inválido.");
            }
        } else {
            request.setAttribute("mensajeError", "Todos los campos son obligatorios.");
        }

        // Volver al JSP
        request.getRequestDispatcher("ClienteNuevaTransferencia.jsp").forward(request, response);
    }
}
