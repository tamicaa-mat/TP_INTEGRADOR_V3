package Servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Negocio.ClienteNegocio;
import NegocioImpl.ClienteNegocioImpl;
import NegocioImpl.PrestamoNegocioImpl;
import dominio.Cliente;
import dominio.Cuota;

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReporteServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("AdministradorReportes.jsp");
            return;
        }

        switch (action) {
            case "topClientes":
            	generarReporteTopClientes(request, response);
                break;
           
            case "reporteMorosidad":
                generarReporteMorosidad(request, response);
                
                break;
            default:
                response.sendRedirect("AdministradorReportes.jsp");
                break;
        }
    }

    private void generarReporteTopClientes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl(); 
        
        // El 10 es por "Top 10".
        List<Cliente> topClientes = clienteNegocio.obtenerTopClientesPorSaldo(10); 
        
        request.setAttribute("listaTopClientes", topClientes);
        request.setAttribute("activeTab", "vip");
        
        RequestDispatcher rd = request.getRequestDispatcher("/AdministradorReportes.jsp");
        rd.forward(request, response);
    }
    

private void generarReporteMorosidad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
    PrestamoNegocioImpl prestamoNegocio = new PrestamoNegocioImpl();
    List<Cuota> cuotasVencidas = prestamoNegocio.obtenerCuotasVencidas();
    
    
    request.setAttribute("listaMorosos", cuotasVencidas);
    request.setAttribute("activeTab", "morosidad");

    
    
    RequestDispatcher rd = request.getRequestDispatcher("/AdministradorReportes.jsp");
    rd.forward(request, response);
}


}