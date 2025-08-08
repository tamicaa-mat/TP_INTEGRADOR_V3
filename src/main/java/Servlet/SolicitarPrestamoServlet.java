package Servlet;

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
import dominio.Prestamo;

import Negocio.CuentaNegocio;
import Negocio.PrestamoNegocio;

import NegocioImpl.CuentaNegocioImpl;
import NegocioImpl.PrestamoNegocioImpl;


@WebServlet("/SolicitarPrestamoServlet")
public class SolicitarPrestamoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SolicitarPrestamoServlet() {
        super();
    }

   
     //El doGet SIEMPRE se encarga de la carga inicial de la página.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cargarCuentasYMostrarFormulario(request, response);
    }
    
    
    
    
     // El doPost SIEMPRE se encarga de procesar el formulario que se envía.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        if (clienteLogueado == null) {
            request.setAttribute("mensajeError", "Debe iniciar sesión para realizar esta operación.");
            RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            // 1. Obtenemos los datos del formulario.
            int idCuenta = Integer.parseInt(request.getParameter("cuentaSeleccionada"));
            BigDecimal monto = new BigDecimal(request.getParameter("importe"));
            int plazo = Integer.parseInt(request.getParameter("plazo"));

            // 2. Validaciones básicas.
            if (monto.compareTo(BigDecimal.ZERO) <= 0 || plazo <= 0) {
                throw new Exception("El monto y el plazo deben ser mayores a cero.");
            }

            // 3. Creamos el objeto Préstamo.
            Prestamo prestamo = new Prestamo();
            prestamo.setCliente(clienteLogueado);
            
            Cuenta cuentaAsociada = new Cuenta();
            cuentaAsociada.setIdCuenta(idCuenta);
            prestamo.setCuentaAsociada(cuentaAsociada);
            
            prestamo.setImportePedido(monto.doubleValue());
            prestamo.setPlazoMeses(plazo);
            prestamo.setInteres(5.0); // Interés fijo de ejemplo

            // 4. Llamamos a la capa de negocio para guardar la solicitud.
            PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl();
            boolean exito = prestamoNegocio.solicitarPrestamo(prestamo);

            if (exito) {
                request.setAttribute("mensajeExito", "¡Solicitud enviada con éxito!");
            } else {
                request.setAttribute("mensajeError", "Error al registrar la solicitud.");
            }

        } catch (Exception e) {
            request.setAttribute("mensajeError", "Datos inválidos. Verifique los campos. Error: " + e.getMessage());
        }
        
        // al final de procesar la solicitud volvemos a cargar las cuentas , que era el origen del problema
        cargarCuentasYMostrarFormulario(request, response);
    }

    
  //    Método auxiliar para NO REPETIR. Carga las cuentas del cliente
    private void cargarCuentasYMostrarFormulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cliente clienteLogueado = (Cliente) session.getAttribute("clienteLogueado");

        if (clienteLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
        List<Cuenta> listaCuentas = cuentaNegocio.obtenerCuentasPorIdCliente(clienteLogueado.getIdCliente(), clienteLogueado);
        
        request.setAttribute("listaCuentas", listaCuentas);
        
        RequestDispatcher rd = request.getRequestDispatcher("/CLIENTEsolicitarPrestamos.jsp");
        rd.forward(request, response);
    }
}
