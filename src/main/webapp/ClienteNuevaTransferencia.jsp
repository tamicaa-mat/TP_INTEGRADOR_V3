<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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


    <form action="TransferenciaServlet" method="post" class="mb-5" onsubmit="return validarTransferencia()">
        <div class="mb-3">
            <label for="cuentaOrigen" class="form-label">Cuenta de Origen:</label>
        <select id="cuentaOrigen" name="idCuentaOrigen" required onchange="actualizarValidacion()">
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
            <input type="text" class="form-control" id="cuentaDestino" name="numeroCuentaDestino" required onblur="actualizarValidacion()">
            <div id="mensajeValidacion" class="text-danger mt-1" style="display: none;">
                ⚠️ No puede transferir a sus propias cuentas
            </div>
        </div>

        <div class="mb-3">
            <label for="monto" class="form-label">Monto a Transferir:</label>
            <input type="number" step="0.01" class="form-control" id="monto" name="monto" required>
        </div>

        <button type="submit" class="btn btn-info" id="btnTransferir">Realizar Transferencia</button>
    </form>

    <h4 class="mb-3">Historial de Transferencias</h4>
    
 	
 	
 	<table id="tablaTransferencias" class="display table table-striped">
    <thead>
        <tr>
            <th>Fecha</th>
            <th>Cuenta Origen</th>
            <th>Remitente</th>
            <th>Cuenta Destino</th>
            <th>Destinatario</th>
            <th class="text-end">Monto</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="t" items="${transferencias}">
            <tr>
                <td><fmt:formatDate value="${t.fechaHora}" pattern="dd/MM/yyyy HH:mm" /></td>
                <td>${t.numeroCuentaOrigen}</td>
                <td>${t.nombreClienteOrigen}</td>
                <td>${t.numeroCuentaDestino}</td>
                <td>${t.nombreClienteDestino}</td>
                <td class="text-end"><fmt:formatNumber value="${t.monto}" type="currency" currencySymbol="$ " /></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
 	
 
 
 
    
    
</div>

<script>
// Lista de cuentas propias del cliente (generada desde el servidor)
var cuentasPropias = [
    <c:forEach var="cuenta" items="${cuentas}" varStatus="status">
        "${cuenta.numeroCuenta}"<c:if test="${!status.last}">,</c:if>
    </c:forEach>
];

function actualizarValidacion() {
    var cuentaOrigen = document.getElementById('cuentaOrigen').value;
    var cuentaDestino = document.getElementById('cuentaDestino').value;
    var mensaje = document.getElementById('mensajeValidacion');
    var boton = document.getElementById('btnTransferir');
    
    // Verificar si la cuenta destino es una de las propias
    var esTransferenciaPropia = cuentasPropias.includes(cuentaDestino);
    
    if (esTransferenciaPropia && cuentaDestino !== '') {
        mensaje.style.display = 'block';
        boton.disabled = true;
        boton.classList.add('btn-secondary');
        boton.classList.remove('btn-info');
    } else {
        mensaje.style.display = 'none';
        boton.disabled = false;
        boton.classList.remove('btn-secondary');
        boton.classList.add('btn-info');
    }
}

function validarTransferencia() {
    var cuentaOrigen = document.getElementById('cuentaOrigen').value;
    var cuentaDestino = document.getElementById('cuentaDestino').value;
    
    // Validación final antes del envío
    if (cuentasPropias.includes(cuentaDestino)) {
        alert('No puede realizar transferencias a sus propias cuentas.');
        return false;
    }
    
    if (cuentaOrigen === cuentaDestino) {
        alert('La cuenta de origen y destino no pueden ser la misma.');
        return false;
    }
    
    return true;
}
</script>

</body>
</html>
