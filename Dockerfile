# our base build image
FROM maven:3.6.0-jdk-8 as maven

# copy the project files
COPY ./pom.xml ./pom.xml

# build all dependencies
RUN mvn dependency:go-offline -B

# copy your other files
COPY ./src ./src

# build for release
RUN mvn package -DskipTests

# our final base image
FROM openjdk:8-jre-alpine

# set deployment directory
WORKDIR /my-project

# copy input and output file
COPY ./CDRFiles/ ./CDRFiles/
COPY ./CDRFiles/CDRs_0002.xml ./CDRFiles/CDRs_0002.xml
COPY ./CDRFiles/CDRs0001.csv ./CDRFiles/CDRs0001.csv
COPY ./CDRFiles/CDRs0003.json ./CDRFiles/CDRs0003.json

COPY ./CDROutput/ ./CDROutput/
COPY ./CDROutput/CDROUT.json ./CDROutput/CDROUT.json


# copy over the built artifact from the maven image
COPY --from=maven target/springboot-rakuten-mysql-1.0.jar ./

# set the startup command to run your binary
CMD ["java", "-jar", "./springboot-rakuten-mysql-1.0.jar"]
