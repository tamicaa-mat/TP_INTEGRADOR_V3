<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Informacion</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="CSS/estilos.css">
<style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .datos-container {
            max-width: 800px;
            margin: 30px auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        .datos-header {
            background-color: #2c3e50;
            color: white;
            padding: 20px;
            border-radius: 8px 8px 0 0;
        }
        .datos-body {
            padding: 25px;
        }
        .seccion-titulo {
            color: #2c3e50;
            border-left: 4px solid #3498db;
            padding-left: 10px;
            margin: 25px 0 15px 0;
            font-weight: 600;
        }
        .dato-item {
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }
        .dato-label {
            font-weight: 500;
            color: #7f8c8d;
            font-size: 0.9rem;
            margin-bottom: 5px;
        }
        .dato-valor {
            font-size: 1rem;
            color: #2c3e50;
            font-weight: 400;
        }
        .password-dots {
            letter-spacing: 2px;
            font-size: 1.2rem;
        }
    </style>
</head>
<body>
<jsp:include page="masterPage.jsp" />

    <main class="container py-5">
<div class="container">
        <div class="datos-container">
            <div class="datos-header text-center">
                <h3><i class="bi bi-person-badge"></i> Mis Datos Personales</h3>
            </div>
            
            <div class="datos-body">
                <h5 class="seccion-titulo">INFORMACIÓN PERSONAL</h5>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Nombre</div>
                            <div class="dato-valor">Laura</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Apellido</div>
                            <div class="dato-valor">Martínez</div>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">DNI</div>
                            <div class="dato-valor">34.567.890</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">CUIL/CUIT</div>
                            <div class="dato-valor">27-34567890-4</div>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Sexo</div>
                            <div class="dato-valor">Femenino</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Nacionalidad</div>
                            <div class="dato-valor">Argentina</div>
                        </div>
                    </div>
                </div>
                
                <div class="dato-item">
                    <div class="dato-label">Fecha de Nacimiento</div>
                    <div class="dato-valor">12/05/1990</div>
                </div>
                
                <h5 class="seccion-titulo">INFORMACIÓN DE CONTACTO</h5>
                
                <div class="dato-item">
                    <div class="dato-label">Dirección</div>
                    <div class="dato-valor">Av. Libertador 1234, Piso 3, Depto B</div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Localidad</div>
                            <div class="dato-valor">Capital Federal</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Provincia</div>
                            <div class="dato-valor">Buenos Aires</div>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Teléfono</div>
                            <div class="dato-valor">+54 11 4567-8901</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Correo Electrónico</div>
                            <div class="dato-valor">laura.martinez@example.com</div>
                        </div>
                    </div>
                </div>
                
                <h5 class="seccion-titulo">DATOS DE ACCESO</h5>
                
                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Usuario</div>
                            <div class="dato-valor">lauramart90</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Contraseña</div>
                            <div class="dato-valor password-dots">••••••••••</div>
                        </div>
                    </div>
                </div>
                
                <div class="alert alert-info mt-4">
                    <i class="bi bi-info-circle"></i> Para actualizar tus datos personales, por favor comunícate con nuestro servicio de atención al cliente.
                </div>
            </div>
        </div>
    </div>
        </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>