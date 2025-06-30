<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Datos de Acceso</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="CSS/solicitarPrestamos.css">
</head>
<body>
	<jsp:include page="masterPage.jsp" />
	
	<main class="container-fluid login-container d-flex justify-content-center align-items-center">
	    <div class="col-md-4">
	        <div class="card shadow">
	            <div class="card-body">
	                <h4 class="card-title text-center mb-4">Datos de Acceso</h4>
	                <form action="login" method="post">
					    <div class="mb-3">
					        <input type="text" class="form-control" id="usuario" name="usuario" placeholder="Usuario" required>
					    </div>
					    <div class="mb-3">
					        <input type="password" class="form-control" id="contrasena" name="contrasena" placeholder="Contraseña" required>
					    </div>
					    <div class="mb-3">
					        <input type="password" class="form-control" id="confirmarContrasena" name="confirmarContrasena" placeholder="Confirmar Contraseña" required>
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
    