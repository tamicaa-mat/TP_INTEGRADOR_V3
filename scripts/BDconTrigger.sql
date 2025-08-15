DROP DATABASE IF EXISTS BancoTpIntegrador3;
CREATE DATABASE BancoTpIntegrador3;
USE BancoTpIntegrador3;

-- Tabla Provincia
CREATE TABLE Provincia (
  IdProvincia INT AUTO_INCREMENT PRIMARY KEY,
  Descripcion VARCHAR(100) NOT NULL
);

-- Tabla Localidad
CREATE TABLE Localidad (
  IdLocalidad INT AUTO_INCREMENT PRIMARY KEY,
  Descripcion VARCHAR(100) NOT NULL,
  IdProvincia INT,
  FOREIGN KEY (IdProvincia) REFERENCES Provincia(IdProvincia)
);

-- Tabla TipoUsuario
CREATE TABLE TipoUsuario (
  IdTipoUsuario INT AUTO_INCREMENT PRIMARY KEY,
  Descripcion VARCHAR(50)
);

-- Tabla Usuario
CREATE TABLE Usuario (
  IdUsuario INT AUTO_INCREMENT PRIMARY KEY,
  NombreUsuario VARCHAR(50),
  Password VARCHAR(255),
  IdTipoUsuario INT,
  Estado BOOLEAN DEFAULT 1,
  FOREIGN KEY (IdTipoUsuario) REFERENCES TipoUsuario(IdTipoUsuario)
);

-- Tabla Cliente
CREATE TABLE Cliente (
  IdCliente INT AUTO_INCREMENT PRIMARY KEY,
  Dni VARCHAR(10),
  Cuil VARCHAR(15),
  Nombre VARCHAR(50),
  Apellido VARCHAR(50),
  Sexo VARCHAR(1),
  Nacionalidad VARCHAR(50),
  FechaNacimiento DATE,
  Direccion VARCHAR(100),
  CorreoElectronico VARCHAR(50),
  Telefono VARCHAR(50),
  IdLocalidad INT,
  IdUsuario INT,
  Estado BOOLEAN DEFAULT 1,
  FOREIGN KEY (IdLocalidad) REFERENCES Localidad(IdLocalidad),
  FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario)
);

-- Tabla TipoCuenta
CREATE TABLE TipoCuenta (
  IdTipoCuenta INT AUTO_INCREMENT PRIMARY KEY,
  Descripcion VARCHAR(50)
);

-- Tabla Cuenta
CREATE TABLE Cuenta (
  IdCuenta INT AUTO_INCREMENT PRIMARY KEY,
  IdCliente INT,
  FechaCreacion DATE,
  IdTipoCuenta INT,
  NumeroCuenta VARCHAR(20),
  Cbu VARCHAR(22),
  Saldo DECIMAL(15,2),
  Estado BOOLEAN DEFAULT 1,
  FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente),
  FOREIGN KEY (IdTipoCuenta) REFERENCES TipoCuenta(IdTipoCuenta)
);

-- Tabla Prestamo
CREATE TABLE Prestamo (
  IdPrestamo INT AUTO_INCREMENT PRIMARY KEY,
  IdCliente INT,
  IdCuentaAsociada INT,
  FechaAlta DATE,
  ImportePedido DECIMAL(15,2),
  PlazoMeses INT,
  ImportePorMes DECIMAL(15,2),
  Interes DECIMAL(5,2),
  CantidadCuotas INT,
  Estado BOOLEAN DEFAULT 1,
  FOREIGN KEY (IdCliente) REFERENCES Cliente(IdCliente),
  FOREIGN KEY (IdCuentaAsociada) REFERENCES Cuenta(IdCuenta)
);

-- Tabla Cuota
CREATE TABLE Cuota (
  IdCuota INT AUTO_INCREMENT PRIMARY KEY,
  IdPrestamo INT,
  NumeroCuota INT,
  Monto DECIMAL(15,2),
  FechaPago DATE,
  Estado BOOLEAN DEFAULT 0,
  FOREIGN KEY (IdPrestamo) REFERENCES Prestamo(IdPrestamo)
);

