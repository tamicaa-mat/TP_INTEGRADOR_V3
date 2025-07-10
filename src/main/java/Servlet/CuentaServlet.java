package Servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Negocio.CuentaNegocio;
import NegocioImpl.ClienteNegocioImpl;
import NegocioImpl.CuentaNegocioImpl;
import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;

@WebServlet("/CuentaServlet")
public class CuentaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CuentaServlet() {
        super();
    }

  
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 String action = request.getParameter("action");
    	    HttpSession session = request.getSession();

    	    // Acción: Reporte de cuentas creadas entre fechas
    	    if (action != null && action.equals("reporteCuentas")) {
    	        String fechaDesdeStr = request.getParameter("fechaInicio");
    	        String fechaHastaStr = request.getParameter("fechaFin");

    	        try {
    	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	            Date fechaDesde = sdf.parse(fechaDesdeStr);
    	            Date fechaHasta = sdf.parse(fechaHastaStr);

    	            CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
    	            int totalCuentas = cuentaNegocio.contarCuentasCreadasEntreFechas(fechaDesde, fechaHasta);
    	            double saldoTotal = cuentaNegocio.obtenerSaldoTotalCuentasCreadasEntreFechas(fechaDesde, fechaHasta);

    	            request.setAttribute("totalCuentas", totalCuentas);
    	            request.setAttribute("saldoTotalCuentas", saldoTotal);
    	            request.setAttribute("fechaDesde", fechaDesdeStr);
    	            request.setAttribute("fechaHasta", fechaHastaStr);

    	            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorReportes.jsp");
    	            rd.forward(request, response);
    	            return;

    	        } catch (ParseException e) {
    	            request.setAttribute("error", "Formato de fecha inválido");
    	            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorReportes.jsp");
    	            rd.forward(request, response);
    	            return;
    	        }
    	    }

    	    // Acción: Listar cuentas de un cliente por DNI
    	    if (action != null && action.equals("listar")) {
    	        String dniCliente = request.getParameter("dni");

    	        CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
    	        ClienteNegocioImpl clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());

    	        // ✅ Buscamos el cliente por DNI (no desde sesión)
    	        Cliente cliente = clienteNegocio.obtenerClientePorDni(dniCliente);

    	        if (cliente == null) {
    	            request.setAttribute("errorLimiteCuentas", "No se encontró el cliente con DNI: " + dniCliente);
    	            request.getRequestDispatcher("/AdministradorListaCuentas.jsp").forward(request, response);
    	            return;
    	        }

    	        // ✅ Obtenemos las cuentas del cliente
    	        ArrayList<Cuenta> listaCuentas = (ArrayList<Cuenta>) cuentaNegocio.obtenerCuentasPorIdCliente(cliente.getIdCliente(), cliente);

    	        request.setAttribute("listaCuentas", listaCuentas);
    	        request.setAttribute("dniCliente", dniCliente);

    	        RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
    	        rd.forward(request, response);
    	    }
    	     if (action != null && action.equals("eliminar")) {
    	    	
    	    	 
    	        try {
    	            int idCuenta = Integer.parseInt(request.getParameter("idCuenta"));
    	            String dniCliente = request.getParameter("dniCliente");

    	            CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
    	            System.out.println("Entró al action eliminar");
       	    	    System.out.println("ID cuenta a eliminar: " + idCuenta);
       	    	    System.out.println("DNI cliente: " + dniCliente);
       	    	
    	            boolean exito = cuentaNegocio.darDeBajaLogicaCuentas(idCuenta);
    	            System.out.println("Resultado baja lógica: " + exito);

    	            if (exito) {
    	                request.setAttribute("mensajeExitoCuenta", "Cuenta eliminada correctamente.");
    	            } else {
    	                request.setAttribute("errorLimiteCuentas", "No se pudo eliminar la cuenta.");
    	            }

    	            ClienteNegocioImpl clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());
    	            Cliente cliente = clienteNegocio.obtenerClientePorDni(dniCliente);
    	            ArrayList<Cuenta> listaCuentas = (ArrayList<Cuenta>) cuentaNegocio.obtenerCuentasPorIdCliente(cliente.getIdCliente(), cliente);

    	            request.setAttribute("listaCuentas", listaCuentas);
    	            request.setAttribute("dniCliente", dniCliente);

    	            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
    	            rd.forward(request, response);
    	            return;

    	        } catch (Exception e) {
    	            e.printStackTrace(); // Mostralo en consola
    	            response.getWriter().write("Error al eliminar la cuenta: " + e.getMessage());
    	            return;
    	        }
    	        
    	    }
    	    
    	    
    	    
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("action");

        CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
        ClienteNegocioImpl clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());

        if (action != null && action.equals("agregar")) {
            String dniCliente = request.getParameter("dniCliente");
            int idTipoCuenta = Integer.parseInt(request.getParameter("idTipoCuenta"));

            // ✅ Buscar al cliente por DNI ya que sos admin
            Cliente cliente = clienteNegocio.obtenerClientePorDni(dniCliente);

            if (cliente == null) {
                request.setAttribute("errorLimiteCuentas", "No se encontró el cliente con DNI: " + dniCliente);
                request.getRequestDispatcher("/AdministradorListaCuentas.jsp").forward(request, response);
                return;
            }

            // ✅ Generar número de cuenta
            String numeroCuenta = cuentaNegocio.generarNumeroCuenta(cliente.getDni());
            String cbu = cuentaNegocio.generarNumeroCbu(numeroCuenta);
            
            Cuenta nuevaCuenta = new Cuenta();
            nuevaCuenta.setIdCliente(cliente.getIdCliente());
            nuevaCuenta.setNumeroCuenta(numeroCuenta);
            nuevaCuenta.setCbu(cbu);
            
            
            nuevaCuenta.setFechaCreacion(LocalDate.now());
            nuevaCuenta.setTipoCuenta(idTipoCuenta);
            nuevaCuenta.setSaldo(10000.0);
            nuevaCuenta.setEstado(true);

            boolean seAgrego = cuentaNegocio.agregarCuenta(nuevaCuenta, cliente);
            if (!seAgrego) {
                request.setAttribute("errorLimiteCuentas", "⚠️ El cliente ya tiene 3 cuentas activas.");
            } else {
                request.setAttribute("mensajeExitoCuenta", "✅ Cuenta creada con éxito. Número: " + numeroCuenta + " | Saldo inicial: $10,000.00");
            
            ArrayList<Cuenta> listaCuentas = cuentaNegocio.getCuentasPorCliente(dniCliente, cliente);
            request.setAttribute("listaCuentas", listaCuentas);
            request.setAttribute("dniCliente", dniCliente);

            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
            rd.forward(request, response);
        }
       
    }
    
        
    }
    
    
}
    
