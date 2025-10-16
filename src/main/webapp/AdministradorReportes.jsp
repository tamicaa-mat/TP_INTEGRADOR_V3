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
            
            <c:if test="${not empty totalCuentasNuevas}">
        <div class="mt-4 p-4 border rounded bg-white">
            <h6 class="border-bottom pb-2 mb-3 text-primary">
                Resultados del Período: 
                <span class="fw-normal text-secondary">${fechaDesde} - ${fechaHasta}</span>
            </h6>
            <div class="row">
                <div class="col-md-4 mb-3">
                    <div class="card text-center bg-light">
                        <div class="card-body">
                            <h5 class="card-title fw-bold">Cuentas Nuevas</h5>
                            <p class="card-text display-6 text-success">
                                <c:out value="${totalCuentasNuevas}" />
                            </p>
                            <p class="text-muted small">Creadas en el período</p>
                        </div>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card text-center bg-light">
                        <div class="card-body">
                            <h5 class="card-title fw-bold">Flujo Neto de Capital</h5>
                            <p class="card-text display-6 <c:if test="${flujoNeto >= 0}">text-success</c:if><c:if test="${flujoNeto < 0}">text-danger</c:if>">
                                $<c:out value="${flujoNeto}" />
                            </p>
                            <p class="text-muted small">Ingresos totales - Egresos totales</p>
                        </div>
                    </div>
                </div>

                <div class="col-md-4 mb-3">
                    <div class="card text-center bg-light">
                        <div class="card-body">
                            <h5 class="card-title fw-bold">Saldo Total Activo</h5>
                            <p class="card-text display-6 text-primary">
                                $<c:out value="${saldoTotalActual}" />
                            </p>
                            <p class="text-muted small">Capital total del banco (Hoy)</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
            
        
            
            
            
            
        </div>
        
        
        
        
        
        
        

        <div class="tab-pane fade p-4 ${activeTab == 'prestamos' ? 'show active' : ''}" id="prestamos-tab-pane" role="tabpanel">
             <h5 class="mb-3">Cantidad de Préstamos y Total Prestado</h5>
             
             
             <form method="get" action="ReporteServlet" class="row g-3 align-items-end">
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
			    <div class="mt-4 p-4 border rounded bg-white">
			        <h6 class="border-bottom pb-2 mb-3 text-secondary">
			            Análisis de Cartera (Periodo: ${fechaInicioPrestamo} a ${fechaFinPrestamo})
			        </h6>
			        <div class="row">
			
			            <div class="col-md-3 mb-3">
			                <div class="card text-center bg-light">
			                    <div class="card-body">
			                        <h5 class="card-title fw-bold text-success">Total Desembolsado</h5>
			                        <p class="card-text display-6">
			                            <fmt:formatNumber value="${totalPrestamos}" type="currency" currencySymbol="$ " maxFractionDigits="2" />
			                        </p>
			                        <p class="text-muted small">Nuevos préstamos aprobados (${cantidadPrestamos})</p>
			                    </div>
			                </div>
			            </div>
			
			            <div class="col-md-3 mb-3">
			                <div class="card text-center bg-info-subtle">
			                    <div class="card-body">
			                        <h5 class="card-title fw-bold text-primary">Capital Pendiente</h5>
			                        <p class="card-text display-6">
			                            <fmt:formatNumber value="${capitalPendiente}" type="currency" currencySymbol="$ " maxFractionDigits="2" />
			                        </p>
			                        <p class="text-muted small">Monto restante por cobrar (Total)</p>
			                    </div>
			                </div>
			            </div>
			
			            <div class="col-md-3 mb-3">
			                <div class="card text-center <c:if test="${prestamosMorosos > 0}">bg-danger-subtle</c:if><c:if test="${prestamosMorosos == 0}">bg-success-subtle</c:if>">
			                    <div class="card-body">
			                        <h5 class="card-title fw-bold text-danger">Préstamos Morosos</h5>
			                        <p class="card-text display-6"><c:out value="${prestamosMorosos}" /></p>
			                        <p class="text-muted small">Préstamos con cuotas vencidas</p>
			                    </div>
			                </div>
			            </div>
			
			            <div class="col-md-3 mb-3">
			                <div class="card text-center bg-light">
			                    <div class="card-body">
			                        <h5 class="card-title fw-bold">Tasa de Morosidad</h5>
			                        <p class="card-text display-6">
			                            <c:set var="tasa" value="${(prestamosMorosos / cantidadPrestamos) * 100}" />
			                            <fmt:formatNumber value="${tasa}" pattern="#0.00" />%
			                        </p>
			                        <p class="text-muted small">Morosos / Total Aprobados en periodo</p>
			                    </div>
			                </div>
			            </div>
			        </div>
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