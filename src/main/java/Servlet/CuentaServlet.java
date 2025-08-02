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

@WebServlet("/CuentaServlet")
public class CuentaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CuentaServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		HttpSession session = request.getSession();

		if (action != null && action.equals("reporteCuentas")) {
			String fechaDesdeStr = request.getParameter("fechaInicio");
			String fechaHastaStr = request.getParameter("fechaFin");

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaDesde = sdf.parse(fechaDesdeStr);
				Date fechaHasta = sdf.parse(fechaHastaStr);

				System.out.println("[DEBUG] Fecha desde: " + fechaDesde);
				System.out.println("[DEBUG] Fecha hasta: " + fechaHasta);

				CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
				int totalCuentas = cuentaNegocio.contarCuentasCreadasEntreFechas(fechaDesde, fechaHasta);
				double saldoTotal = cuentaNegocio.obtenerSaldoTotalCuentasCreadasEntreFechas(fechaDesde, fechaHasta);

				System.out.println("[DEBUG] Total cuentas encontradas: " + totalCuentas);
				System.out.println("[DEBUG] Saldo total: " + saldoTotal);

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

		if (action != null && action.equals("listar")) {
			String dniCliente = request.getParameter("dni");

			System.out.println("[DEBUG] Listando cuentas para DNI: " + dniCliente);

			CuentaNegocioImpl cuentaNegocio = new CuentaNegocioImpl(new CuentaDaoImpl());
			ClienteNegocioImpl clienteNegocio = new ClienteNegocioImpl(new ClienteDaoImpl());

			Cliente cliente = clienteNegocio.obtenerClientePorDni(dniCliente);

			if (cliente == null) {
				System.out.println("[ERROR] No se encontró cliente con DNI: " + dniCliente);
				request.setAttribute("errorLimiteCuentas", "No se encontró el cliente con DNI: " + dniCliente);
				request.getRequestDispatcher("/AdministradorListaCuentas.jsp").forward(request, response);
				return;
			}

			System.out.println("[DEBUG] Cliente encontrado: " + cliente.getNombre() + " " + cliente.getApellido());

			ArrayList<Cuenta> listaCuentas = (ArrayList<Cuenta>) cuentaNegocio
					.obtenerCuentasPorIdCliente(cliente.getIdCliente(), cliente);

			System.out.println("[DEBUG] Cuentas obtenidas: " + (listaCuentas != null ? listaCuentas.size() : 0));

			request.setAttribute("listaCuentas", listaCuentas);
			request.setAttribute("dniCliente", dniCliente);

			RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
			rd.forward(request, response);
		} else if ("eliminar".equals(action)) {
			String idCuentaStr = request.getParameter("idCuenta");
			String dni = request.getParameter("dni");

			System.out.println("[DEBUG] Eliminando cuenta - ID: " + idCuentaStr + ", DNI: " + dni);

			if (idCuentaStr != null && dni != null && !dni.trim().isEmpty()) {
				try {
					int idCuenta = Integer.parseInt(idCuentaStr);
					Connection conexion = null;
					boolean eliminada = false;

					try {
						conexion = daoImpl.Conexion.getConexion().getSQLConexion();
						conexion.setAutoCommit(false);

						System.out.println("[DEBUG] Iniciando eliminación de cuenta ID: " + idCuenta);

						PreparedStatement stmtVerificar = conexion.prepareStatement(
								"SELECT IdCuenta, NumeroCuenta, Saldo FROM Cuenta WHERE IdCuenta = ?");
						stmtVerificar.setInt(1, idCuenta);
						java.sql.ResultSet rsVerificar = stmtVerificar.executeQuery();

						if (rsVerificar.next()) {
							System.out.println("[DEBUG] Cuenta encontrada - ID: " + rsVerificar.getInt("IdCuenta")
									+ ", Número: " + rsVerificar.getString("NumeroCuenta") + ", Saldo: "
									+ rsVerificar.getBigDecimal("Saldo"));
						} else {
							System.out.println("[ERROR] No se encontró cuenta con ID: " + idCuenta);
							conexion.rollback();
							request.getSession().setAttribute("mensaje", "Error: La cuenta no existe.");
							response.sendRedirect("CuentaServlet?action=listar&dni=" + dni);
							return;
						}

						PreparedStatement stmtMovimientos = conexion
								.prepareStatement("DELETE FROM Movimiento WHERE IdCuenta = ?");
						stmtMovimientos.setInt(1, idCuenta);
						int movimientosEliminados = stmtMovimientos.executeUpdate();
						System.out.println("[DEBUG] Movimientos eliminados: " + movimientosEliminados);

						PreparedStatement stmtTransferencias = conexion.prepareStatement(
								"DELETE FROM Transferencia WHERE IdCuentaOrigen = ? OR IdCuentaDestino = ?");
						stmtTransferencias.setInt(1, idCuenta);
						stmtTransferencias.setInt(2, idCuenta);
						int transferenciasEliminadas = stmtTransferencias.executeUpdate();
						System.out.println("[DEBUG] Transferencias eliminadas: " + transferenciasEliminadas);

						PreparedStatement stmtVerificarPrestamos = conexion.prepareStatement(
								"SELECT COUNT(*) as total FROM Prestamo WHERE IdCuentaAsociada = ? AND Estado = 1 AND CantidadCuotas > 0");
						stmtVerificarPrestamos.setInt(1, idCuenta);
						java.sql.ResultSet rsPrestamos = stmtVerificarPrestamos.executeQuery();
						rsPrestamos.next();
						int totalPrestamos = rsPrestamos.getInt("total");
						System.out.println(
								"[DEBUG] Préstamos APROBADOS CON CUOTAS PENDIENTES encontrados: " + totalPrestamos);

						if (totalPrestamos > 0) {
							System.out.println(
									"[ERROR] La cuenta tiene préstamos aprobados con cuotas pendientes, no se puede eliminar");
							conexion.rollback();
							request.getSession().setAttribute("mensaje",
									"Error: No se puede eliminar la cuenta porque tiene préstamos activos con cuotas pendientes.");
							response.sendRedirect("CuentaServlet?action=listar&dni=" + dni);
							return;
						}

						PreparedStatement stmtEliminarPrestamosNoActivos = conexion.prepareStatement(
								"UPDATE Prestamo SET IdCuentaAsociada = NULL WHERE IdCuentaAsociada = ? AND (Estado IN (0, 2) OR (Estado = 1 AND CantidadCuotas = 0))");
						stmtEliminarPrestamosNoActivos.setInt(1, idCuenta);
						int prestamosActualizados = stmtEliminarPrestamosNoActivos.executeUpdate();
						System.out.println("[DEBUG] Préstamos no activos desvinculados: " + prestamosActualizados);

						PreparedStatement stmtEliminarCuotas = conexion.prepareStatement(
								"DELETE FROM Cuota WHERE IdPrestamo IN (SELECT IdPrestamo FROM Prestamo WHERE IdCuentaAsociada = ?)");
						stmtEliminarCuotas.setInt(1, idCuenta);
						int cuotasEliminadas = stmtEliminarCuotas.executeUpdate();
						System.out.println("[DEBUG] Cuotas eliminadas: " + cuotasEliminadas);

						PreparedStatement stmtCuenta = conexion
								.prepareStatement("DELETE FROM Cuenta WHERE IdCuenta = ?");
						stmtCuenta.setInt(1, idCuenta);
						int cuentasEliminadas = stmtCuenta.executeUpdate();
						System.out.println("[DEBUG] Cuentas eliminadas: " + cuentasEliminadas);

						if (cuentasEliminadas > 0) {
							conexion.commit();
							eliminada = true;
							System.out.println("[DEBUG] Eliminación física completada con éxito");
						} else {
							conexion.rollback();
							System.out.println("[DEBUG] No se encontró la cuenta para eliminar");
						}

					} catch (SQLException e) {
						System.out.println("[ERROR] Error en eliminación física: " + e.getMessage());
						e.printStackTrace();
						if (conexion != null) {
							try {
								conexion.rollback();
							} catch (SQLException ex) {
								ex.printStackTrace();
							}
						}
					} finally {
						if (conexion != null) {
							try {
								conexion.setAutoCommit(true);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					}

					System.out.println("[DEBUG] Resultado eliminación: " + eliminada);

					if (eliminada) {
						request.getSession().setAttribute("mensaje",
								"Cuenta eliminada permanentemente de la base de datos.");
					} else {
						request.getSession().setAttribute("mensaje", "Error: No se pudo eliminar la cuenta.");
					}
				} catch (NumberFormatException e) {
					request.getSession().setAttribute("mensaje", "Error: ID de cuenta inválido.");
					System.out.println("[ERROR] NumberFormatException: " + e.getMessage());
				}

				response.sendRedirect("CuentaServlet?action=listar&dni=" + dni);
			} else {
				System.out.println("[ERROR] Faltan parámetros - DNI: " + dni + ", ID: " + idCuentaStr);
				request.getSession().setAttribute("mensaje", "Error: Faltan parámetros necesarios.");
				response.sendRedirect("ClienteServlet");
			}
			return;
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
			request.setAttribute("dniCliente", dniCliente);

			RequestDispatcher rd = request.getRequestDispatcher("/AdministradorListaCuentas.jsp");
			rd.forward(request, response);
		}
	}

}
