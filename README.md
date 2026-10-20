# supermercado-api

API REST para gestionar un supermercado. Hecha con Spring Boot y Java. Sirve para administrar productos, categorias, proveedores, clientes y ventas. Se puede probar con Postman.

## Requisitos

- Java 17 o mas reciente
- Maven
- MySQL (opcional, se puede usar H2 sin instalar nada)
- Postman para probar los endpoints

## Como ejecutar

El proyecto ya viene configurado para usar H2 (base de datos en memoria), asi que puedes arrancarlo sin instalar MySQL:

```
mvn spring-boot:run
```

La API queda en: http://localhost:8081

Si quieres usar MySQL, ejecuta primero el script SQL que esta en la carpeta `database/`:

```
mysql -u root -p < database/supermercado.sql
```

Antes de arrancar, configura la variable de entorno `DB_PASSWORD` con la contraseña de tu usuario MySQL. En PowerShell:

```
$env:DB_PASSWORD="tu-contraseña"
```

Luego arranca con el perfil mysql:

```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=mysql
```

El usuario de MySQL se configura en `src/main/resources/application-mysql.properties`; la contraseña se toma de `DB_PASSWORD` y no se guarda en el repositorio.

## Probar con Postman

Importa el archivo `postman/SupermercadoAPI.postman_collection.json` en Postman.

El orden recomendado para probar es:

1. Crear categorias y proveedores
2. Crear productos (usando los ids que te devuelven)
3. Crear clientes
4. Registrar ventas

La coleccion ya trae ejemplos de peticiones que devuelven errores (400, 404, 409) para que veas como responde la API cuando algo sale mal.

## Endpoints principales

Categorias, proveedores, productos y clientes tienen los mismos CRUD:

- POST /api/categorias - crear
- GET /api/categorias - listar
- GET /api/categorias/{id} - obtener por id
- PUT /api/categorias/{id} - actualizar
- DELETE /api/categorias/{id} - eliminar

Lo mismo para /api/proveedores, /api/productos y /api/clientes.

Endpoints extras de productos:

- GET /api/productos/categoria/{categoriaId}
- GET /api/productos/buscar?nombre=...
- GET /api/productos/stock-bajo?limite=10

Clientes:

- GET /api/clientes/documento/{documento}

Ventas (no tienen PUT, no se actualizan):

- POST /api/ventas - registrar una venta
- GET /api/ventas - listar todas
- GET /api/ventas/{id} - obtener una venta
- GET /api/ventas/cliente/{clienteId} - ventas de un cliente
- GET /api/ventas/fecha?inicio=...&fin=... - ventas en un rango
- DELETE /api/ventas/{id} - eliminar (devuelve el stock)

Ejemplo de cuerpo para registrar una venta:

```json
{
  "clienteId": 1,
  "detalles": [
    { "productoId": 3, "cantidad": 2 },
    { "productoId": 7, "cantidad": 1 }
  ]
}
```

## Codigo de errores que devuelve la API

- 200: todo bien (consultas y actualizaciones)
- 201: se creo algo correctamente
- 204: se elimino correctamente
- 400: datos incorrectos (stock insuficiente, cantidad invalida, etc.)
- 404: no se encontro lo que buscas (producto, cliente, categoria...)
- 409: conflicto (codigo de barras repetido, documento repetido, o tratar de borrar una categoria que tiene productos)
- 500: error inesperado del servidor

## Reglas de negocio incluidas

- El nombre es obligatorio en categorias, proveedores, productos y clientes.
- El precio de un producto debe ser mayor a 0.
- El stock no puede ser negativo.
- El codigo de barras y el documento del cliente no se pueden repetir.
- No se puede eliminar una categoria o proveedor si tiene productos asociados.
- Una venta debe tener al menos un detalle y la cantidad debe ser mayor a 0.
- No se puede vender mas de lo que hay en stock.
- Al registrar una venta se descuenta el stock. Al eliminar una venta se devuelve el stock.
- El precio, subtotal y total los calcula siempre el servidor (el cliente no los envia).

## Notas

Este proyecto lo hice para practicar Spring Boot, JPA y como armar una API REST con capas (controller, service, repository, entity, dto). No tiene autenticacion ni usuarios, cualquier persona puede usar los endpoints.