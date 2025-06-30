<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
        <h5 class="mb-3">Clientes nuevos entre fechas</h5>
        <form method="get" action="ReporteClientesServlet" class="row g-3 align-items-end">
            <div class="col-md-4">
                <label for="fechaDesde" class="form-label">Desde</label>
                <input type="date" name="fechaDesde" id="fechaDesde" class="form-control" required>
            </div>
            <div class="col-md-4">
                <label for="fechaHasta" class="form-label">Hasta</label>
                <input type="date" name="fechaHasta" id="fechaHasta" class="form-control" required>
            </div>
            <div class="col-md-4 d-flex justify-content-center">
                <button type="submit" class="btn btn-primary btn-sm w-50">Buscar</button>
            </div>
        </form>
        <div class="mt-4">
            <h6>Resultado</h6>
            <table class="table table-bordered text-center align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Total de Clientes Nuevos</th>
                        <th>Rango de Fechas</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>45</td>
                        <td>01/03/2024 - 01/03/2025</td> 
                    </tr>
                </tbody>
            </table>
        </div>
    </section>
    <hr>
    <section>
        <h5 class="mb-3">Dinero del banco destinado a préstamos por periodo</h5>
        <form method="get" action="ReportePrestamosServlet" class="row g-3 align-items-end">
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
        <div class="mt-4">
            <h6>Resultado</h6>
            <table class="table table-bordered text-center align-middle">
                <thead class="table-light">
                    <tr>
                        <th>Total Prestado</th>
                        <th>Rango de Fechas</th>
                        <th>Cantidad de Préstamos</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>$5.320.000</td> 
                        <td>01/01/2024 - 01/06/2025</td> 
                        <td>32</td> 
                    </tr>
                </tbody>
            </table>
        </div>
    </section>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
