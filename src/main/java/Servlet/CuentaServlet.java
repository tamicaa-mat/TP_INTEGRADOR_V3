package Servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import NegocioImpl.CuentaNegocioImpl;
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

         if (action != null && action.equals("listar")) {
             String dniCliente = request.getParameter("dni");
             
             CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
             ArrayList<Cuenta> listaCuentas = cuentaNegocio.getCuentasPorCliente(dniCliente);
             
             request.setAttribute("listaCuentas", listaCuentas);
             request.setAttribute("dniCliente", dniCliente); 
             
             RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
             rd.forward(request, response);
         }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("action");
    	CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());

    	if (action != null && action.equals("agregar")) {
    	    String dniCliente = request.getParameter("dniCliente");
    	    int idTipoCuenta = Integer.parseInt(request.getParameter("idTipoCuenta"));

    	    Cuenta nuevaCuenta = new Cuenta();
    	    nuevaCuenta.setTipoCuenta(idTipoCuenta); 

    	    Cliente cliente = new Cliente();
    	    cliente.setDni(dniCliente); 
    	    nuevaCuenta.setCliente(cliente);

    	    nuevaCuenta.setSaldo(0.0);
    	    nuevaCuenta.setFechaCreacion(LocalDate.now());
    	    nuevaCuenta.setEstado(true);
    	  
    	    
    	    
    	    boolean seAgrego = cuentaNegocio.agregarCuenta(nuevaCuenta);

    	    if (!seAgrego) {
    	        request.setAttribute("errorLimiteCuentas", "El cliente ya alcanzó el límite de 3 cuentas activas.");
    	    }

    	    ArrayList<Cuenta> listaCuentas = cuentaNegocio.getCuentasPorCliente(dniCliente);
    	    request.setAttribute("listaCuentas", listaCuentas);
    	    request.setAttribute("dniCliente", dniCliente);

    	    RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
    	    rd.forward(request, response);
    	}

       
    }
}
    
