<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            </form>

            <!-- Formulario para seleccionar préstamo -->
            <c:if test="${not empty prestamos}">
                <form action="PagoPrestamoServlet" method="get">
                    <input type="hidden" name="idCuenta" value="${param.idCuenta}" />
                    <label class="form-label">Seleccione préstamo</label>
                    <select class="form-select mb-3" name="idPrestamo" required onchange="this.form.submit()">
                        <option value="" disabled selected>Seleccione un préstamo</option>
                        <c:forEach var="p" items="${prestamos}">
                            <option value="${p.idPrestamo}"
                                    ${p.idPrestamo == param.idPrestamo ? "selected" : ""}>
                                Préstamo #${p.idPrestamo}
                            </option>
                        </c:forEach>
                    </select>
                </form>
            </c:if>

            <!-- Mostrar datos y formulario de pago -->
            <c:if test="${not empty param.idPrestamo}">
                <form action="PagarCuotaServlet" method="get">
                    <input type="hidden" name="idCuenta" value="${param.idCuenta}" />
                    <input type="hidden" name="idPrestamo" value="${param.idPrestamo}" />
                    <c:forEach var="p" items="${prestamos}">
                        <c:if test="${p.idPrestamo == param.idPrestamo}">
                            <div class="mb-3">
                                <p><strong>Importe pedido:</strong> $${p.importePedido}</p>
                                <p><strong>Importe por mes (cuota):</strong> $${p.importePorMes}</p>
                                <p><strong>Interés:</strong> ${p.interes}%</p>
                                <p><strong>Plazo:</strong> ${p.plazoMeses} meses</p>
                                <p><strong>Cantidad de cuotas:</strong> ${p.cantidadCuotas}</p>
                                <input type="hidden" name="montoPago" value="${p.importePorMes}" />
                            </div>
                        </c:if>
                    </c:forEach>
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
