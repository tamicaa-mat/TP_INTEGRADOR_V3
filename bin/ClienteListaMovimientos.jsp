<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
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
        <div class="container mb-4">
            <form method="get" action="CLIENTElistaMovimientos.jsp" class="row g-3 align-items-end">
                <div class="col-md-4">
                    <label for="cuenta" class="form-label">Cuenta</label>
                    <select name="cuenta" id="cuenta" class="form-select">
                        <option value="">Todas</option>
                        <option value="CA001">CA001-123456</option>
                        <option value="CA002">CA002-654321</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="tipo" class="form-label">Tipo de Movimiento</label>
                    <select name="tipo" id="tipo" class="form-select">
                        <option value="">Todos</option>
                        <option value="Transferencia">Transferencia</option>
                        <option value="Pago Préstamo">Pago Préstamo</option>
                        <option value="Depósito">Depósito</option>
                        <option value="Extracción">Extracción</option>
                        <option value="Cobro">Cobro</option>
                        <option value="Débito Automático">Débito Automático</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-secondary w-100">Filtrar</button>
                </div>
            </form>
        </div>
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
                    <tr>
                        <td>2025-06-14 10:30</td>
                        <td>Alta de Cuenta: CA001-123456</td>
                        <td>$10,000.00</td>
                        <td>Depósito</td>
                    </tr>
                    <tr>
                        <td>2025-06-15 09:00</td>
                        <td>Transferencia a CA002-654321</td>
                        <td>-$2,000.00</td>
                        <td>Débito</td>
                    </tr>
                    <tr>
                        <td>2025-06-16 12:45</td>
                        <td>Pago de servicio</td>
                        <td>-$1,200.00</td>
                        <td>Débito</td>
                    </tr>
                    <tr>
                        <td>2025-06-17 14:00</td>
                        <td>Transferencia recibida de CA003-789456</td>
                        <td>$3,500.00</td>
                        <td>Crédito</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
