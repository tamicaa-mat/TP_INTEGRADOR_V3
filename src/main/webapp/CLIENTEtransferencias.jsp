<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Transferencias</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
    body { background-color: #f8f9fa; }
    .transferencias-container { margin-top: 30px; margin-bottom: 50px; background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
    .table thead th { background-color: #343a40; color: white; font-weight: 500; white-space: nowrap; }
    .table td { vertical-align: middle; }
    .monto-negativo { font-weight: 600; color: #dc3545; white-space: nowrap; }
    .monto-positivo { font-weight: 600; color: #28a745; white-space: nowrap; }
    .cbu-numero { font-family: monospace; font-size: 0.9em; }
    .referencia-detalle { font-size: 0.8em; color: #6c757d; }
    .fecha-hora { white-space: nowrap; }
</style>
</head>
<body>
    <jsp:include page="masterPage.jsp" />
    <main class="container py-5">
        <div class="transferencias-container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h1 class="h3 mb-0">Historial de Movimientos</h1>
               
                <a href="TransferenciaServlet?action=mostrarFormulario" class="btn btn-primary">Realizar Transferencia</a>
            </div>
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Fecha y Hora</th>
                            <th>Tipo</th>
                            <th>Detalle</th>
                            <th>Cuenta Afectada</th>
                            <th>Importe</th>
                        </tr>
                    </thead>
                    <tbody>
                      
                        <c:if test="${empty historialTransferencias}">
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">No hay movimientos para mostrar.</td>
                            </tr>
                        </c:if>
                        
                       
                        <c:forEach var="mov" items="${historialTransferencias}">
                            <tr>
                                <td class="fecha-hora">
                                   
            							${mov.fechaHoraFormateada}  <%-- JSTL llama automáticamente a getFechaHoraFormateada() --%>

                                </td>
                                <td>${mov.getTipoMovimiento().getDescripcion()}</td>
                                <td>
                                    <div class="referencia-detalle">${mov.getReferencia()}</div>
                                </td>
                                <td>
                                    <div class="cbu-numero">${mov.getCuenta().getNumeroCuenta()}</div>
                                </td>
                                <td>
                                    
                                    <span class="${mov.getImporte() < 0 ? 'monto-negativo' : 'monto-positivo'}">
                                        <fmt:formatNumber value="${mov.getImporte()}" type="currency" currencySymbol="$ " />
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <%-- Aquí podrías agregar la lógica de paginación si la implementas --%>
            
            
            
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
