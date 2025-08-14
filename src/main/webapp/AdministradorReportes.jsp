<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Reportes del Sistema Bancario</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="CSS/estilos.css">
</head>
<body class="bg-light">
	<jsp:include page="masterPage.jsp" />
	
	
	
	
	<main class="container py-5">
    <h3 class="text-center mb-4">Reportes del Sistema Bancario</h3>

    <ul class="nav nav-tabs" id="reportesTab" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link ${empty activeTab || activeTab == 'cuentas' ? 'active' : ''}" id="cuentas-tab" data-bs-toggle="tab" data-bs-target="#cuentas-tab-pane" type="button" role="tab">Cuentas y Saldos</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link ${activeTab == 'prestamos' ? 'active' : ''}" id="prestamos-tab" data-bs-toggle="tab" data-bs-target="#prestamos-tab-pane" type="button" role="tab">Préstamos</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link ${activeTab == 'morosidad' ? 'active' : ''}" id="morosidad-tab" data-bs-toggle="tab" data-bs-target="#morosidad-tab-pane" type="button" role="tab">Morosidad</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link ${activeTab == 'vip' ? 'active' : ''}" id="vip-tab" data-bs-toggle="tab" data-bs-target="#vip-tab-pane" type="button" role="tab">Clientes VIP</button>
        </li>
    </ul>

    <div class="tab-content card" id="reportesTabContent">
        
        <div class="tab-pane fade p-4 ${empty activeTab || activeTab == 'cuentas' ? 'show active' : ''}" id="cuentas-tab-pane" role="tabpanel">
            <h5 class="mb-3">Cuentas creadas y Saldo Total</h5>
            <form method="get" action="CuentaServlet" class="row g-3 align-items-end">
                <input type="hidden" name="action" value="reporteCuentas" />
                <div class="col-md-4">
                    <label for="fechaDesde" class="form-label">Desde</label> <input
                        type="date" name="fechaInicio" id="fechaDesde"
                        class="form-control" required>
                </div>
                <div class="col-md-4">
                    <label for="fechaHasta" class="form-label">Hasta</label> <input
                        type="date" name="fechaFin" id="fechaHasta" class="form-control"
                        required>
                </div>
                <div class="col-md-4 d-flex justify-content-center">
                    <button type="submit" class="btn btn-primary btn-sm w-50">Buscar</button>
                </div>
            </form>
            
            <c:if test="${not empty totalCuentas}">
                <div class="mt-4">
                    <h6>Resultado</h6>
                    <table class="table table-bordered text-center align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Período</th>
                                <th>Cantidad de Cuentas</th>
                                <th>Saldo Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>${fechaDesde} a ${fechaHasta}</td>
                                <td>${totalCuentas}</td>
                                <td><fmt:formatNumber value="${saldoTotalCuentas}" type="currency" currencySymbol="$ " /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>

        <div class="tab-pane fade p-4 ${activeTab == 'prestamos' ? 'show active' : ''}" id="prestamos-tab-pane" role="tabpanel">
             <h5 class="mb-3">Cantidad de Préstamos y Total Prestado</h5>
             <form method="get" action="PrestamoServlet" class="row g-3 align-items-end">
                <input type="hidden" name="action" value="reportePrestamos" />
                <div class="col-md-4">
                    <label for="fechaInicioPrestamo" class="form-label">Desde</label> <input
                        type="date" name="fechaInicioPrestamo" id="fechaInicioPrestamo"
                        class="form-control" required>
                </div>
                <div class="col-md-4">
                    <label for="fechaFinPrestamo" class="form-label">Hasta</label> <input
                        type="date" name="fechaFinPrestamo" id="fechaFinPrestamo"
                        class="form-control" required>
                </div>
                <div class="col-md-4 d-flex justify-content-center">
                    <button type="submit" class="btn btn-success btn-sm w-50">Consultar</button>
                </div>
            </form>
            
            <c:if test="${not empty cantidadPrestamos}">
                <div class="mt-4">
                    <h6>Resultado</h6>
                    <table class="table table-bordered text-center align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Período</th>
                                <th>Cantidad de Préstamos</th>
                                <th>Total Prestado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>${fechaInicioPrestamo} a ${fechaFinPrestamo}</td>
                                <td>${cantidadPrestamos}</td>
                                <td><fmt:formatNumber value="${totalPrestamos}" type="currency" currencySymbol="$ " /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>

        <div class="tab-pane fade p-4 ${activeTab == 'morosidad' ? 'show active' : ''}" id="morosidad-tab-pane" role="tabpanel">
            <h5 class="mb-3">Reporte de Morosidad de Préstamos</h5>
            <form method="get" action="ReporteServlet">
                <input type="hidden" name="action" value="reporteMorosidad" />
                <p>Busca todas las cuotas de préstamos que no han sido pagadas y cuya fecha de vencimiento ya pasó.</p>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-danger">Generar Reporte de Morosos</button>
                </div>
            </form>
        
            <c:if test="${not empty listaMorosos}">
                <div class="mt-4">
                    <h6 class="text-danger">Resultado: Clientes con Cuotas Vencidas</h6>
                    <table class="table table-bordered text-center align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Cliente</th>
                                <th>DNI</th>
                                <th>N° Préstamo</th>
                                <th>N° Cuota</th>
                                <th>Monto Cuota</th>
                                <th>Fecha de Vencimiento</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cuota" items="${listaMorosos}">
                                <tr>
                                    <td>${cuota.prestamo.cliente.nombre} ${cuota.prestamo.cliente.apellido}</td>
                                    <td>${cuota.prestamo.cliente.dni}</td>
                                    <td>${cuota.prestamo.idPrestamo}</td>
                                    <td>${cuota.numeroCuota}</td>
                                    <td><fmt:formatNumber value="${cuota.monto}" type="currency" currencySymbol="$ " /></td>
                                    <td><fmt:formatDate value="${cuota.fechaVencimiento}" pattern="dd/MM/yyyy" /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>

        <div class="tab-pane fade p-4 ${activeTab == 'vip' ? 'show active' : ''}" id="vip-tab-pane" role="tabpanel">
            <h5 class="mb-3">Top 10 Clientes con Mayor Saldo</h5>
            <form method="get" action="ReporteServlet">
                <input type="hidden" name="action" value="topClientes" />
                <p>Este reporte muestra los 10 clientes con el mayor saldo combinado en todas sus cuentas activas.</p>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-info">Generar Reporte VIP</button>
                </div>
            </form>
        
            <c:if test="${not empty listaTopClientes}">
                 <div class="mt-4">
                    <h6 class="text-info">Resultado: Top 10 Clientes por Saldo</h6>
                    <table class="table table-bordered text-center align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Ranking</th>
                                <th>Nombre del Cliente</th>
                                <th>DNI</th>
                                <th>Saldo Total Acumulado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cliente" items="${listaTopClientes}" varStatus="loop">
                                <tr>
                                    <td><strong>#${loop.count}</strong></td>
                                    <td>${cliente.nombre} ${cliente.apellido}</td>
                                    <td>${cliente.dni}</td>
                                    <td><fmt:formatNumber value="${cliente.saldoTotal}" type="currency" currencySymbol="$ " /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
    </div>
</main>
	
	
	
	
	
	
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>