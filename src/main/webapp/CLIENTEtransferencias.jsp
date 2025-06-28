<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Transferencias</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
      body {
            background-color: #f8f9fa;
        }
        .transferencias-container {
            margin-top: 30px;
            margin-bottom: 50px;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .table {
            margin-bottom: 0;
        }
        .table thead th {
            background-color: #343a40;
            color: white;
            font-weight: 500;
            white-space: nowrap;
        }
        .table td {
            vertical-align: middle;
        }
        .estado-transferencia {
            font-weight: 600;
            white-space: nowrap;
        }
        .estado-completada {
            color: #28a745;
        }
        .estado-pendiente {
            color: #ffc107;
        }
        .estado-rechazada {
            color: #dc3545;
        }
        .monto-transferencia {
            font-weight: 600;
            color: #212529;
            white-space: nowrap;
        }
        .cbu-numero {
            font-family: monospace;
            font-size: 0.85em;
            word-break: break-all;
        }
        .tipo-cuenta {
            font-size: 0.75em;
            color: #6c757d;
        }
        .fecha-hora {
            white-space: nowrap;
        }
    </style>
</head>
<body>
	<jsp:include page="masterPage.jsp" />
    <main class="container py-5">
    <div class="container">
        <div class="transferencias-container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="h3 mb-0">Historial de Transferencias</h1>
                <button class="btn btn-secondary btn-sm">Nueva Transferencia</button>
            </div>
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Hora</th>
                            <th>Cuenta Origen</th>
                            <th>CBU Destino</th>
                            <th>Monto</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="fecha-hora">15/06/2023</td>
                            <td class="fecha-hora">10:30</td>
                            <td>
                                <div class="cbu-numero">2850590940090418135201</div>
                                <div class="tipo-cuenta">Caja de Ahorro</div>
                            </td>
                            <td class="cbu-numero">1234567890123456789012</td>
                            <td class="monto-transferencia">$125.000,00</td>
                            <td><span class="estado-transferencia estado-completada">COMPLETADA</span></td>
                        </tr>
                        <tr>
                            <td class="fecha-hora">14/06/2023</td>
                            <td class="fecha-hora">14:22</td>
                            <td>
                                <div class="cbu-numero">2850590940090418135201</div>
                                <div class="tipo-cuenta">Caja de Ahorro</div>
                            </td>
                            <td class="cbu-numero">9876543210987654321098</td>
                            <td class="monto-transferencia">$75.500,50</td>
                            <td><span class="estado-transferencia estado-completada">COMPLETADA</span></td>
                        </tr>
                        <tr>
                            <td class="fecha-hora">12/06/2023</td>
                            <td class="fecha-hora">09:15</td>
                            <td>
                                <div class="cbu-numero">2850123456789012345678</div>
                                <div class="tipo-cuenta">Cuenta Corriente</div>
                            </td>
                            <td class="cbu-numero">5678901234567890123456</td>
                            <td class="monto-transferencia">$200.000,00</td>
                            <td><span class="estado-transferencia estado-rechazada">RECHAZADA</span></td>
                        </tr>
                        <tr>
                            <td class="fecha-hora">10/06/2023</td>
                            <td class="fecha-hora">16:45</td>
                            <td>
                                <div class="cbu-numero">2850590940090418135201</div>
                                <div class="tipo-cuenta">Caja de Ahorro</div>
                            </td>
                            <td class="cbu-numero">3456789012345678901234</td>
                            <td class="monto-transferencia">$50.000,00</td>
                            <td><span class="estado-transferencia estado-completada">COMPLETADA</span></td>
                        </tr>
                        <tr>
                            <td class="fecha-hora">05/06/2023</td>
                            <td class="fecha-hora">11:05</td>
                            <td>
                                <div class="cbu-numero">2850987654321098765432</div>
                                <div class="tipo-cuenta">Cuenta Corriente</div>
                            </td>
                            <td class="cbu-numero">7890123456789012345678</td>
                            <td class="monto-transferencia">$175.025,75</td>
                            <td><span class="estado-transferencia estado-pendiente">PENDIENTE</span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <nav aria-label="Page navigation" class="mt-3">
			    <ul class="pagination pagination-sm justify-content-center">
			        <li class="page-item disabled">
			            <a class="page-link text-secondary" href="#" tabindex="-1">‹</a>
			        </li>
			        <li class="page-item active"><a class="page-link bg-secondary border-secondary text-white" href="#">1</a></li>
			        <li class="page-item"><a class="page-link text-secondary" href="#">2</a></li>
			        <li class="page-item"><a class="page-link text-secondary" href="#">3</a></li>
			        <li class="page-item">
			            <a class="page-link text-secondary" href="#">›</a>
			        </li>
			    </ul>
			</nav>
        </div>
    </div>
        </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>


</body>
</html>