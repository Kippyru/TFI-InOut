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

- 05062026  
Agregue Customs Cors porq si no Angular le da problemas...  

- 10062026  
Saque la limitacion sql de employee, agrege para que el numero de legajo se haga automaticamente, e hice cambios en el 
mapper de employee y user para ignorar ciertos campos a la hora de actualizar los datos. Tambien se agrego para que 
la fecha de creacion del empleado sea automatica  