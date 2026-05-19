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

**TEST SCHEDULES**  
POST  
localhost:8080/api/schedules  
BODY:  
{
"name": "Mañana",
"state": "Activo",
"hourWork": 8,
"checkInTolerance": 15,
"checkOutTolerance": 15
}  

POST  
localhost:8080/api/schedules/1/details  
BODY:  
{
"day": "LUNES",
"checkIn": "08:00:00",
"checkOut": "16:00:00"
}  

POST  
localhost:8080/api/schedules/assign  
BODY:  
{
"employeeId": 1,
"scheduleId": 1,
"startDate": "2026-05-17",
"endDate": "2026-06-25"
}

**TEST EVENTATTENDANCE**  
POST  
localhost:8080/api/attendance/1?device=PC-ADMIN

Antes: se alternaba CHECK_IN/CHECK_OUT nomás
Ahora: se compara contra horario esperado  
