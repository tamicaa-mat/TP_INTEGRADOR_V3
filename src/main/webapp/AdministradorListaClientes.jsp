<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Clientes</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">
    <jsp:include page="masterPage.jsp" />

    <main class="container py-5">
        <h4 class="text-center mb-4">Gestión de Clientes (ABML)</h4>
        <div class="alert alert-success text-center" style="display: none;" role="alert">
            Se agregó/modificó/eliminó correctamente.
        </div>
        <div class="alert alert-danger text-center" style="display: none;" role="alert">
            Hubo un error al realizar la operación.
        </div>
        <div class="text-end mb-4">
            <a href="clientesFormulario.jsp?action=agregar" class="btn btn-secondary btn-sm">Agregar Nuevo Cliente</a>
        </div>
        <form method="get" action="ClienteServlet" class="row g-3 align-items-end mb-4">
            <div class="col-md-6">
                <input type="text" class="form-control" id="searchCliente" name="searchCliente" placeholder="Buscar Cliente (Nombre / DNI / CUIL)">
            </div>
            <div class="col-md-4">
                <label for="filterProvincia" class="form-label">Filtrar por Provincia</label>
                <select id="filterProvincia" name="filterProvincia" class="form-select">
                    <option value="">Todas</option>
                    <option value="Buenos Aires">Buenos Aires</option>
                    <option value="Cordoba">Córdoba</option>
                </select>
            </div>
            <div class="col-md-2 text-center">
                <button type="submit" class="btn btn-secondary w-100">Filtrar</button>
            </div>
        </form>
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-secondary">
                    <tr>
                        <th>ID</th>
                        <th>DNI</th>
                        <th>Nombre Completo</th>
                        <th>Correo</th>
                        <th>Teléfono</th>
                        <th>Usuario Acceso</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>1</td>
                        <td>12345678</td>
                        <td>Juan Pérez</td>
                        <td>juan.perez@example.com</td>
                        <td>1122334455</td>
                        <td>jperez</td>
                        <td>
                            <a href="ClienteServlet?action=editar&id=1" class="btn btn-success btn-sm me-2">Editar</a>
                            <a href="ClienteServlet?action=eliminar&id=1" class="btn btn-danger btn-sm" onclick="return confirm('¿Está seguro que desea eliminar este cliente?');">Eliminar</a>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>87654321</td>
                        <td>María Gómez</td>
                        <td>maria.gomez@example.com</td>
                        <td>1199887766</td>
                        <td>mgomez</td>
                        <td>
                            <a href="ClienteServlet?action=editar&id=2" class="btn btn-success btn-sm me-2">Editar</a>
                            <a href="ClienteServlet?action=eliminar&id=2" class="btn btn-danger btn-sm" onclick="return confirm('¿Está seguro que desea eliminar este cliente?');">Eliminar</a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <nav aria-label="Paginación de clientes" class="mt-4">
		    <ul class="pagination justify-content-center">
		        <li class="page-item">
		            <a class="btn btn-secondary btn-sm me-1" href="#">&laquo;</a>
		        </li>
		        <li class="page-item active">
		            <a class="btn btn-secondary btn-sm me-1" href="#">1</a>
		        </li>
		        <li class="page-item">
		            <a class="btn btn-outline-secondary btn-sm me-1" href="#">2</a>
		        </li>
		        <li class="page-item">
		            <a class="btn btn-outline-secondary btn-sm me-1" href="#">3</a>
		        </li>
		        <li class="page-item">
		            <a class="btn btn-secondary btn-sm" href="#">&raquo;</a>
		        </li>
		    </ul>
		</nav>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
