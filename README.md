Technologies Used :

Spring Boot - Server side framework
MySQL - Database
Swagger - API documentation
Docker - Containerizing framework
JWT - Authentication mechanism for REST APIs
Thymeleaf - Templating engine
Material - UI theming/design
Bootstrap - CSS framework


1. Running the server locally :

maven package or mvn install

To run the Spring Boot app from a command line in a Terminal window you can you the java -jar command. This is provided your Spring Boot app was packaged as an executable jar file.

java -jar target/springboot-starterkit-mysql-1.0.jar

You can also use Maven plugin to run the app.
Maven plugin :

mvn spring-boot:run

mvn install -DskipTests

This will skip the check for availability of mysql instance and create the JAR.

UI Interface :

http://localhost:8080

REST APIs base-path :

http://localhost:8080/api/



2. REST API Specification :

http://localhost:8080/swagger-ui/index.html

You can use the User to execute the login api for generating the Bearer token. The token then should be applied in the "Authorize" popup which will by default apply it to all secured APIs.