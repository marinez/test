FROM maven:3.9-amazoncorretto-17 AS maven_build
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -DskipTests


FROM openjdk
COPY --from=maven_build /tmp/target/super-sesame.jar /data/super-sesame.jar
CMD java -jar /data/super-sesame.jar