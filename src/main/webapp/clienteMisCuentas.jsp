<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Cuentas - Sistema Bancario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .card-header-ahorro { background-color: #198754; color: white; }
        .card-header-corriente { background-color: #0d6efd; color: white; }
        .cuenta-info-label { font-weight: 500; color: #6c757d; }
        .cuenta-info-value { font-weight: 600; font-family: monospace; }
        .saldo { font-size: 1.75rem; font-weight: 700; color: #212529; }
    </style>
</head>
<body class="bg-light">
    <jsp:include page="masterPage.jsp" />

    <main class="container py-5">
        <h2 class="mb-4">Resumen de Mis Cuentas</h2>

        <div class="row">
            <%-- Verificamos si la lista de cuentas está vacía --%>
            <c:if test="${empty listaCuentas}">
                <div class="col-12">
                    <div class="alert alert-info" role="alert">
                        Actualmente no tenés ninguna cuenta activa.
                    </div>
                </div>
            </c:if>

            <%-- Iteramos sobre la lista de cuentas y creamos una tarjeta para cada una --%>
            <c:forEach var="cuenta" items="${listaCuentas}">
                <div class="col-md-6 mb-4">
                    <div class="card shadow-sm">
                        <div class="card-header ${cuenta.getTipoCuenta().getDescripcion() == 'Caja de Ahorro' ? 'card-header-ahorro' : 'card-header-corriente'}">
                            <h5 class="mb-0">${cuenta.getTipoCuenta().getDescripcion()}</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <div>
                                    <p class="card-text mb-0 cuenta-info-label">Saldo Actual</p>
                                    <p class="saldo">
                                        <fmt:formatNumber value="${cuenta.getSaldo()}" type="currency" currencySymbol="$ " />
                                    </p>
                                </div>
                                <a href="MovimientoServlet?idCuenta=${cuenta.getIdCuenta()}" class="btn btn-outline-primary btn-sm">Ver Movimientos</a>
                            </div>
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item d-flex justify-content-between">
                                    <span class="cuenta-info-label">N° de Cuenta:</span>
                                    <span class="cuenta-info-value">${cuenta.getNumeroCuenta()}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <span class="cuenta-info-label">CBU:</span>
                                    <span class="cuenta-info-value">${cuenta.getCbu()}</span>
                                </li>
                                <li class="list-group-item d-flex justify-content-between">
                                    <span class="cuenta-info-label">Fecha de Alta:</span>
                                   
                                   <span class="cuenta-info-value">
   									 ${cuenta.getFechaCreacion()}
									</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
