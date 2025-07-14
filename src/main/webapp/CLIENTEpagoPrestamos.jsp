<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Pago de Préstamos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<jsp:include page="masterPage.jsp" />

<main class="container py-5">
    <div class="card col-md-6 mx-auto shadow-sm">
        <div class="card-body">
            <h4 class="text-center mb-4">Pago de Préstamos</h4>

            <!-- Formulario para seleccionar cuenta -->
            <form action="PagoPrestamoServlet" method="get">
                <label class="form-label">Seleccione cuenta</label>
                <select class="form-select mb-3" name="idCuenta" required onchange="this.form.submit()">
                    <option value="" disabled selected>Seleccione una cuenta</option>
                    <c:forEach var="cuenta" items="${cuentas}">
                        <option value="${cuenta.idCuenta}" 
                            ${cuenta.idCuenta == param.idCuenta ? "selected" : ""}>
                            ${cuenta.numeroCuenta}
                        </option>
                    </c:forEach>
                </select>
                <!-- Este form se autocompleta con el onchange. Si querés usar botón, sacá el onchange -->
            </form>

          <!-- Formulario para pagar cuota -->
<c:if test="${not empty prestamos}">
    <form action="PagarCuotaServlet" method="get">
        <input type="hidden" name="idCuenta" value="${param.idCuenta}" />
        <label class="form-label">Seleccione préstamo</label>
        <select class="form-select mb-3" name="idPrestamo" required>
            <option value="" disabled selected>Seleccione un préstamo</option>
            <c:forEach var="p" items="${prestamos}">
                <option value="${p.idPrestamo}">
                    Préstamo #${p.idPrestamo}
                </option>
            </c:forEach>
        </select>

        <label class="form-label">Monto a pagar</label>
        <input type="number" step="0.01" class="form-control mb-3" name="montoPago" required />

        <div class="text-center mt-4">
            <button type="submit" class="btn btn-success">Pagar cuota</button>
        </div>
    </form>
</c:if>
          
            
            
        </div>
    </div>
</main>

</body>
</html>
