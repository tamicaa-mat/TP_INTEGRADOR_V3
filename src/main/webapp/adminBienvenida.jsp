<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Administrador - Sistema Bancario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    
    <jsp:include page="masterPage.jsp" />

    
    <main class="container mt-4">

        <%-- Banner  --%>
        <div class="p-5 mb-4 bg-dark text-white rounded-3 shadow-sm">
            <div class="container-fluid py-4">
                <h1 class="display-5 fw-bold">Panel de Administración</h1>
                <p class="col-md-8 fs-4">
                    Bienvenido, <strong><c:out value="${sessionScope.usuarioLogueado.nombreUsuario}" /></strong>.
                    Desde aquí puedes gestionar las operaciones clave del banco.
                </p>
            </div>
        </div>

        <%-- Tarjetas  --%>
        <div class="row">
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Gestión de Clientes</h5>
                        <p class="card-text">Crear, modificar, y consultar la información de los clientes del banco.</p>
                        <a href="${pageContext.request.contextPath}/ClienteServlet" class="btn btn-primary mt-auto">Ir a Clientes</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Autorizar Préstamos</h5>
                        <p class="card-text">Revisar las solicitudes de préstamos pendientes para aprobarlas o rechazarlas.</p>
                        <a href="${pageContext.request.contextPath}/PrestamoServlet" class="btn btn-primary mt-auto">Ir a Préstamos</a>
                    </div>
                </div>
            </div>
             <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">Reportes y Estadísticas</h5>
                        <p class="card-text">Visualizar informes detallados sobre la actividad y el rendimiento del banco.</p>
                        <a href="AdministradorReportes.jsp" class="btn btn-primary mt-auto">Ir a Reportes</a>
                    </div>
                </div>
            </div>
        </div>

    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
