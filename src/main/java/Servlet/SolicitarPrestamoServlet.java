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

import Negocio.CuentaNegocio;
import NegocioImpl.CuentaNegocioImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;


@WebServlet("/SolicitarPrestamoServlet")
public class SolicitarPrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public SolicitarPrestamoServlet() {
        super();
    
    }

    
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      	  System.out.println("Servlet ejecutado");
      	  HttpSession session = request.getSession();
          Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");  
          int IdCl = cliente.getIdCliente();
          
          
          if (cliente != null) {
          	System.out.println("Cliente logueado: " + IdCl);
              CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
              List<Cuenta> cuentas = cuentaNegocio.ObtenerCuentasPorIdCliente(IdCl, cliente);

              
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
          }

          RequestDispatcher rd = request.getRequestDispatcher("/CLIENTEsolicitarPrestamos.jsp");
          rd.forward(request, response);
      }

  		
  	
  	
  	
  	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  	
  	
  		
  	}
  	
	
	}


