<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de Préstamos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
	<jsp:include page="masterPage.jsp" />
	<main class="container py-5">
	    <h4 class="text-center mb-4">Listado de Préstamos</h4>
	    <div class="table-responsive">
	        <table class="table table-bordered table-hover align-middle text-center">
	            <thead class="table-secondary">
	                <tr>
	                    <th>ID</th>
	                    <th>Cliente</th>
	                    <th>Cuenta Asociada pruebaa git</th>
	                    <th>Fecha Alta</th>
	                    <th>Importe Pedido</th>
	                    <th>Plazo (meses)</th>
	                    <th>Importe por Mes</th>
	                    <th>Interés</th>
	                    <th>Estado</th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr>
	                    <td>1</td>
	                    <td>Juan Pérez</td>
	                    <td>CA001-123456</td>
	                    <td>2025-06-14</td>
	                    <td>$100,000</td>
	                    <td>12</td>
	                    <td>$9,500</td>
	                    <td>14%</td>
	                    <td>
	                        <button class="btn btn-success btn-sm me-1">Aprobar</button>
	                        <button class="btn btn-danger btn-sm">Rechazar</button>
	                    </td>
	                </tr>
	                <tr>
	                    <td>2</td>
	                    <td>María López</td>
	                    <td>CA001-654321</td>
	                    <td>2025-05-01</td>
	                    <td>$80,000</td>
	                    <td>24</td>
	                    <td>$4,200</td>
	                    <td>12%</td>
	                    <td class="text-success fw-bold">Aprobado</td>
	                </tr>
	                <tr>
	                    <td>3</td>
	                    <td>Pedro Gómez</td>
	                    <td>CA002-789012</td>
	                    <td>2025-04-10</td>
	                    <td>$50,000</td>
	                    <td>6</td>
	                    <td>$9,000</td>
	                    <td>10%</td>
	                    <td class="text-danger fw-bold">Rechazado</td>
	                </tr>
	            </tbody>
	        </table>
	    </div>
	</main>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>