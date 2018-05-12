# Controle de boleto bancário
API do sistema de controle de boleto com Java e Spring Boot.

Endereço do gitHub
	https://github.com/marcelocastilho/bankslips.git

Endereço do banco de dados H2	
http://localhost:8080/h2

Endereço do Swagger
	http://localhost:8080/swagger-ui.html

Endereço do Actuator 

Dados dos beans
http://localhost:8080/actuator/info

Dados do servidor
http://localhost:8080/actuator/health

Rest Methods

1 - Create BankSlip
POST /bankslips
{"dueDate":"2018-05-01","customer":"TESTE 111","totalInCents":100000, "status":"PENDING"}

2 - Get all BankSlips
GET /bankslips

3 - Get BankSlips detail
GET /bankslips/{id}

4 - Delete a BankSlip
DELETE /bankslips/{id}/cancel

5 - Pay a BankSlip
PUT /bankslips/{id}/pay


