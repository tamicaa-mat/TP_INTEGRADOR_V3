package Servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import Negocio.PrestamoNegocio;
import dao.PrestamoDao;

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
                
            case "reportePrestamos":
            	generarReportePrestamos(request, response);
            	break;
            	
            default:
                response.sendRedirect("AdministradorReportes.jsp");
                break;
        }
    }

 // MÉTODO AUXILIAR PARA EL REPORTE DE PRÉSTAMOS 
    private void generarReportePrestamos(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl();
        
        String desdeStr = request.getParameter("fechaInicioPrestamo");
        String hastaStr = request.getParameter("fechaFinPrestamo");

        try {
        	
        	// validación para evitar ParseException con cadenas vacías
            if (desdeStr == null || desdeStr.isEmpty() || hastaStr == null || hastaStr.isEmpty()) {
                throw new ParseException("Fechas requeridas.", 0);
            }
        	
        	
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date desde = sdf.parse(desdeStr);
            Date hasta = sdf.parse(hastaStr);

            // Métricas que ya tenías:
            double totalPrestamos = prestamoNegocio.obtenerSumaImporteEntreFechas(desde, hasta);
            int cantidadPrestamos = prestamoNegocio.contarPrestamosEntreFechas(desde, hasta);
            
            // 🚀 NUEVAS MÉTRICAS ENRIQUECIDAS (ASUMIENDO QUE YA LAS CREASTE EN EL NEGOCIO):
            int prestamosMorosos = prestamoNegocio.contarPrestamosMorosos();
            double capitalPendiente = prestamoNegocio.obtenerCapitalPendienteDeCobro(); 
            // --------------------------------------------------------------------------

            // Carga de atributos para el JSP
            request.setAttribute("totalPrestamos", totalPrestamos);
            request.setAttribute("cantidadPrestamos", cantidadPrestamos);
            request.setAttribute("fechaInicioPrestamo", desdeStr);
            request.setAttribute("fechaFinPrestamo", hastaStr);
            
            // Carga de métricas enriquecidas
            request.setAttribute("prestamosMorosos", prestamosMorosos);
            request.setAttribute("capitalPendiente", capitalPendiente);
            
            request.setAttribute("activeTab", "prestamos");

            request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
        
        } catch (ParseException e) {
            request.setAttribute("error", "Formato de fecha inválido.");
            request.setAttribute("activeTab", "prestamos");
            request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Hubo un error al generar el reporte.");
            request.setAttribute("activeTab", "prestamos");
            request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
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