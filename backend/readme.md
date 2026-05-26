en root del proyecto, osea aca.. donde esta este readme, levantar docker con:  
docker compose up -d

para listar el contenedor y ver el status:  
docker ps -a

pueden probar conectarse por workbench y deberia andar  
user: root  
pass: 1234  
port: 27017


**TO DO:**  
AGREGAR Autentificador de tokens

**DONE:**  
ARREGLAR Delete
AGREGAR mas Validaciones 


**Changelog**  
- 25052026  
Cree la clase "BaseEntity" con la lógica del Soft Delete, todas las entidades la heredan  
Eso hace que cualquier "deleteById" haga un UPDATE deleted = true en base de datos sin tocar los servicios  
Tambien agregue un "@Index" en cada tabla apuntando a la columna deleted para que la base de datosse los saltee  
Y un restore para restaurar cualquier cosa que haya sido eliminada  
**IMPORTANTE:** A partir de ahora todos los datos viejos no van a aparecer, pero pueden restaurarse con los nuevos endpoint:  
por ejepmlo para los roles: localhost:8080/api/roles/restore/1
