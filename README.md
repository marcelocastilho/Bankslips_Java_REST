# BankSlips Controler
This rest application provides methods to create, find, findAll, pay and cancel bankSlips.

GitHub address
	https://github.com/marcelocastilho/bankslips.git

Swagger address
http://localhost:8080/swagger-ui.html

Actuator Address
	Beans information
	http://localhost:8080/actuator/info
	Server health information
	http://localhost:8080/actuator/health

# Rest Methods
	All the rest methods are security! To call the methods are need to access /login, with example below:
	Request Example: {"username":"admin","password":"password"}

0 - Get token
POST /login
	
1 - Create BankSlip
POST /bankslips

	Request Example:{"dueDate":"2018-05-01","customer":"TESTE 111","totalInCents":100000, "status":"PENDING"}

2 - Get all BankSlips
GET /bankslips

3 - Get BankSlips detail
GET /bankslips/{id}

4 - Cancel a BankSlip
DELETE /bankslips/{id}/cancel

5 - Pay a BankSlip
PUT /bankslips/{id}/pay

H2 Data Base address
	http://localhost:8080/h2
	Saved Settings:Generic H2 (Embedded)
	Setting Name:Generic H2 (Embedded)
	Driver Class:org.h2.Driver
	JDBC URL:jdbc:h2:~/bankSlipsTest
	User: sa
	Password: <em branco>

*Obs.: For data tranformation(JPA --> DTO / DTO --> JPA) this application  are  using ModdelMapper jar. 