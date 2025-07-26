<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">


<head>
    <meta charset="UTF-8">
    <title>Nueva Transferencia</title>
<jsp:include page="masterPage.jsp" />
<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css"
      href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- jQuery y DataTables JS -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" charset="utf8"
        src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js"></script>
        
        
       <script type="text/javascript">
    $(document).ready(function () {
        $('#tablaTransferencias').DataTable({
            "language": {
                "url": "//cdn.datatables.net/plug-ins/1.10.19/i18n/Spanish.json"
            },
            "columnDefs": [
                { "targets": [], "orderable": false, "searchable": false } // sin acciones en este caso
            ],
            "pageLength": 10
        });
    });
</script> 
        
        
        
</head>


<body class="bg-light">

<div class="container py-5">
    <h2 class="mb-4">Nueva Transferencia</h2>
    
    
<c:if test="${not empty mensajeExito}">
    <div class="alert alert-success">${mensajeExito}</div>
</c:if>

<c:if test="${not empty mensajeError}">
    <div class="alert alert-danger">${mensajeError}</div>
</c:if>


    <form action="TransferenciaServlet" method="post" class="mb-5">
        <div class="mb-3">
            <label for="cuentaOrigen" class="form-label">Cuenta de Origen:</label>
        <select id="cuentaOrigen" name="idCuentaOrigen" required>
    <option value="" disabled selected>Seleccione una cuenta</option>
    <c:forEach var="cuenta" items="${cuentas}">
        <option value="${cuenta.numeroCuenta}">
            ${cuenta.numeroCuenta} - Saldo: $${cuenta.saldo}
        </option>
    </c:forEach>
</select>

        </div>

        <div class="mb-3">
            <label for="cuentaDestino" class="form-label">Cuenta de Destino:</label>
            <input type="text" class="form-control" id="cuentaDestino" name="numeroCuentaDestino" required>
        </div>

        <div class="mb-3">
            <label for="monto" class="form-label">Monto a Transferir:</label>
            <input type="number" step="0.01" class="form-control" id="monto" name="monto" required>
        </div>

        <button type="submit" class="btn btn-info">Realizar Transferencia</button>
    </form>

    <h4 class="mb-3">Historial de Transferencias</h4>
    
    <table id="tablaTransferencias" class="display table table-striped">
    <thead>
        <tr>
            <th>ID</th>
            <th>Cuenta Origen</th>
            <th>Cuenta Destino</th>
            <th>Monto</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="t" items="${transferencias}">
            <tr>
                <td>${t.idTransferencia}</td>
                <td>${t.idCuentaOrigen}</td>
                <td>${t.idCuentaDestino}</td>
                <td>$${t.monto}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
    
    
</div>


</body>
</html>
