Aca pongo los test a los endpoints que hice:  

**TEST a ROLE**  
POST  
localhost:8080/api/roles  
BODY:  
{
"name": "Admin"
}  
/  
{
"name": "Employee"
}

GET  
localhost:8080/api/roles/list  


**TEST USER**  
POST  
localhost:8080/api/users  
BODY:  
{
"username": "Administrador",
"password": "1234",
"role": "1",
"state": "Activo"
}  

GET  
localhost:8080/api/users/list  

**TEST EMPLOYEE**  
POST  
localhost:8080/api/employees  
BODY:  
{
"name": "John",
"lastName": "Spring",
"role": "2",
"numberEmployee": "1234",
"cuil": "20123456784",
"dni": "12345678"
}  

**TEST EVENTATTENDANCE**  
POST  
localhost:8080/api/attendance/1?device=PC-ADMIN  

Si no hay eventos hace un CHECK_IN  
Si encuentra que el último evento es CHECK_IN, entonces hace un CHECK_OUT