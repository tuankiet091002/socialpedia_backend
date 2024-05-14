FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /home/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY ./src ./src
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f ./pom.xml clean package

FROM build as test
CMD ["./mvnw", "test"]

FROM eclipse-temurin:17-jre-jammy AS production
WORKDIR /home/app
COPY --from=build ./home/app/target/*.jar ./app.jar
CMD ["java","-jar","app.jar"]