package daoImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import daoImpl.Conexion;
import dao.TransferenciaDao;
import dominio.Cuenta;
import dominio.Transferencia;

public class TransferenciaDaoImpl implements TransferenciaDao {

    private static final String ACTUALIZAR_SALDO = "UPDATE Cuenta SET Saldo = ? WHERE IdCuenta = ?";
    private static final String INSERTAR_MOVIMIENTO = "INSERT INTO Movimiento (FechaHora, Referencia, Importe, IdTipoMovimiento, IdCuenta) VALUES (NOW(), ?, ?, ?, ?)";
    private static final String INSERTAR_TRANSFERENCIA = "INSERT INTO Transferencia (IdCuentaOrigen, IdCuentaDestino, Monto) VALUES (?, ?, ?)";
    private static final int ID_TIPO_MOVIMIENTO_TRANSFERENCIA = 4; 
    private static final String SELECT_TRANSFERENCIAS = 
    	    "SELECT IdTransferencia, IdCuentaOrigen, IdCuentaDestino, Monto FROM Transferencia WHERE IdCuentaOrigen = ?";

    
    
    
    
    @Override
    public boolean ejecutarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, BigDecimal monto) {
        Connection conexion = null;
        boolean exito = false;

        try {
            conexion = Conexion.getConexion().getSQLConexion();
            // --- INICIO DE LA TRANSACCIÓN ---
            conexion.setAutoCommit(false);

            // 1. Descontar saldo de la cuenta origen
            BigDecimal nuevoSaldoOrigen = cuentaOrigen.getSaldo().subtract(monto);
            try (PreparedStatement psUpdateOrigen = conexion.prepareStatement(ACTUALIZAR_SALDO)) {
                psUpdateOrigen.setBigDecimal(1, nuevoSaldoOrigen);
                psUpdateOrigen.setInt(2, cuentaOrigen.getIdCuenta());
                psUpdateOrigen.executeUpdate();
            }

            // 2. Acreditar saldo a la cuenta destino
            BigDecimal nuevoSaldoDestino = cuentaDestino.getSaldo().add(monto);
            try (PreparedStatement psUpdateDestino = conexion.prepareStatement(ACTUALIZAR_SALDO)) {
                psUpdateDestino.setBigDecimal(1, nuevoSaldoDestino);
                psUpdateDestino.setInt(2, cuentaDestino.getIdCuenta());
                psUpdateDestino.executeUpdate();
            }

            // 3. Registrar el movimiento de DÉBITO en la cuenta origen
            try (PreparedStatement psMovOrigen = conexion.prepareStatement(INSERTAR_MOVIMIENTO)) {
                psMovOrigen.setString(1, "Transferencia enviada a CBU " + cuentaDestino.getCbu());
                psMovOrigen.setBigDecimal(2, monto.negate()); // Importe negativo
                psMovOrigen.setInt(3, ID_TIPO_MOVIMIENTO_TRANSFERENCIA);
                psMovOrigen.setInt(4, cuentaOrigen.getIdCuenta());
                psMovOrigen.executeUpdate();
            }

            // 4. Registrar el movimiento de CRÉDITO en la cuenta destino
            try (PreparedStatement psMovDestino = conexion.prepareStatement(INSERTAR_MOVIMIENTO)) {
                psMovDestino.setString(1, "Transferencia recibida de CBU " + cuentaOrigen.getCbu());
                psMovDestino.setBigDecimal(2, monto); // Importe positivo
                psMovDestino.setInt(3, ID_TIPO_MOVIMIENTO_TRANSFERENCIA);
                psMovDestino.setInt(4, cuentaDestino.getIdCuenta());
                psMovDestino.executeUpdate();
            }
            
            // 5. Registrar en la tabla Transferencia (si es necesario para auditoría)
            try (PreparedStatement psTransferencia = conexion.prepareStatement(INSERTAR_TRANSFERENCIA)) {
                psTransferencia.setInt(1, cuentaOrigen.getIdCuenta());
                
                psTransferencia.setInt(2, cuentaDestino.getIdCuenta());
                psTransferencia.setBigDecimal(3, monto);
              
                psTransferencia.executeUpdate();
            }

            // --- COMMIT ---
            // Si todo salió bien, confirma los cambios en la BD
            conexion.commit();
            exito = true;

        } catch (SQLException e) {
            // --- ROLLBACK ---
            // Si algo falló, revierte todos los cambios
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            // Podrías lanzar una excepción personalizada aquí
            
        } finally {
            if (conexion != null) {
                try {
                    // Devuelve la conexión a su estado normal
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return exito;
    }





	@Override
	public List<Transferencia> obtenerTransferenciasPorCuentaOrigen(int idCuentaOrigen) {
		 List<Transferencia> lista = new ArrayList<>();
		    
		 
		 
		    try (Connection conexion = Conexion.getConexion().getSQLConexion();
		         PreparedStatement ps = conexion.prepareStatement(SELECT_TRANSFERENCIAS)) {
		        
		        ps.setInt(1, idCuentaOrigen);
		        ResultSet rs = ps.executeQuery();
		        
		        while (rs.next()) {
		            Transferencia t = new Transferencia();
		            t.setIdTransferencia(rs.getInt("IdTransferencia"));
		            t.setIdCuentaOrigen(rs.getInt("IdCuentaOrigen"));
		            t.setIdCuentaDestino(rs.getInt("IdCuentaDestino"));
		            t.setMonto(rs.getDouble("Monto"));
		            lista.add(t);
		        }
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    
		    return lista;
	}
}


