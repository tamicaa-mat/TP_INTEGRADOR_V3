<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ingresar al sistema</title>
    
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- ✅ Estilos embebidos -->
    <style>
        body {
            background-color: #f8f9fa;
        }

        .card {
            border-radius: 15px;
            border: 1px solid #ccc;
        }

        .btn {
            background-color: #0d6efd;
            color: white;
        }

        .btn:hover {
            background-color: #0b5ed7;
        }
    </style>
</head>

<body class="bg-light">
    <header class="navbar navbar-dark bg-secondary">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1 mx-auto">SISTEMA BANCARIO UTN</span>
        </div>
    </header>

    <main class="container-fluid login-container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
        <div class="col-md-4">
            <div class="card shadow">
                <div class="card-body">
                    <h4 class="card-title text-center mb-4">Ingresar al sistema</h4>
                    <form action="LoginServlet" method="post">
                        <div class="mb-3">
                            <input type="text" class="form-control" id="usuario" name="usuario" placeholder="Usuario" required>
                        </div>
                        <div class="mb-3">
                            <input type="password" class="form-control" id="contrasena" name="contrasena" placeholder="Contraseña" required>
                        </div>
                        <button type="submit" class="btn btn-secondary btn-sm w-50 d-block mx-auto">Ingresar</button>
                    </form>
                    <% if (request.getAttribute("errorLogin") != null) { %>
                        <div class="alert alert-danger text-center mt-3">
                            <%= request.getAttribute("errorLogin") %>
                        </div>
                    <% } %>
                </div>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
