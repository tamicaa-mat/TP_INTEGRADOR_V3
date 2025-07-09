<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	    <section class="mb-5">
		    <h5 class="mb-3">Cuentas creadas y Saldo Total</h5>
		    <form method="get" action="CuentaServlet" class="row g-3 align-items-end">
		        <input type="hidden" name="action" value="reporteCuentas" />
		        <div class="col-md-4">
		            <label for="fechaDesde" class="form-label">Desde</label>
		            <input type="date" name="fechaInicio" id="fechaDesde" class="form-control" required>
		        </div>
		        <div class="col-md-4">
		            <label for="fechaHasta" class="form-label">Hasta</label>
		            <input type="date" name="fechaFin" id="fechaHasta" class="form-control" required>
		        </div>
		        <div class="col-md-4 d-flex justify-content-center">
		            <button type="submit" class="btn btn-primary btn-sm w-50">Buscar</button>
		        </div>
		    </form>
		    <% 
			    String fechaDesde = (String) request.getAttribute("fechaDesde");
		        String fechaHasta = (String) request.getAttribute("fechaHasta");
		        Integer totalCuentas = (Integer) request.getAttribute("totalCuentas");
		        Double saldoTotalCuentas = (Double) request.getAttribute("saldoTotalCuentas");
		    %>
		    <% if (totalCuentas != null && fechaDesde != null && fechaHasta != null) { %>
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
		                    <td><%= fechaDesde %> a <%= fechaHasta %></td>
		                    <td><%= totalCuentas %></td>
		                    <td>$<%= String.format("%.2f", saldoTotalCuentas) %></td>
		                </tr>
		            </tbody>
		        </table>
		    </div>
		    <% } %>
		</section>
	    <hr>
	    <section>
	        <h5 class="mb-3">Cantidad de Préstamos y Total Prestado</h5>
	        <form method="get" action="PrestamoServlet" class="row g-3 align-items-end">
	            <input type="hidden" name="action" value="reportePrestamos" />
	            <div class="col-md-4">
	                <label for="fechaInicioPrestamo" class="form-label">Desde</label>
	                <input type="date" name="fechaInicioPrestamo" id="fechaInicioPrestamo" class="form-control" required>
	            </div>
	            <div class="col-md-4">
	                <label for="fechaFinPrestamo" class="form-label">Hasta</label>
	                <input type="date" name="fechaFinPrestamo" id="fechaFinPrestamo" class="form-control" required>
	            </div>
	            <div class="col-md-4 d-flex justify-content-center">
	                <button type="submit" class="btn btn-success btn-sm w-50">Consultar</button>
	            </div>
	        </form>
	        <%
		        String fechaInicioPrestamo = (String) request.getAttribute("fechaInicioPrestamo");
	            String fechaFinPrestamo = (String) request.getAttribute("fechaFinPrestamo");
	            Integer cantidadPrestamos = (Integer) request.getAttribute("cantidadPrestamos");
	            Double totalPrestamos = (Double) request.getAttribute("totalPrestamos");
	        %>
	        <% if (totalPrestamos != null && cantidadPrestamos != null) { %>
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
	                    	<td><%= fechaInicioPrestamo %> a <%= fechaFinPrestamo %></td> 
	                    	<td><%= cantidadPrestamos %></td> 
	                        <td>$<%= String.format("%.2f", totalPrestamos) %></td> 
	                    </tr>
	                </tbody>
	            </table>
	        </div>
	        <% } %>
	    </section>
	</main>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
