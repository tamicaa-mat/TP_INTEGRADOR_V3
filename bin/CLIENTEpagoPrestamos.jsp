<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Solicitar Préstamos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="CSS/solicitarPrestamos.css">
</head>
<body class="bg-light">
	<jsp:include page="masterPage.jsp" />
	<main>
		<div class="col-md-6">
	        <div class="card shadow-sm">
	            <div class="card-body">
	                <h4 class="card-title mb-4 text-center">Pago de Préstamos</h4>
	                <form action="procesarSolicitud.jsp" method="post">
	                    <div class="mb-3">
	                        <select class="form-select" id="cuenta" name="cuenta" required>
	                            <option value="" disabled selected>Cuenta Asociada</option>
	                            <option value="CA001-123456">CA001-123456</option>
	                            <option value="CA002-654321">CA002-654321</option>
	                        </select>
	                    </div>
	                    <div class="mb-3">
	                        <select class="form-select" id="prestamo" name="prestamo" required>
	                            <option value="" disabled selected>Préstamo</option>
	                            <option value="CA001-123456">Prestamo CA001-123456</option>
	                            <option value="CA002-654321">Prestamo CA002-654321</option>
	                        </select>
	                    </div>
	                    <button type="submit" class="btn btn-secondary w-50 d-block mx-auto">Consultar Cuota</button>
	                </form>
	            </div>
	        </div>
	    </div>
	</main>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>