<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de Movimientos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="masterPage.jsp" />
    <main class="container py-5">
        <h4 class="text-center mb-4">Movimientos Bancarios</h4>

        <!-- Formulario de filtros -->
        <div class="container mb-4">
            <form method="get" action="MovimientoServlet" class="row g-3 align-items-end">
                <div class="col-md-4">
                    <label for="cuenta" class="form-label">Cuenta</label>
                    <select name="cuenta" id="cuenta" class="form-select">
                        <option value="0" ${filtroCuenta == 0 ? "selected" : ""}>Todas</option>
                         <c:forEach var="c" items="${cuentas}">
    <c:choose>
        <c:when test="${not empty c.tipoCuenta}">
            <option value="${c.idCuenta}" ${filtroCuenta == c.idCuenta ? "selected" : ""}>
                ${c.tipoCuentaObjeto.descripcion} Nº ${c.numeroCuenta}
            </option>
        </c:when>
        <c:otherwise>
            <option disabled>Error: cuenta sin tipo (${c.idCuenta})</option>
        </c:otherwise>
    </c:choose>
</c:forEach>    
                            
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="tipo" class="form-label">Tipo de Movimiento</label>
                    <select name="tipo" id="tipo" class="form-select">
                        <option value="0" ${filtroTipo == 0 ? "selected" : ""}>Todos</option>
                        <option value="1" ${filtroTipo == 1 ? "selected" : ""}>Alta Cuenta</option>
                        <option value="2" ${filtroTipo == 2 ? "selected" : ""}>Alta Préstamo</option>
                        <option value="3" ${filtroTipo == 3 ? "selected" : ""}>Pago Préstamo</option>
                        <option value="4" ${filtroTipo == 4 ? "selected" : ""}>Transferencia</option>
                        
                    </select>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-secondary w-100">Filtrar</button>
                </div>
            </form>
        </div>

        <!-- Tabla de movimientos -->
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-secondary">
                    <tr>
                        <th>Fecha y Hora</th>
                        <th>Concepto</th>
                        <th>Importe</th>
                        <th>Tipo</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty movimientos}">
                            <tr>
                                <td colspan="4" class="text-center text-muted">No se encontraron movimientos para los filtros seleccionados.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="m" items="${movimientos}">
                                <tr>
                                    <td>${m.fechaHora}</td>
                                    <td>${m.referencia}</td>
                                    <td>$${m.importe}</td>
                                    <td>${m.tipoMovimiento.descripcion}</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
