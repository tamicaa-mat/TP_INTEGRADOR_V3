package NegocioImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import Negocio.PrestamoNegocio;
import dao.PrestamoDao;
import daoImpl.PrestamoDaoImpl;
import dominio.Cuenta;
import dominio.Cuota;
import dominio.Movimiento;
import dominio.Prestamo;
import dominio.TipoMovimiento;
import dominio.Usuario;
import Negocio.CuentaNegocio;
import Negocio.MovimientoNegocio;
import dao.CuotaDao;
import daoImpl.CuotaDaoImpl;

public class PrestamoNegocioImpl implements PrestamoNegocio {

	//private PrestamoDao prestamoDao;
    private PrestamoDao prestamoDao = new PrestamoDaoImpl();
    
    
    private CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
    private MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl();
    private CuotaDao cuotaDao = new CuotaDaoImpl();
    
    
    @Override
    public boolean aprobarPrestamo(int idPrestamo) {
        // 1. OBTENER EL PRÉSTAMO
    	
        System.out.println("NEGOCIO: Ingresando a aprobarPrestamo para ID: " + idPrestamo);

    	
        Prestamo prestamo = this.obtenerPrestamoPorId(idPrestamo);
        if (prestamo == null) {
            System.err.println("ERROR: No se encontró el préstamo con ID " + idPrestamo);
            return false;
        }

        // 2. ACTUALIZAR EL ESTADO DEL PRÉSTAMO A "APROBADO" (Estado = 1)
        boolean estadoActualizado = this.actualizarEstadoPrestamo(idPrestamo, 1);
        if (!estadoActualizado) {
            System.err.println("ERROR: No se pudo actualizar el estado del préstamo.");
            return false;
        }

        // 3. ACREDITAR DINERO EN LA CUENTA DEL CLIENTE (Lógica que estaba en el Servlet)
        // 3.1. Crear el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setFechaHora(LocalDateTime.now());
        movimiento.setReferencia("ALTA PRESTAMO ID: " + idPrestamo);
        movimiento.setImporte(BigDecimal.valueOf(prestamo.getImportePedido()));
        TipoMovimiento tipo = new TipoMovimiento();
        tipo.setIdTipoMovimiento(2); // ID para "Alta Préstamo"
        movimiento.setTipoMovimiento(tipo);
        movimiento.setCuenta(prestamo.getCuentaAsociada());
        
        boolean movimientoCreado = movimientoNegocio.crearMovimiento(movimiento);
        if (!movimientoCreado) {
            System.err.println("ERROR: No se pudo crear el movimiento del préstamo.");
            // Opcional: Podrías hacer un rollback del estado del préstamo aquí.
            return false;
        }
        
        // 3.2. Actualizar el saldo de la cuenta
        Cuenta cuenta = prestamo.getCuentaAsociada();
        BigDecimal nuevoSaldo = cuenta.getSaldo().add(movimiento.getImporte());
        boolean saldoActualizado = cuentaNegocio.actualizarSaldo(cuenta.getIdCuenta(), nuevoSaldo.doubleValue());
        if(!saldoActualizado) {
            System.err.println("ERROR: No se pudo actualizar el saldo de la cuenta.");
            return false;
        }

        
        
        System.out.println("NEGOCIO: Estado del préstamo y saldo actualizados. Preparando para generar cuotas.");
        System.out.println("NEGOCIO: El préstamo tiene " + prestamo.getCantidadCuotas() + " cuotas para generar.");

        // 4. *** GENERAR LAS CUOTAS (La nueva lógica) ***
        for (int i = 1; i <= prestamo.getCantidadCuotas(); i++) {
            Cuota nuevaCuota = new Cuota();

            // ▼▼▼ ESTA ES LA LÍNEA CLAVE ▼▼▼
            // Le pasamos el objeto 'prestamo' (la variable) que obtuvimos al inicio del método.
            nuevaCuota.setPrestamo(prestamo); 

            nuevaCuota.setNumeroCuota(i);
            nuevaCuota.setMonto(BigDecimal.valueOf(prestamo.getImportePorMes()));
            nuevaCuota.setEstado(false); // false = No pagada

            boolean cuotaAgregada = cuotaDao.agregar(nuevaCuota);
            if (!cuotaAgregada) {
                System.err.println("ERROR: Falla al crear la cuota N°" + i + " para el préstamo " + idPrestamo);
                return false;
            }
        }
        
        System.out.println("Préstamo " + idPrestamo + " aprobado y cuotas generadas exitosamente.");
        return true; // Si todo salió bien
    }
    
    
    
    
    
    

