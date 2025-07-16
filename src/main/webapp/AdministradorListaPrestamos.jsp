<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listado de Préstamos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- DataTables CSS -->
    <link rel="stylesheet" type="text/css"
          href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css">
    
    <!-- jQuery y DataTables JS -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
        $('#tablaPrestamos').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.19/i18n/Spanish.json"
            },
            "columnDefs": [
                { "targets": [10], "orderable": false, "searchable": false } // columna acciones sin orden ni búsqueda
            ]
        });
    });
</script>
</head>
<body class="bg-light">
<jsp:include page="masterPage.jsp" />

<main class="container py-5">
    <h4 class="text-center mb-4">Listado de Préstamos</h4>

    <div class="table-responsive">
        <table id="tablaPrestamos" class="display table table-bordered table-hover align-middle text-center">
            <thead class="table-secondary">
            <tr>
                <th>ID</th>
                <th>Cliente</th>
                <th>Cuenta Asociada</th>
                <th>Fecha Alta</th>
                <th>Importe Pedido</th>
                <th>Plazo (meses)</th>
                <th>Importe por Mes</th>
                <th>Interés</th>
                <th>Estado</th>
                <th>Cantidad de cuotas</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${prestamos}">
                <tr>
                    <td>${p.idPrestamo}</td>
                    <td>${p.cliente.nombre} ${p.cliente.apellido}</td>
                    <td>${p.cuentaAsociada.numeroCuenta}</td>
                    <td>${p.fechaAlta}</td>
                    <td>$${p.importePedido}</td>
                    <td>${p.plazoMeses}</td>
                    <td>$${p.importePorMes}</td>
                    <td>${p.interes}%</td>
                    <td>
                        <c:choose>
                            <c:when test="${p.estado == 0}">
                                Pendiente
                            </c:when>
                            <c:when test="${p.estado == 1}">
                                <span class="text-success fw-bold">Aprobado</span>
                            </c:when>
                            <c:when test="${p.estado == 2}">
                                <span class="text-danger fw-bold">Rechazado</span>
                            </c:when>
                            <c:otherwise>
                                Desconocido
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${p.cantidadCuotas}</td>
                    <td>
                        <c:if test="${p.estado == 0}">
                            <form action="PrestamoServlet" method="post" style="display:inline">
                                <input type="hidden" name="idPrestamo" value="${p.idPrestamo}" />
                                <input type="hidden" name="accion" value="aprobar" />
                                <button class="btn btn-success btn-sm me-1">Aprobar</button>
                            </form>
                            <form action="PrestamoServlet" method="post" style="display:inline">
                                <input type="hidden" name="idPrestamo" value="${p.idPrestamo}" />
                                <input type="hidden" name="accion" value="rechazar" />
                                <button class="btn btn-danger btn-sm">Rechazar</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
