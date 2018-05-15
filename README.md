# BankSlips Controler
	This rest application provides methods to create, find, findAll, pay and cancel bankSlips.

# Rest Methods
	All the rest methods are security! 
	To call the methods are need to access "/login", with example below, get the response Header "Authorization" and put this into the Header of anothers methods.
	/Login request Example: {"username":"admin","password":"password"}

###Get token
POST /login
	
###Create BankSlip
POST /bankslips

	Request Example:{"dueDate":"2018-05-01","customer":"TESTE 111","totalInCents":100000, "status":"PENDING"}

###Get all BankSlips
GET /bankslips

###Get BankSlips detail
GET /bankslips/{id}

###Cancel a BankSlip
DELETE /bankslips/{id}/cancel

###Pay a BankSlip
PUT /bankslips/{id}/pay

# Utils Links

##GitHub address
	https://github.com/marcelocastilho/bankslips.git

##Swagger address
	http://localhost:8080/swagger-ui.html

##Actuator Address
	Beans information
	http://localhost:8080/actuator/info
	Server health information
	http://localhost:8080/actuator/health
	
##H2 Data Base address
	http://localhost:8080/h2 \n
	Saved Settings:Generic H2 (Embedded) \n
	Setting Name:Generic H2 (Embedded) \n
	Driver Class:org.h2.Driver \n
	JDBC URL:jdbc:h2:~/bankSlipsTest \n
	User: sa \n
	Password: <blank>

# Running the application
	1 - In the application path "bankslips\bankslips-rest" run command mvn package.
	2 - In the application path "bankslips\bankslips-rest\target" run the command java -jar bankslips-rest-0.0.1-SNAPSHOT-exec.jar

### Obs.:
	
	For data tranformation(JPA --> DTO / DTO --> JPA) this application is using ModdelMapper jar. 
	
	For security rules this application is using inMemoryAuthentication