<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Pago de Prestamos</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body class="bg-light">
	<jsp:include page="masterPage.jsp" />

	<main class="container py-5">
		<div class="card col-md-6 mx-auto shadow-sm">
			<div class="card-body">
				<h4 class="text-center mb-4">Pago de Prestamos</h4>

				<!-- mensaje de error si existe -->
				<c:if test="${not empty error}">
					<div class="alert alert-danger text-center">${error}</div>
				</c:if>

				<!-- mensaje de éxito si existe -->
				<c:if test="${not empty exito}">
					<div class="alert alert-success text-center">✅ Pago
						procesado correctamente.</div>
				</c:if>

				<!-- ✅ Mostrar info del préstamo si existe -->
				<c:if test="${not empty prestamo}">
					<!-- CORREGIR: Usar lógica basada en cuotas restantes -->
					<c:set var="cuotasPagadas"
						value="${prestamo.plazoMeses - prestamo.cantidadCuotas}" />
					<c:set var="totalPagado"
						value="${prestamo.importePorMes * cuotasPagadas}" />
					<c:set var="saldoRestantePrestamo"
						value="${prestamo.importePorMes * prestamo.cantidadCuotas}" />
					<c:set var="cuotasRestantes" value="${prestamo.cantidadCuotas}" />
					<c:set var="importeTotalPrestamo"
						value="${prestamo.importePorMes * prestamo.plazoMeses}" />

					<div class="alert alert-info text-center mt-3">
						<strong>Importe original recibido:</strong> $
						<fmt:formatNumber value="${prestamo.importePedido}" type="number"
							minFractionDigits="2" />
						<br /> <strong>Importe total a pagar (con interés):</strong> $
						<fmt:formatNumber value="${importeTotalPrestamo}" type="number"
							minFractionDigits="2" />
						<br /> <strong>Cuotas pagadas:</strong> ${cuotasPagadas} de
						${prestamo.plazoMeses} <br /> <strong>Cuotas restantes:</strong>
						${cuotasRestantes} <br /> <strong>Total pagado hasta
							ahora:</strong> $
						<fmt:formatNumber value="${totalPagado}" type="number"
							minFractionDigits="2" />
						<br /> <strong>Saldo restante a pagar:</strong> $
						<fmt:formatNumber value="${saldoRestantePrestamo}" type="number"
							minFractionDigits="2" />
						<br />
						<c:if test="${prestamo.cantidadCuotas == 0}">
							<span class="text-success fw-bold">✅ PRÉSTAMO
								COMPLETAMENTE PAGADO</span>
						</c:if>
					</div>
				</c:if>

				<!-- 🔁 Mostrar formularios SOLO si aún no se procesó el pago -->
				<c:if test="${empty exito}">

					<!-- 🔁 Formulario para seleccionar cuenta -->
					<form action="PagoPrestamoServlet" method="get">
						<label class="form-label">Seleccione cuenta</label> <select
							class="form-select mb-3" name="idCuenta" required
							onchange="this.form.submit()">
							<option value="" disabled selected>Seleccione una cuenta</option>
							<c:forEach var="cuenta" items="${cuentas}">
								<option value="${cuenta.idCuenta}"
									${cuenta.idCuenta == param.idCuenta ? "selected" : ""}>
									${cuenta.numeroCuenta}</option>
							</c:forEach>
						</select>
					</form>

					<!-- 🔁 Formulario para seleccionar préstamo -->
					<c:if test="${not empty prestamos}">
						<form action="PagoPrestamoServlet" method="get">
							<input type="hidden" name="idCuenta" value="${param.idCuenta}" />
							<label class="form-label">Seleccione prestamo</label> <select
								class="form-select mb-3" name="idPrestamo" required
								onchange="this.form.submit()">
								<option value="" disabled selected>Seleccione un
									prestamo</option>
								<c:forEach var="p" items="${prestamos}">
									<option value="${p.idPrestamo}"
										${p.idPrestamo == param.idPrestamo ? "selected" : ""}>
										Prestamo #${p.idPrestamo}</option>
								</c:forEach>
							</select>
						</form>
					</c:if>

					<!-- 🔁 Formulario para pagar cuota -->
					<c:if test="${not empty param.idPrestamo}">
						<c:forEach var="p" items="${prestamos}">
							<c:if test="${p.idPrestamo == param.idPrestamo}">
								<c:choose>
									<c:when test="${p.cantidadCuotas > 0}">
										<!-- Solo mostrar formulario si hay cuotas pendientes -->
										<form action="PagarCuotaServlet" method="get">
											<input type="hidden" name="idCuenta"
												value="${param.idCuenta}" /> <input type="hidden"
												name="idPrestamo" value="${param.idPrestamo}" />
											<div class="mb-3">
												<p>
													<strong>Importe pedido:</strong> $${p.importePedido}
												</p>
												<p>
													<strong>Importe por mes (cuota):</strong>
													$${p.importePorMes}
												</p>
												<p>
													<strong>Interes:</strong> ${p.interes}%
												</p>
												<p>
													<strong>Plazo:</strong> ${p.plazoMeses} meses
												</p>
												<p>
													<strong>Cantidad de cuotas restantes:</strong>
													${p.cantidadCuotas}
												</p>
												<input type="hidden" name="montoPago"
													value="${p.importePorMes}" />
											</div>
											<div class="text-center mt-4">
												<button type="submit" class="btn btn-success">Pagar
													cuota</button>
											</div>
										</form>
									</c:when>
									<c:otherwise>
										<!-- Mostrar mensaje cuando no hay cuotas pendientes -->
										<div class="alert alert-info text-center">
											<h5>✅ Préstamo Completamente Pagado</h5>
											<p>Este préstamo ya ha sido pagado en su totalidad. No
												hay cuotas pendientes.</p>
											<a href="PagoPrestamoServlet" class="btn btn-primary">Volver
												al inicio</a>
										</div>
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:forEach>
					</c:if>

				</c:if>
				<!-- cierre del "if empty exito" -->

			</div>
		</div>
	</main>
</body>
</html>
