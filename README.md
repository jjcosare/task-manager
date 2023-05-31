Requirements:

* Java 17
* Gradle
* Docker

---

To run the application:

```gradle
./gradlew clean build composeUp
```

this will run docker compose and set up the environment and once done you may access the application on:

swagger api docs: http://localhost:8080/docs/index.html

---

To access test coverage on sonarqube:

```gradle clean pitest sonar
```

sonarqube: http://localhost:9000/dashboard?id=com.jjcosare%3Atask

---

To stop the application:

```gradle
./gradlew composeDown && docker rmi jjcosare-spring-boot-postgres:latest
```
