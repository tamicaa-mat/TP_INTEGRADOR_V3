<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Cuenta" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Realizar Transferencia</title>
<!-- Agrega aquí tus links a CSS (Bootstrap, etc.) para que se vea mejor -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
   
     <jsp:include page="masterPage.jsp" /> 

    <div class="container mt-5">
        <h2 class="mb-4">Realizar una Transferencia</h2>

        <%-- Mostrar mensajes de éxito o error --%>
        <% 
            String mensaje = (String) request.getAttribute("mensaje");
            if (mensaje != null) {
        %>
            <div class="alert <%= mensaje.contains("éxito") ? "alert-success" : "alert-danger" %>" role="alert">
                <%= mensaje %>
            </div>
        <% 
            }
        %>

        <div class="card">
            <div class="card-body">
                <form action="TransferenciaServlet" method="post">
                    <%-- Usamos un campo oculto para indicar la acción al servlet --%>
                    <input type="hidden" name="action" value="transferir">

                    <div class="mb-3">
                        <label for="idCuentaOrigen" class="form-label">Seleccione su cuenta de origen:</label>
                        
                        
                        
                        <select class="form-select" id="idCuentaOrigen" name="idCuentaOrigen" required>
                            <option value="">-- Seleccionar cuenta --</option>
                            <% 
                                List<Cuenta> cuentas = (List<Cuenta>) request.getAttribute("cuentas");
                                if (cuentas != null) {
                                    for (Cuenta c : cuentas) {
                            %>
                                <option value="<%= c.getIdCuenta() %>">
                                    <%= c.getTipoCuenta().getDescripcion() %> - N° <%= c.getNumeroCuenta() %> (Saldo: $<%= String.format("%.2f", c.getSaldo()) %>)
                                </option>
                            <% 
                                    }
                                }
                            %>
                        </select>
                        
                        
                        
                    </div>

                    <div class="mb-3">
                        <label for="cbuDestino" class="form-label">CBU de la cuenta de destino:</label>
                        <input type="text" class="form-control" id="cbuDestino" name="cbuDestino" placeholder="Ingrese el CBU de 22 dígitos" required pattern="[0-9]{22}" title="El CBU debe contener 22 dígitos numéricos.">
                    </div>

                    <div class="mb-3">
                        <label for="monto" class="form-label">Monto a transferir:</label>
                        <div class="input-group">
                            <span class="input-group-text">$</span>
                            <input type="number" class="form-control" id="monto" name="monto" step="0.01" min="0.01" placeholder="0.00" required>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary">Transferir</button>
                </form>
            </div>
        </div>
    </div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