    @Override
    public List<Cuota> obtenerCuotasVencidas() {
        return prestamoDao.obtenerCuotasVencidas();
    }
	
	
	@Override
    public boolean clienteTienePrestamosActivos(int idCliente) {
        return prestamoDao.tienePrestamosActivos(idCliente);
    }
	

	public PrestamoNegocioImpl(PrestamoDao prestamoDao) {
		this.prestamoDao = prestamoDao;
	}

	public PrestamoNegocioImpl() {
		
	}


	public boolean solicitarPrestamo(Usuario usuario, Prestamo prestamo) {
		if (usuario != null && prestamo != null) {
			return prestamoDao.insert(prestamo);
		}
		return false;
	}

	public boolean eliminarPrestamo(int idPrestamo) {
		return false;
	}

	public boolean pagarPrestamo(int idPrestamo, double montoPago) {
		Prestamo prestamo = prestamoDao.getPrestamoPorIdPrestamo(idPrestamo);
		if (prestamo != null && montoPago > 0) {
			double nuevoSaldo = prestamo.getImportePedido() - montoPago;

			if (nuevoSaldo >= 0) {
				return prestamoDao.actualizarImportePedido(idPrestamo, nuevoSaldo);
			}
		}
		return false;

	}

	public List<Prestamo> listarPrestamos() {
		return prestamoDao.obtenerTodosLosPrestamos();
	}

	public boolean actualizarEstadoPrestamo(int idPrestamo, int nuevoEstado) {
		return prestamoDao.actualizarEstado(idPrestamo, nuevoEstado);
	}

	public boolean solicitarPrestamo(Prestamo prestamo) {

		if (prestamo.getInteres() == 0) {
			prestamo.setInteres(5.0);
		}

		if (prestamo.getImportePorMes() == 0 && prestamo.getPlazoMeses() > 0) {
			double importePedido = prestamo.getImportePedido();
			double interes = prestamo.getInteres();
			int plazo = prestamo.getPlazoMeses();

			double importeTotalConInteres = importePedido * (1 + interes / 100.0);
			double cuotaMensual = importeTotalConInteres / plazo;
			prestamo.setImportePorMes(cuotaMensual);
		}

		if (prestamo.getCantidadCuotas() == 0) {
			prestamo.setCantidadCuotas(prestamo.getPlazoMeses());
		}

		prestamo.setEstado(1);
		return prestamoDao.insert(prestamo);

	}

	@Override
	public double obtenerSumaImporteEntreFechas(Date desde, Date hasta) {
		return prestamoDao.obtenerSumaImporteEntreFechas(desde, hasta);
	}

	@Override
	public int contarPrestamosEntreFechas(Date desde, Date hasta) {
		return prestamoDao.contarPrestamosEntreFechas(desde, hasta);
	}

	public List<Prestamo> obtenerPrestamosActivosPorCuenta(int idCuenta) {

		return prestamoDao.getPrestamoPorIdCuenta(idCuenta);

	}

	@Override
	public Prestamo obtenerPrestamoPorId(int idPrestamo) {

		return prestamoDao.getPrestamoPorIdPrestamo(idPrestamo);
	}

	public boolean pagarCuota(int idCuenta, int idPrestamo, double monto) {
		return prestamoDao.pagarCuotaConTransaccion(idCuenta, idPrestamo, monto);
	}
	
	
	
	
	

}