-- Tabla TipoMovimiento
CREATE TABLE TipoMovimiento (
  IdTipoMovimiento INT AUTO_INCREMENT PRIMARY KEY,
  Descripcion VARCHAR(100)
);

-- Tabla Movimiento modifico segun corrección de la primer entrega
CREATE TABLE Movimiento (
  IdMovimiento INT AUTO_INCREMENT PRIMARY KEY,
  FechaHora DATETIME,
  Referencia VARCHAR(255),
  Importe DECIMAL(15,2),
  IdTipoMovimiento INT,
  IdCuenta INT,
  FOREIGN KEY (IdTipoMovimiento) REFERENCES TipoMovimiento(IdTipoMovimiento),
  FOREIGN KEY (IdCuenta) REFERENCES Cuenta(IdCuenta)
  
);

-- agrego tabla transferencia segun correccion de la primer entrega
CREATE TABLE Transferencia (
  IdTransferencia INT AUTO_INCREMENT PRIMARY KEY,
  IdCuentaOrigen INT,
  IdCuentaDestino INT,
  Monto DECIMAL(15, 2) NOT NULL DEFAULT 0,
  FechaHora DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (IdCuentaOrigen) REFERENCES Cuenta(IdCuenta),
  FOREIGN KEY (IdCuentaDestino) REFERENCES Cuenta(IdCuenta)
);

-- INSERTS
INSERT INTO Provincia (Descripcion) VALUES 
('San Luis'), ('Misiones'), ('Cordoba'), ('Buenos Aires'), ('La Rioja'), 
('Tierra del Fuego'), ('Mendoza'), ('Santa Cruz'), ('Formosa'), ('Tucuman'),
('Jujuy'), ('Neuquen'), ('La Pampa'), ('Rio Negro'), ('Entre Rios'),
('Chaco'), ('Chubut'), ('Santa Fe');

INSERT INTO Localidad (Descripcion, IdProvincia) VALUES 
('Villa Mercedez', 1), ('Posadas', 2), ('Rio Cuarto', 3), ('Florencio Varela', 4),
('Chilecito', 5), ('Rio Grande', 6), ('Lujan de Cuyo', 7), ('Caleta Olivia', 8),
('Clorinda', 9), ('Tafi', 10), ('Tilcara', 11), ('Zapala', 12),
('Santa Rosa', 13), ('Viedma', 14), ('Colon', 15), ('Villa Angela', 16),
('Puerto Madryn', 17), ('Rafaela', 18);

INSERT INTO TipoUsuario (Descripcion) VALUES ('Administrador'), ('Cliente');

INSERT INTO Usuario (NombreUsuario, Password, IdTipoUsuario, Estado) VALUES
('adminbank', 'admin123', 1 , 1);

-- ============================================
-- USUARIOS
-- ============================================

INSERT INTO Usuario (NombreUsuario, Password, IdTipoUsuario, Estado)
VALUES ('clienteLucianaSoria', 'clave123', 2, 1);
SET @IdUsuarioLuciana := LAST_INSERT_ID();

INSERT INTO Usuario (NombreUsuario, Password, IdTipoUsuario, Estado)
VALUES ('clienteBrunoFarias', 'clave456', 2, 1);
SET @IdUsuarioBruno := LAST_INSERT_ID();

INSERT INTO Usuario (NombreUsuario, Password, IdTipoUsuario, Estado)
VALUES ('clienteNoeliaPaz', 'clave789', 2, 1);
SET @IdUsuarioNoelia := LAST_INSERT_ID();

INSERT INTO Usuario (NombreUsuario, Password, IdTipoUsuario, Estado)
VALUES ('clienteGastonRios', 'clave101', 2, 1);
SET @IdUsuarioGaston := LAST_INSERT_ID();

