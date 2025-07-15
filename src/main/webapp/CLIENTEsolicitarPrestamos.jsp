
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Cuenta" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Solicitar Préstamos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
   
</head>
<body class="bg-light">
    <jsp:include page="masterPage.jsp" />

    <main class="container my-5">
        <div class="col-md-6 mx-auto">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="card-title mb-4 text-center">Solicitar Préstamo</h4>

  <form action="SolicitarPrestamoServlet" method="post">
    <div class="mb-3">
        <label class="form-label small text-muted">Cuenta</label>
        <select class="form-select" name="cuentaSeleccionada" required>
            <option value="" disabled selected>Seleccione una cuenta</option>
            <c:forEach var="c" items="${cuentas}">
              <c:if test="${c.estado}">
    <option value="${c.idCuenta}">${c.numeroCuenta}</option>
</c:if>
            </c:forEach>
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
