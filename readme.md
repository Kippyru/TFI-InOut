TFI  

## Configuracion de Inicio  

El proyecto está dockerizado completo: backend, frontend y base de datos corren cada uno en su propio container.

### Requisitos

- [Docker](https://www.docker.com/) y Docker Compose instalados

### Cómo iniciar el proyecto

1. Clonar el repositorio  
2. En la carpeta raíz del proyecto (`TFI-InOut/`)  
3. Ejecutár en la terminal:  

```bash
docker compose up --build
```

Esto va a:  
- Levantar la base de datos **MariaDB**  
- Compilar y levantar el **[backend](backend/readme.md)** (Spring Boot)  
- Compilar y levantar el **[frontend](frontend/README.md)** (Angular, servido con nginx)  

### Acceso a la aplicación

 Frontend | localhost:4200 |  
 Backend (API) | localhost:8080 |  
 Base de datos (MariaDB) | localhost:3306 |  

### Comandos útiles

```bash
# Iniciar en segundo plano (no ocupa la terminal)  
docker compose up -d

# Ver logs en tiempo real
docker compose logs -f

# Detener todo
docker compose down

# Reconstruir solo un servicio (por ejemplo, tras cambiar el backend)
docker compose up --build backend
```

> **Nota:** solo es necesario usar `--build` cuando se modifica el código, el `Dockerfile` o el `docker-compose.yaml`. Si no hubo cambios, alcanza con `docker compose up`.