## BackEnd

Para ver la db desde workbench pueden probar conectarse por: 
user: root    
pass: 1234  
port: 3306

Cuando se hace "docker compose up" se crean solo los roles ADMIN y EMPLOYEE, ademas de un usuario administrador  

Por defecto se crea:
User: admin
Password: 1234

Se puede cambiar en las variables de entorno:
- ADMIN_USERNAME
- ADMIN_PASSWORD

**ChangeLog**  
Para ver cambios consultar en [Changelog](CHANGELOG.md)