<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Cuenta" %>

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
	                <h4 class="card-title mb-4 text-center">Solicitar Préstamo</h4>
	               
	                <form action="SolicitarPrestamoServlet" method="post">
	                    <div class="mb-3">
	                       
	                     <select class="form-select" id="cuentas" name="cuentas" required>
    <option value="" disabled selected>Seleccione una cuenta</option>
    <% 
        List<Cuenta> cuentas = (List<Cuenta>) request.getAttribute("cuentas");
        if (cuentas != null) {
            for (Cuenta c : cuentas) {
    %>
        <option value="<%= c.getIdCuenta() %>"><%= c.getNumeroCuenta() %></option>
    <%      }
        }
    %>
</select>
	                     
	                    </div>
	                    <div class="mb-3">
	                        <input type="number" class="form-control" id="importe" name="importe" placeholder="Importe solicitado" min="1000" required>
	                    </div>
	                    <div class="mb-3">
	                        <input type="number" class="form-control" id="plazo" name="plazo" placeholder="Plazo (en meses)" min="1" max="60" required>
	                    </div>
	                    <button type="submit" class="btn btn-secondary w-50 d-block mx-auto">Enviar solicitud</button>
	                </form>
	            </div>
	        </div>
	    </div>
	</main>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>