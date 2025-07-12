<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Bienvenido - Sistema Bancario UTN</title>
    <%-- Los estilos de Bootstrap ya están en la masterPage, pero es buena práctica tenerlos por si acaso --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .welcome-banner {
            position: relative;
            color: white;
            text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.7);
        }
        .welcome-banner img {
            width: 100%;
            height: 400px;
            object-fit: cover; /* Esto hace que la imagen cubra el espacio sin deformarse */
            border-radius: .3rem;
        }
        .welcome-banner .overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(to top, rgba(0,0,0,0.6), rgba(0,0,0,0.1));
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
        }
    </style>
</head>
<body>
    
    <jsp:include page="masterPage.jsp" />

  
    <main class="container mt-4">
        <div class="welcome-banner">
           
            <img src="https://picsum.photos/id/20/1200/400" alt="Banner del banco">
            
            <div class="overlay p-5">
                
                <h1 class="display-4 fw-bold">¡Bienvenido/a, <c:out value="${sessionScope.clienteLogueado.nombre}" />!</h1>
                <p class="fs-4">Gestioná tus cuentas y finanzas de forma fácil y segura.</p>
                <hr class="my-4" style="width: 50%; border-top: 1px solid white;">
                <p>¿Qué te gustaría hacer hoy?</p>
                <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/MovimientoServlet" role="button">Ver mis movimientos</a>
            </div>
        </div>
        
        <div class="row text-center mt-5">
             <div class="col-md-4">
                <h3><a href="${pageContext.request.contextPath}/TransferenciaServlet" class="text-decoration-none text-dark">Transferencias</a></h3>
                <p>Enviá y recibí dinero al instante entre tus cuentas o a otros clientes del banco.</p>
             </div>
             <div class="col-md-4">
                <h3><a href="${pageContext.request.contextPath}/SolicitarPrestamoServlet" class="text-decoration-none text-dark">Préstamos</a></h3>
                <p>Solicitá un préstamo personal con las mejores tasas y planes de financiación.</p>
             </div>
             <div class="col-md-4">
                 <h3><a href="CLIENTEpagoPrestamos.jsp" class="text-decoration-none text-dark">Pagos</a></h3>
                 <p>Consultá el estado de tus préstamos y realizá el pago de tus cuotas fácilmente.</p>
             </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
