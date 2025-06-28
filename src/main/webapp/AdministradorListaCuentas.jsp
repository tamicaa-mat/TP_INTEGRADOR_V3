<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Cuentas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="CSS/estilos.css">
</head>
<body class="bg-light">
    <jsp:include page="masterPage.jsp" />

    <main class="container py-5">
        <h3 class="mb-3">Gestión de Cuentas para el Cliente DNI: <strong><c:out value="${dniCliente}" /></strong></h3>

        <div class="card mb-4">
            <div class="card-header fw-bold">
                Agregar Nueva Cuenta
            </div>
            <div class="card-body">
                <form action="CuentaServlet" method="post">
                    <input type="hidden" name="action" value="agregar">
                    <input type="hidden" name="dniCliente" value="<c:out value="${dniCliente}" />">
                    <div class="row align-items-end">
                        <div class="col-md-4">
                            <label for="idTipoCuenta" class="form-label">Tipo de Cuenta</label>
                            <select name="idTipoCuenta" id="idTipoCuenta" class="form-select" required>
                                <option value="" disabled selected>Seleccione un tipo</option>
                                <option value="1">Caja de Ahorro</option>
                                <option value="2">Cuenta Corriente</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <button type="submit" class="btn btn-success">Crear Cuenta</button>
                        </div>
                    </div>
                </form>
                <c:if test="${not empty errorLimiteCuentas}">
                    <div class="alert alert-danger mt-3" role="alert">
                        <c:out value="${errorLimiteCuentas}" />
                    </div>
                </c:if>
            </div>
        </div>

        <h4 class="text-center mb-4">Cuentas Activas del Cliente</h4>
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-secondary">
                    <tr>
                        <th>N° de Cuenta</th>
                        <th>Tipo</th>
                        <th>CBU</th>
                        <th>Fecha Alta</th>
                        <th class="text-end">Saldo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cuenta" items="${listaCuentas}">
                        <tr>
                            <td><c:out value="${cuenta.getNumeroCuenta()}" /></td>
                            <td><c:out value="${cuenta.getTipoCuenta().getDescripcion()}" /></td>
                            <td><c:out value="${cuenta.getCbu()}" /></td>
                            <td><c:out value="${cuenta.getFechaCreacion()}" /></td>
                            <td class="text-end">$<c:out value="${cuenta.getSaldo()}" /></td>
                            <td>
                                <form action="CuentaServlet" method="post" onsubmit="return confirm('¿Está seguro que desea eliminar esta cuenta?');">
                                    <input type="hidden" name="action" value="eliminar">
                                    <input type="hidden" name="idCuenta" value="<c:out value="${cuenta.getIdCuenta()}" />">
                                    <input type="hidden" name="dniCliente" value="<c:out value="${dniCliente}" />">
                                    <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listaCuentas}">
                        <tr>
                            <td colspan="6" class="text-center text-muted">Este cliente no tiene cuentas activas.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>