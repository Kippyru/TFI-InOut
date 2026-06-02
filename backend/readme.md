## Configuracion de Inicio
En root del proyecto, osea aca.. donde esta este readme, levantar docker con:  
docker compose up -d

para listar el contenedor y ver el status:  
docker ps -a

pueden probar conectarse por workbench y deberia andar  
user: root  
pass: 1234  
port: 27017

Cuando se hace "docker compose up" se crean solo los roles ADMIN y EMPLOYEE, ademas de un usuario administrador  

Por defecto se crea:
User: admin
Password: 1234

Se puede cambiar en las variables de entorno:
- ADMIN_USERNAME
- ADMIN_PASSWORD

**TO DO:**  
Fixes

**DONE:**  
ARREGLAR Delete  
AGREGAR mas Validaciones  
AGREGAR Autentificador de tokens


## Changelog  
- 25052026  
Cree la clase "BaseEntity" con la lógica del Soft Delete, todas las entidades la heredan  
Eso hace que cualquier "deleteById" haga un UPDATE deleted = true en base de datos sin tocar los servicios  
Tambien agregue un "@Index" en cada tabla apuntando a la columna deleted para que la base de datosse los saltee  
Y un restore para restaurar cualquier cosa que haya sido eliminada  
**IMPORTANTE:** A partir de ahora todos los datos viejos no van a aparecer, pero pueden restaurarse con los nuevos endpoint:  
por ejepmlo para los roles: localhost:8080/api/roles/restore/1

- 31052026  
Agregue el audit, jwt y encriptacion a las passwords, lo ultimo hizo que las passwords ya existentes dejen de andar, aprovechando eso hice
que automaticamente se cree un admin y los roles cuando levantas la app

- 02062026  
Refactorice el softdelete, ahora permite usar LAZY... Excepto en user -> rol. Eso si o si EAGER porq
Spring Security necesita el rol constantemente. Ademas no deberia dar problemas porq rol es un objeto chico y el usuario
tiene un unico rol