INSERT INTO Usuario (NombreUsuario, Password, IdTipoUsuario, Estado)
VALUES ('clienteAndreaMartinez', 'clave202', 2, 1);
SET @IdUsuarioAndrea := LAST_INSERT_ID();

-- ============================================
-- CLIENTES
-- ============================================

INSERT INTO Cliente (Dni, Cuil, Nombre, Apellido, Sexo, Nacionalidad, FechaNacimiento,
  Direccion, CorreoElectronico, Telefono, IdLocalidad, IdUsuario, Estado)
VALUES
('40444111', '27-40444111-3', 'Luciana', 'Soria', 'F', 'ARGENTINA', '1992-09-01',
 'Calle Los Sauces 12', 'luciana.soria@mail.com', '1155667788', 4, @IdUsuarioLuciana, 1),

('38555122', '20-38555122-6', 'Bruno', 'Farias', 'M', 'ARGENTINA', '1988-04-18',
 'Avenida Belgrano 222', 'bruno.farias@mail.com', '1166778899', 5, @IdUsuarioBruno, 1),

('42666333', '27-42666333-5', 'Noelia', 'Paz', 'F', 'ARGENTINA', '1995-12-05',
 'Calle Luna Nueva 101', 'noelia.paz@mail.com', '1177889900', 6, @IdUsuarioNoelia, 1),

('39777444', '20-39777444-1', 'Gaston', 'Rios', 'M', 'ARGENTINA', '1990-02-28',
 'Pasaje Las Rosas 400', 'gaston.rios@mail.com', '1144556677', 7, @IdUsuarioGaston, 1),

('43333555', '27-43333555-2', 'Andrea', 'Martinez', 'F', 'ARGENTINA', '1983-07-11',
 'Boulevard Mitre 999', 'andrea.martinez@mail.com', '1133445566', 8, @IdUsuarioAndrea, 1);


INSERT INTO TipoCuenta (Descripcion) VALUES ('Caja de Ahorro'), ('Cuenta Corriente');

-- ============================================
-- OBTENER ID CLIENTES
-- ============================================

SELECT IdCliente INTO @IdClienteLuciana FROM Cliente WHERE Dni = '40444111';
SELECT IdCliente INTO @IdClienteBruno   FROM Cliente WHERE Dni = '38555122';
SELECT IdCliente INTO @IdClienteNoelia  FROM Cliente WHERE Dni = '42666333';
SELECT IdCliente INTO @IdClienteGaston  FROM Cliente WHERE Dni = '39777444';
SELECT IdCliente INTO @IdClienteAndrea  FROM Cliente WHERE Dni = '43333555';

-- ============================================
-- CUENTAS
-- ============================================

INSERT INTO Cuenta (IdCliente, FechaCreacion, IdTipoCuenta, NumeroCuenta, Cbu, Saldo, Estado) VALUES
(@IdClienteLuciana, '2024-08-10', 1, '2020202020', '4567891234567890123456', 150000.00, 1),
(@IdClienteBruno,   '2024-09-22', 2, '3030303030', '5678912345678901234567', 97000.00, 1),
(@IdClienteNoelia,  '2024-10-05', 1, '4040404040', '6789123456789012345678', 25000.00, 1),
(@IdClienteGaston,  '2024-12-01', 1, '5050505050', '7891234567890123456789', 40000.00, 1),
(@IdClienteAndrea,  '2025-01-15', 2, '6060606060', '8901234567890123456789', 30000.00, 1);

INSERT INTO TipoMovimiento (Descripcion) VALUES 
('Alta Cuenta'), ('Alta Prestamo'), ('Pago Prestamo'), ('Transferencia');

-- Script para limpiar usuarios huérfanos si ya tienes algunos en la BD
-- Ejecutar SOLO si ya hay usuarios con NombreUsuario y Password NULL
-- DELETE FROM Usuario WHERE NombreUsuario IS NULL AND Password IS NULL;

