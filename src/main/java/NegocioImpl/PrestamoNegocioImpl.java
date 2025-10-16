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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date; 
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PrestamoNegocioImpl implements PrestamoNegocio {

	//private PrestamoDao prestamoDao;
    private PrestamoDao prestamoDao = new PrestamoDaoImpl();
    
    
    private CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
    private MovimientoNegocio movimientoNegocio = new MovimientoNegocioImpl();
    private CuotaDao cuotaDao = new CuotaDaoImpl();
    
    
    @Override
    public int contarPrestamosMorosos() {
    	 // La capa de negocio llama al DAO para ejecutar la consulta SQL compleja.
        System.out.println("NEGOCIO: Solicitando conteo de préstamos morosos al DAO.");
        return prestamoDao.contarPrestamosMorosos();
    	
    }
    

    public double obtenerCapitalPendienteDeCobro() {
    	  // La capa de negocio llama al DAO para obtener la suma de los saldos restantes.
        System.out.println("NEGOCIO: Solicitando cálculo de capital pendiente al DAO.");
        return prestamoDao.obtenerCapitalPendienteDeCobro();
    }
    
    
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

        // 3. ACREDITAR DINERO Y ACTUALIZAR SALDO 
        
        // 3.1. Crear el movimiento (Acreditación)
        Movimiento movimiento = new Movimiento();
        movimiento.setFechaHora(LocalDateTime.now());
        movimiento.setReferencia("ALTA PRESTAMO ID: " + idPrestamo);
        movimiento.setImporte(BigDecimal.valueOf(prestamo.getImportePedido()));
        TipoMovimiento tipo = new TipoMovimiento();
        tipo.setIdTipoMovimiento(2); // ID para "Alta Préstamo"
        movimiento.setTipoMovimiento(tipo);
        movimiento.setCuenta(prestamo.getCuentaAsociada());
        
        boolean movimientoCreado = movimientoNegocio.crearMovimiento(movimiento); // ⬅️ Vuelve a llamar a tu MovimientoNegocio
        if (!movimientoCreado) {
            System.err.println("ERROR: No se pudo crear el movimiento del préstamo (ACREDITACIÓN).");
            // Aquí deberías revertir el estado del préstamo (volver a Pendiente: 0)
            return false;
        }
        
        // 3.2. Actualizar el saldo de la cuenta
        Cuenta cuenta = prestamo.getCuentaAsociada();
     // 🚨 CORRECCIÓN CLAVE: Usar el ID de la cuenta para obtener el saldo actual de la BD 🚨
        // Usamos cuenta.getIdCuenta() en lugar de cuenta.getSaldo()
        BigDecimal saldoActual = cuentaNegocio.obtenerSaldoActual(cuenta.getIdCuenta()); 
        
        BigDecimal nuevoSaldo = saldoActual.add(movimiento.getImporte());
        boolean saldoActualizado = cuentaNegocio.actualizarSaldo(cuenta.getIdCuenta(), nuevoSaldo.doubleValue());
        if(!saldoActualizado) {
            System.err.println("ERROR: No se pudo actualizar el saldo de la cuenta.");
            return false;
        }
        
        System.out.println("NEGOCIO: Préstamo acreditado. Preparando para generar cuotas.");

     // 1. Obtener los milisegundos desde el Epoch
        long timeInMilliseconds = prestamo.getFechaAlta().getTime(); // Es seguro asignar la subclase a la superclase
        
     // 2. Convertir los milisegundos a un objeto Instant, y de ahí a LocalDate
        LocalDate fechaBase = new Date(timeInMilliseconds)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        
        // 4. *** GENERAR LAS CUOTAS ***
        for (int i = 1; i <= prestamo.getCantidadCuotas(); i++) {
            Cuota nuevaCuota = new Cuota();

            LocalDate fechaVencimientoLocal = fechaBase.plusMonths(i);

            // CONVERSIÓN a java.util.Date
            Date fechaVencimientoUtil = Date.from(
                fechaVencimientoLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()
            );
            
            // ASIGNACIÓN DE DATOS CLAVE
            nuevaCuota.setPrestamo(prestamo); 
            nuevaCuota.setNumeroCuota(i);
            nuevaCuota.setMonto(BigDecimal.valueOf(prestamo.getImportePorMes()));
            nuevaCuota.setEstado(false); 
            nuevaCuota.setFechaVencimiento(fechaVencimientoUtil); 
            
            boolean cuotaAgregada = cuotaDao.agregar(nuevaCuota);
            if (!cuotaAgregada) {
                System.err.println("ERROR: Falla al crear la cuota N°" + i + " para el préstamo " + idPrestamo);
                return false;
            }
        }
        
        System.out.println("Préstamo " + idPrestamo + " aprobado, acreditado y cuotas generadas exitosamente.");
        return true;
    	
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
