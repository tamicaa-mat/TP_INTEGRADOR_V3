package Servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Negocio.ClienteNegocio;

import Negocio.MovimientoNegocio;
import Negocio.CuentaNegocio;
import NegocioImpl.ClienteNegocioImpl;
import NegocioImpl.CuentaNegocioImpl;
import NegocioImpl.MovimientoNegocioImpl;
import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.MovimientoDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Movimiento;
import dominio.TipoCuenta;
import dominio.TipoMovimiento;
import excepciones.OperacionInvalidaException;

@WebServlet("/CuentaServlet")
public class CuentaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CuentaServlet() {
		super();
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String action = request.getParameter("action");
	    if (action == null) {
	        action = ""; // Asignamos un valor por defecto para evitar errores
	    }

	    // Instanciamos las capas de negocio que vamos a necesitar
	    CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
	    ClienteNegocio clienteNegocio = new ClienteNegocioImpl();

	    switch (action) {
	        case "reporteCuentas": {
	            String fechaDesdeStr = request.getParameter("fechaInicio");
	            String fechaHastaStr = request.getParameter("fechaFin");
	            MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl(); 
	            // ⬅️ SOLUCIÓN: Declarar las variables aquí, fuera del bloque try
	            java.util.Date fechaDesde = null; 
	            java.util.Date fechaHasta = null;

	            try {
	            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                fechaDesde = sdf.parse(fechaDesdeStr); // Asignación dentro del try
	                fechaHasta = sdf.parse(fechaHastaStr); // Asignación dentro del try
	                // 1. Crecimiento (Métrica que ya tienes)
	                int totalCuentasNuevas = cuentaNegocio.contarCuentasCreadasEntreFechas(fechaDesde, fechaHasta);
	                
	                // 2. Flujo Neto (NUEVA Métrica Estadística)
	                double flujoNeto = movimientoNegocio.obtenerFlujoNetoDeCapital(fechaDesde, fechaHasta); 
	                
	                // 3. Saldo Total del Banco (Métrica de corte)
	                double saldoTotalActual = movimientoNegocio.obtenerSaldoTotalBanco(); 

	                request.setAttribute("totalCuentasNuevas", totalCuentasNuevas);
	                request.setAttribute("flujoNeto", flujoNeto);
	                request.setAttribute("saldoTotalActual", saldoTotalActual);
	                
	                request.setAttribute("fechaDesde", fechaDesdeStr);
	                request.setAttribute("fechaHasta", fechaHastaStr);
	                request.setAttribute("activeTab", "cuentas");
	                
	                RequestDispatcher rd = request.getRequestDispatcher("/AdministradorReportes.jsp");
	                rd.forward(request, response);
	            } catch (ParseException e) {
	                request.setAttribute("error", "Formato de fecha inválido");
	                RequestDispatcher rd = request.getRequestDispatcher("/AdministradorReportes.jsp");
	                rd.forward(request, response);
	            }
	            break; 
	        }

	        case "listar": {
	            String dniCliente = request.getParameter("dni");
	            Cliente cliente = clienteNegocio.obtenerClientePorDni(dniCliente);

	            if (cliente == null) {
	                request.setAttribute("error", "No se encontró el cliente con DNI: " + dniCliente);
	                RequestDispatcher rd = request.getRequestDispatcher("/ClienteServlet");
	                rd.forward(request, response);
	                return; 
	            }

	            
	            ArrayList<Cuenta> listaCuentas = cuentaNegocio.getCuentasPorCliente(cliente);

	            request.setAttribute("listaCuentas", listaCuentas);
	            request.setAttribute("cliente", cliente); // Pasamos el objeto cliente completo al JSP

	            RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
	            rd.forward(request, response);
	            break;
	        }

	        case "eliminar": {
	        	String dniCliente = request.getParameter("dni"); // Necesitamos el DNI para saber a dónde volver
	            try {
	                int idCuenta = Integer.parseInt(request.getParameter("idCuenta"));
	                
	              
	                boolean exito = cuentaNegocio.darDeBajaLogicaCuentas(idCuenta);

	                if (exito) {
	                    request.getSession().setAttribute("mensaje", "Cuenta desactivada correctamente.");
	                } else {
	                    // UPDATE en la BD por si  falla por otra razón.
	                    request.getSession().setAttribute("mensaje", "Error: No se pudo desactivar la cuenta.");
	                }

	            } catch (OperacionInvalidaException e) { 
	                request.getSession().setAttribute("mensaje", e.getMessage()); 
	            } catch (NumberFormatException e) {
	                request.getSession().setAttribute("mensaje", "Error: ID de cuenta inválido.");
	            }
	            
	            // Al final, siempre redirigimos de vuelta a la lista de cuentas de ese cliente.
	            response.sendRedirect("CuentaServlet?action=listar&dni=" + dniCliente);
	            break;
	        	
	        }
	        
	        default: {
	            // Si no hay una acción específica, redirigimos a la página principal de gestión de clientes
	            response.sendRedirect("ClienteServlet");
	            break;
	        }
	    }
	}

	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
		ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());

		if ("agregar".equals(action)) {
			String dniCliente = request.getParameter("dniCliente");
			int idTipoCuenta = Integer.parseInt(request.getParameter("idTipoCuenta"));

			Cliente cliente = clienteNegocio.obtenerClientePorDni(dniCliente);

			if (cliente == null) {
				request.setAttribute("errorLimiteCuentas", "No se encontró el cliente con DNI: " + dniCliente);
				request.getRequestDispatcher("/AdministradorListaCuentas.jsp").forward(request, response);
				return;
			}

			String numeroCuenta = cuentaNegocio.generarNumeroCuenta(cliente.getDni());
			String cbu = cuentaNegocio.generarNumeroCbu(numeroCuenta);

			Cuenta nuevaCuenta = new Cuenta();

			nuevaCuenta.setCliente(cliente);

			TipoCuenta tipoCuenta = new TipoCuenta();
			tipoCuenta.setIdTipoCuenta(idTipoCuenta);
			nuevaCuenta.setTipoCuentaObjeto(tipoCuenta);

			nuevaCuenta.setNumeroCuenta(numeroCuenta);
			nuevaCuenta.setCbu(cbu);
			nuevaCuenta.setFechaCreacion(LocalDate.now());
			nuevaCuenta.setSaldo(new BigDecimal("10000.0"));
			nuevaCuenta.setEstado(true);

			boolean seAgrego = cuentaNegocio.agregarCuenta(nuevaCuenta, cliente);

			if (!seAgrego) {
				request.setAttribute("errorLimiteCuentas", "⚠️ El cliente ya tiene 3 cuentas activas.");
			} else {
				request.setAttribute("mensajeExitoCuenta", "✅ Cuenta creada con éxito. Número: " + numeroCuenta);

				int idCuentaNueva = cuentaNegocio.obtenerIdCuentaPorNumero(numeroCuenta);

				Cuenta cuentaDelMovimiento = new Cuenta();
				cuentaDelMovimiento.setIdCuenta(idCuentaNueva);

				TipoMovimiento tipoMovimientoAlta = new TipoMovimiento();
				tipoMovimientoAlta.setIdTipoMovimiento(1); 
				Movimiento movimientoInicial = new Movimiento();
				movimientoInicial.setFechaHora(LocalDateTime.now());
				movimientoInicial.setReferencia("Apertura de cuenta");
				movimientoInicial.setImporte(new BigDecimal("10000.0"));
				movimientoInicial.setCuenta(cuentaDelMovimiento);
				movimientoInicial.setTipoMovimiento(tipoMovimientoAlta);

				MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl(new MovimientoDaoImpl());
				movimientoNegocio.crearMovimiento(movimientoInicial);
			}

			ArrayList<Cuenta> listaCuentas = (ArrayList<Cuenta>) cuentaNegocio
					.obtenerCuentasPorIdCliente(cliente.getIdCliente(), cliente);
			request.setAttribute("listaCuentas", listaCuentas);
			request.setAttribute("cliente", cliente);

			RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
			rd.forward(request, response);
		}
	}

}
