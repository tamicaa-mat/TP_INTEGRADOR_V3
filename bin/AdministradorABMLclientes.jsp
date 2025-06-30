<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
 <!DOCTYPE html>
<html lang="es">



<head>
    <meta charset="UTF-8">
    <title>Gestión de Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="CSS/solicitarPrestamos.css">
</head>


<body class="bg-light">
<jsp:include page="masterPage.jsp" />
	<main class="container-fluid login-container d-flex justify-content-center align-items-center">
	    <div class="col-md-4">
	        <div class="card shadow">
	            <div class="card-body">
	                <h4 class="card-title text-center mb-4">Gestión de Clientes</h4>
	                <form action="login" method="post">
	                	<div class="mb-3">
					        <input type="text" class="form-control" id="dni" name="dni" placeholder="DNI" required>
					    </div>
					    <div class="mb-3">
					        <input type="text" class="form-control" id="cuil" name="cuil" placeholder="CUIL" required>
					    </div>
					    <div class="mb-3">
					        <input type="text" class="form-control" id="nombre" name="nombre" placeholder="Nombre" required>
					    </div>
					    <div class="mb-3">
					        <input type="text" class="form-control" id="apellido" name="apellido" placeholder="Apellido" required>
					    </div>
					    <div class="mb-3">
	                        <select class="form-select" id="sexo" name="sexo" required>
	                            <option value="" disabled selected>Sexo</option>
			                    <option value="M">Masculino</option>
			                    <option value="F">Femenino</option>
			                    <option value="O">Otro</option>
	                        </select>
	                    </div>
	                    <div class="mb-3">
						    <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento" required>
						</div>
			            <div class="mb-3">
			                <input type="text" class="form-control" id="direccion" name="direccion" placeholder="Dirección" required>
			            </div>
			            <div class="mb-3">
	                        <select class="form-select" id="localidad" name="localidad" required>
	                            <option value="" disabled selected>Localidad</option>
			                    <option value="1">1</option>
			                    <option value="2">2</option>
			                    <option value="3">3</option>
			                    <option value="4">4</option>
	                        </select>
	                    </div>
	                    <div class="mb-3">
	                        <select class="form-select" id="provincia" name="provincia" required>
	                            <option value="" disabled selected>Provincia</option>
			                    <option value="1">1</option>
			                    <option value="2">2</option>
			                    <option value="3">3</option>
			                    <option value="4">4</option>
	                        </select>
	                    </div>
			            <div class="mb-3">
			                <input type="text" class="form-control" id="correo" name="correo" placeholder="Correo Electrónico" required>
			            </div>
			            <div class="mb-3">
			                <input type="text" class="form-control" id="telefono" name="telefono" placeholder="Teléfono" required>
			            </div>
					    <div class="d-flex justify-content-center gap-2">
					        <button type="submit" class="btn btn-secondary btn-sm w-50">Guardar Cliente</button>
					        <button type="reset" class="btn btn-secondary btn-sm w-50">Limpiar Formulario</button>
					    </div>
					</form>
	            </div>
	        </div>
	    </div>
	</main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>   