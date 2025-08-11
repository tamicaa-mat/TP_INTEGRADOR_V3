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
               // generarReporteTopClientes(request, response);
                break;
           
            case "reporteMorosidad":
                generarReporteMorosidad(request, response);
                
                break;
            default:
                response.sendRedirect("AdministradorReportes.jsp");
                break;
        }
    }

   /* private void generarReporteTopClientes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
        
        // El '10' es por "Top 10", puedes hacerlo dinámico si quieres
        List<Cliente> topClientes = clienteNegocio.obtenerTopClientesPorSaldo(10); 
        
        request.setAttribute("listaTopClientes", topClientes);
        
        RequestDispatcher rd = request.getRequestDispatcher("/AdministradorReportes.jsp");
        rd.forward(request, response);
    }
*/
    

private void generarReporteMorosidad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 1. Instancias la capa de negocio
    PrestamoNegocioImpl prestamoNegocio = new PrestamoNegocioImpl();
    
    // 2. Llamas al nuevo método de negocio
    List<Cuota> cuotasVencidas = prestamoNegocio.obtenerCuotasVencidas();
    
    // 3. Pasas la lista resultante al JSP
    request.setAttribute("listaMorosos", cuotasVencidas);
    
    // 4. Rediriges a la página para mostrar los datos
    RequestDispatcher rd = request.getRequestDispatcher("/AdministradorReportes.jsp");
    rd.forward(request, response);
}


}