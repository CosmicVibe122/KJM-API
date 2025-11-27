# KJM Sports API

API REST básica para la tienda KJM.

## Requisitos
- Java JDK 21 (con `JAVA_HOME` configurado)
- MySQL 8.x en ejecución
- Maven Wrapper (`mvnw.cmd` incluido)

## Endpoints
Base: `/api`

- Usuarios `/usuarios`
	- GET `/api/usuarios`
	- GET `/api/usuarios/{id}`
	- POST `/api/usuarios`
	- POST `/api/usuarios/login`
	- PUT `/api/usuarios/{id}`
	- DELETE `/api/usuarios/{id}`

- Productos `/productos`
	- GET `/api/productos`
	- GET `/api/productos/{id}`
	- POST `/api/productos`
	- PUT `/api/productos/{id}`
	- DELETE `/api/productos/{id}`

- Categorías `/categorias`
	- GET `/api/categorias`
	- GET `/api/categorias/{id}`
	- POST `/api/categorias`
	- PUT `/api/categorias/{id}`
	- DELETE `/api/categorias/{id}`

- Boletas `/boletas`
	- GET `/api/boletas`
	- POST `/api/boletas`
