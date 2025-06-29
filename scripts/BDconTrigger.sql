
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

-- Tabla Movimiento
CREATE TABLE Movimiento (
  IdMovimiento INT AUTO_INCREMENT PRIMARY KEY,
  FechaHora DATETIME,
  Referencia VARCHAR(255),
  Importe DECIMAL(15,2),
  IdTipoMovimiento INT,
  IdCuentaOrigen INT,
  IdCuentaDestino INT,
  FOREIGN KEY (IdTipoMovimiento) REFERENCES TipoMovimiento(IdTipoMovimiento),
  FOREIGN KEY (IdCuentaOrigen) REFERENCES Cuenta(IdCuenta),
  FOREIGN KEY (IdCuentaDestino) REFERENCES Cuenta(IdCuenta)
);

-- Inserción de datos
-- Provincias
INSERT INTO Provincia (Descripcion) VALUES 
('San Luis'), ('Misiones'), ('Cordoba'), ('Buenos Aires'), ('La Rioja'), 
('Tierra del Fuego'), ('Mendoza'), ('Santa Cruz'), ('Formosa'), ('Tucuman'),
('Jujuy'), ('Neuquen'), ('La Pampa'), ('Rio Negro'), ('Entre Rios'),
('Chaco'), ('Chubut'), ('Santa Fe');

-- Localidades
INSERT INTO Localidad (Descripcion, IdProvincia) VALUES 
('Villa Mercedez', 1), ('Posadas', 2), ('Rio Cuarto', 3), ('Florencio Varela', 4),
('Chilecito', 5), ('Rio Grande', 6), ('Lujan de Cuyo', 7), ('Caleta Olivia', 8),
('Clorinda', 9), ('Tafi', 10), ('Tilcara', 11), ('Zapala', 12),
('Santa Rosa', 13), ('Viedma', 14), ('Colon', 15), ('Villa Angela', 16),
('Puerto Madryn', 17), ('Rafaela', 18);

-- Tipos de usuario
INSERT INTO TipoUsuario (Descripcion) VALUES ('Administrador'), ('Cliente');

-- Usuarios
INSERT INTO Usuario (NombreUsuario, Password, IdTipoUsuario, Estado) VALUES 
('adminbank', 'admin123', 1 , 1),
('clienteAmoMariaFernanda', 'cliente1', 2, 1),
('clienteVenecioSalvador', 'cliente2', 2, 1),
('clientetamaramarilyn', 'cliente3', 2, 1),
('clienteHomeroChispa', 'cliente4', 2, 1);

-- Clientes
INSERT INTO Cliente (Dni, Cuil, Nombre, Apellido, Sexo, Nacionalidad, FechaNacimiento,
  Direccion, CorreoElectronico, Telefono, IdLocalidad, IdUsuario, Estado) VALUES 
('12345688', '21-12345688-5', 'Maria Fernanda', 'Amo', 'F', 'ARGENTINA', '1990-06-20', 
 'Rambla 999', 'mariafernandaamo@hotmail.com', '1588977400', 2, 2, 1),

('31553699', '20-31553699-5', 'Salvador', 'Venecio', 'M', 'ARGENTINA', '1985-10-02', 
 'Calle ILDEFONZO 3456', 'salvadorvenecio@hotmail.com', '1578599666', 1, 3, 1),

('30123456', '20-30123456-5', 'Tamara', 'Marilyn', 'F', 'ARGENTINA', '1993-08-20', 
 'Calle Sol 123', 'tamaramarilyn@hotmail.com', '1500055500', 1, 4, 1),

('29685599', '20-29685599-5', 'Homero', 'Chispa', 'M', 'ARGENTINA', '1986-06-14', 
 'Calle luna 689', 'homerochispa@hotmail.com', '15777888', 3, 5, 1);

-- TipoCuenta
INSERT INTO TipoCuenta (Descripcion) VALUES ('Caja de Ahorro'), ('Cuenta Corriente');

-- Cuentas
INSERT INTO Cuenta (IdCliente, FechaCreacion, IdTipoCuenta, NumeroCuenta, Cbu, Saldo, Estado) VALUES 
(1, '2023-01-20', 1, '1111222233', '3211234567890123456789', 55000.00, 1),
(2, '2023-03-15', 2, '4444555566', '6549871234567890123456', 87000.00, 1),
(3, '2023-07-08', 1, '7777888899', '7896541234567890123456', 30000.00, 1),
(4, '2024-01-01', 1, '1010101010', '9998887776665554443332', 20000.00, 1);

-- Prestamos
INSERT INTO Prestamo (IdCliente, IdCuentaAsociada, FechaAlta, ImportePedido, PlazoMeses, ImportePorMes, Interes, CantidadCuotas, Estado) VALUES 
(1, 1, '2024-09-15', 120000.00, 12, 11000.00, 10.00, 12, 1),
(2, 2, '2024-11-01', 90000.00, 18, 6200.00, 12.50, 18, 1),
(3, 3, '2025-01-10', 60000.00, 6, 10200.00, 15.00, 6, 1),
(4, 4, '2025-03-05', 30000.00, 3, 10500.00, 5.00, 3, 1);

-- Cuotas
INSERT INTO Cuota (IdPrestamo, NumeroCuota, Monto, FechaPago, Estado) VALUES
(1, 1, 11000.00, '2024-10-15', 1),
(2, 1, 6200.00, '2024-12-01', 1),
(2, 2, 6200.00, '2025-01-01', 1),
(3, 1, 10200.00, '2025-02-10', 1),
(4, 1, 10500.00, '2025-04-05', 1);

-- TipoMovimiento
INSERT INTO TipoMovimiento (Descripcion) VALUES 
('Alta Cuenta'), ('Alta Prestamo'), ('Pago Prestamo'), ('Transferencia');

-- Movimiento
INSERT INTO Movimiento (FechaHora, Referencia, Importe, IdTipoMovimiento, IdCuentaOrigen, IdCuentaDestino) VALUES 
('2025-04-18 21:16:35', 'deposito a cuenta', 1051.02, 2, 2, 1),
('2025-03-28 07:14:33', 'transferencia a cuenta', 42979.87, 1, 3, 2),
('2024-07-03 20:58:32', 'deposito a cuenta', 27372.81, 1, 2, 3);









-- TRIGGER 
-- CREATE DEFINER=`root`@`localhost` TRIGGER cliente_BEFORE_INSERT
-- BEFORE INSERT ON cliente
-- FOR EACH ROW
-- BEGIN
-- DECLARE vIdUsuario INT;

 -- IF NEW.IdUsuario IS NULL THEN
   -- INSERT INTO Usuario (NombreUsuario, Password, IdTipoUsuario, Estado)
   -- VALUES (NULL, NULL, 2, 1);

     -- SET vIdUsuario = LAST_INSERT_ID();
   -- SET NEW.IdUsuario = vIdUsuario;
  --  END IF;
 -- END


-- PRUEBA TRIGGER FUNCIONAAAAA SIN EL SP

INSERT INTO Cliente (Dni, Cuil, Nombre, Apellido, Sexo, Nacionalidad, FechaNacimiento,
  Direccion, CorreoElectronico, Telefono, IdLocalidad, IdUsuario, Estado) VALUES 
('55555555', '21-55555555-5', 'Marco Pan', 'Polo', 'M', 'ARGENTINO', '1990-05-20', 
 'Rambla NNN 2255', 'marcopanpolo@hotmail.com', '1555566622', 7, NULL, 1);
