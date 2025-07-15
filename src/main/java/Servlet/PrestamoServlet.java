package Servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Negocio.MovimientoNegocio;
import Negocio.PrestamoNegocio;
import Negocio.CuentaNegocio;
import NegocioImpl.MovimientoNegocioImpl;
import NegocioImpl.PrestamoNegocioImpl;
import NegocioImpl.CuentaNegocioImpl;
import daoImpl.MovimientoDaoImpl;
import daoImpl.PrestamoDaoImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cuenta;
import dominio.Movimiento;
import dominio.Prestamo;
import dominio.TipoMovimiento;


@WebServlet("/PrestamoServlet")
public class PrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public PrestamoServlet() {
        super();
     
    }

    private MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl(new MovimientoDaoImpl());
    private PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl(new PrestamoDaoImpl());
    private CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null && action.equals("reportePrestamos")) {
            String desdeStr = request.getParameter("fechaInicioPrestamo");
            String hastaStr = request.getParameter("fechaFinPrestamo");

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date desde = sdf.parse(desdeStr);
                Date hasta = sdf.parse(hastaStr);

                // Obtener reporte de préstamos
                double totalPrestamos = prestamoNegocio.obtenerSumaImporteEntreFechas(desde, hasta);
                int cantidadPrestamos = prestamoNegocio.contarPrestamosEntreFechas(desde, hasta);

                request.setAttribute("totalPrestamos", totalPrestamos);
                request.setAttribute("cantidadPrestamos", cantidadPrestamos);
                request.setAttribute("fechaInicioPrestamo", desdeStr);
                request.setAttribute("fechaFinPrestamo", hastaStr);

                // Mostrar el reporte en la página JSP
                request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
                return;

            } catch (ParseException e) {
                request.setAttribute("error", "Formato de fecha inválido. Use el formato YYYY-MM-DD");
                request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                request.setAttribute("error", "Hubo un error al generar el reporte. Por favor intente nuevamente.");
                request.getRequestDispatcher("AdministradorReportes.jsp").forward(request, response);
                return;
            }
        }

        // Listar todos los préstamos
        List<Prestamo> prestamos = prestamoNegocio.listarPrestamos();
        request.setAttribute("prestamos", prestamos);
        request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("idPrestamo");
        String accion = request.getParameter("accion");

        System.out.println("doPost id=" + id + ", accion=" + accion);

        if (id != null && accion != null) {
            try {
                int idPrestamo = Integer.parseInt(id);
                int nuevoEstado = accion.equals("aprobar") ? 1 : 2;

                prestamoNegocio.actualizarEstadoPrestamo(idPrestamo, nuevoEstado);

                if (nuevoEstado == 1) {
                    Prestamo prestamo = prestamoNegocio.obtenerPrestamoPorId(idPrestamo);

                    if (prestamo == null) {
                        System.err.println("ERROR: No se encontró el préstamo con ID " + idPrestamo);
                        request.setAttribute("error", "No se encontró el préstamo con ID: " + idPrestamo);
                        request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
                        return;
                    }

                    if (prestamo.getCuentaAsociada() == null) {
                        System.err.println("ERROR: El préstamo no tiene cuenta asociada.");
                        request.setAttribute("error", "El préstamo no tiene cuenta asociada.");
                        request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
                        return;
                    }

                    Movimiento movimiento = new Movimiento();
                    movimiento.setFechaHora(LocalDateTime.now());
                    movimiento.setReferencia("ALTA PRESTAMO ID: " + idPrestamo);
                    movimiento.setImporte(BigDecimal.valueOf(prestamo.getImportePedido()));

                    TipoMovimiento tipo = new TipoMovimiento();
                    tipo.setIdTipoMovimiento(2);
                    movimiento.setTipoMovimiento(tipo);

                    movimiento.setCuenta(prestamo.getCuentaAsociada());

                    System.out.println("Datos del movimiento a insertar:");
                    System.out.println("FechaHora: " + movimiento.getFechaHora());
                    System.out.println("Referencia: " + movimiento.getReferencia());
                    System.out.println("Importe: " + movimiento.getImporte());
                    System.out.println("IdTipoMovimiento: " + movimiento.getTipoMovimiento().getIdTipoMovimiento());
                    System.out.println("IdCuenta: " + movimiento.getCuenta().getIdCuenta());

                    boolean resultado = movimientoNegocio.crearMovimiento(movimiento);	

                    if (resultado) {
                        System.out.println("Resultado inserción movimiento: " + resultado);

                        Cuenta cuenta = prestamo.getCuentaAsociada();
                        BigDecimal nuevoSaldo = cuenta.getSaldo().add(movimiento.getImporte());

                        boolean saldoActualizado = cuentaNegocio.actualizarSaldo(cuenta.getIdCuenta(), nuevoSaldo.doubleValue());

                        if (!saldoActualizado) {
                            request.setAttribute("error", "Movimiento creado, pero no se pudo actualizar el saldo.");
                            request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("error", "Hubo un problema al crear el movimiento.");
                        request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID de préstamo inválido.");
                request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                request.setAttribute("error", "Hubo un error al procesar la solicitud. Por favor intente nuevamente.");
                request.getRequestDispatcher("AdministradorListaPrestamos.jsp").forward(request, response);
                return;
            }
        }

        response.sendRedirect("PrestamoServlet");
    }


	
}

