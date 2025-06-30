<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dominio.Usuario" %>
<%@ page import="dominio.Cliente" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");

    if (usuario == null || cliente == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Informacion</title>
    <link rel="stylesheet" type="text/css" href="CSS/estilos.css">
 
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    
    
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .datos-container {
            max-width: 800px;
            margin: 30px auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        .datos-header {
            background-color: #2c3e50;
            color: white;
            padding: 20px;
            border-radius: 8px 8px 0 0;
        }
        .datos-body {
            padding: 25px;
        }
        .seccion-titulo {
            color: #2c3e50;
            border-left: 4px solid #3498db;
            padding-left: 10px;
            margin: 25px 0 15px 0;
            font-weight: 600;
        }
        .dato-item {
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }
        .dato-label {
            font-weight: 500;
            color: #7f8c8d;
            font-size: 0.9rem;
            margin-bottom: 5px;
        }
        .dato-valor {
            font-size: 1rem;
            color: #2c3e50;
            font-weight: 400;
        }
        .password-dots {
            letter-spacing: 2px;
            font-size: 1.2rem;
        }
    </style>
</head>
<body>
<jsp:include page="masterPage.jsp" />

<main class="container py-5">
    <div class="container">
        <div class="datos-container">
            <div class="datos-header text-center">
                <h3><i class="bi bi-person-badge"></i> Mis Datos Personales</h3>
            </div>

            <div class="datos-body">
                <h5 class="seccion-titulo">INFORMACIÓN PERSONAL</h5>

                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Nombre</div>
                            <div class="dato-valor"><%= cliente.getNombre() %></div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Apellido</div>
                            <div class="dato-valor"><%= cliente.getApellido() %></div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">DNI</div>
                            <div class="dato-valor"><%= cliente.getDni() %></div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">CUIL/CUIT</div>
                            <div class="dato-valor"><%= cliente.getCuil() %></div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Sexo</div>
                            <div class="dato-valor"><%= cliente.getSexo() %></div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Nacionalidad</div>
                            <div class="dato-valor"><%= cliente.getNacionalidad() %></div>
                        </div>
                    </div>
                </div>

                <div class="dato-item">
                    <div class="dato-label">Fecha de Nacimiento</div>
                    <div class="dato-valor"><%= cliente.getFechaNacimiento() %></div>
                </div>

                <h5 class="seccion-titulo">INFORMACIÓN DE CONTACTO</h5>

                <div class="dato-item">
                    <div class="dato-label">Dirección</div>
                    <div class="dato-valor"><%= cliente.getDireccion() %></div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Localidad</div>
                            <div class="dato-valor"><%= cliente.getLocalidad().getDescripcion() %></div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="dato-item">
                            <div class="dato-label">Teléfono</div>
                            <div class="dato-valor"><%= cliente.getTelefono() %></div>
                        </div>
                    </div>
                </div>

                <div class="dato-item">
                    <div class="dato-label">Correo Electrónico</div>
                    <div class="dato-valor"><%= cliente.getCorreoElectronico() %></div>
                </div>

                <h5 class="seccion-titulo">CAMBIAR CONTRASEÑA</h5>

                <form action="CambiarPasswordServlet" method="post" class="mb-4">
                    <input type="hidden" name="IdUsuario" value="<%= usuario.getIdUsuario() %>">

                    <div class="mb-3">
                        <label class="form-label">Nueva Contraseña</label>
                        <input type="password" name="nuevaPassword" class="form-control" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Actualizar Contraseña</button>
                </form>

             
                <% if (request.getAttribute("mensaje") != null) { %>
                    <div class="alert alert-success text-center mt-3">
                        <%= request.getAttribute("mensaje") %>
                    </div>
                <% } else if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger text-center mt-3">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
