<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<title>Gestión de Cuentas</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="CSS/estilos.css" />
</head>
<body class="bg-light">
	<jsp:include page="masterPage.jsp" />

	<main class="container py-5">
	
		<h3 class="mb-3">Gestión de Cuentas para el Cliente: 
		<strong><c:out value="${cliente.nombre}" /></strong>
		<strong><c:out value="${cliente.apellido}" /></strong>
		DNI:
		<strong><c:out value="${cliente.dni	}" /></strong>
		
		
		</h3>

		<div class="card mb-4 shadow-sm">
			<div class="card-header fw-bold bg-primary text-white">Agregar
				Nueva Cuenta</div>
			<div class="card-body">
				<form action="CuentaServlet" method="post"
					class="row g-3 align-items-end">
					<input type="hidden" name="action" value="agregar" /> <input
						type="hidden" name="dniCliente"
						value="<c:out value='${cliente.dni}' />" />

					<div class="col-md-4">
						<label for="idTipoCuenta" class="form-label">Tipo de
							Cuenta</label> <select name="idTipoCuenta" id="idTipoCuenta"
							class="form-select" required>
							<option value="" disabled selected>Seleccione un tipo</option>
							<option value="1">Caja de Ahorro</option>
							<option value="2">Cuenta Corriente</option>
						</select>
					</div>
					<div class="col-md-4">
						<button type="submit" class="btn btn-success w-100">
							Crear Cuenta</button>
					</div>
				</form>

				<!-- Mensajes -->
				<c:if test="${not empty mensajeExitoCuenta}">
					<div class="alert alert-success mt-3 fw-bold text-center"
						role="alert">
						<c:out value="${mensajeExitoCuenta}" />
					</div>
				</c:if>

				<c:if test="${not empty errorLimiteCuentas}">
					<div class="alert alert-danger mt-3 text-center" role="alert">
						<c:out value="${errorLimiteCuentas}" />
					</div>
				</c:if>
			</div>
		</div>

		<h4 class="text-center mb-4">Cuentas Activas del Cliente</h4>

		<div class="table-responsive shadow-sm rounded">
			<table
				class="table table-bordered table-hover align-middle text-center mb-0">
				<thead class="table-secondary text-uppercase">
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
					<c:choose>
						<c:when test="${not empty listaCuentas}">
							<c:forEach var="cuenta" items="${listaCuentas}">
								<tr>
									<td><c:out value="${cuenta.numeroCuenta}" /></td>
									<td><c:out value="${cuenta.tipoCuentaObjeto.descripcion}" /></td>
									<td><c:out value="${cuenta.cbu}" /></td>
									<td><c:out value="${cuenta.fechaCreacionFormateada}" /></td>
									<td class="text-end">$ <fmt:formatNumber
											value="${cuenta.saldo}" type="currency" currencySymbol=""
											minFractionDigits="2" />
									</td>
									<td><c:if test="${cuenta.estado}">
											<a
												href="CuentaServlet?action=eliminar&idCuenta=${cuenta.idCuenta}&dni=${cliente.dni}"
												onclick="return confirm('¿Está seguro de que desea eliminar esta cuenta?')">
												Eliminar </a>
										</c:if> <c:if test="${!cuenta.estado}">
											<span class="text-muted">Cuenta Inactiva</span>
										</c:if></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="6" class="text-center text-muted fst-italic">
									Este cliente no tiene cuentas activas.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</main>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>