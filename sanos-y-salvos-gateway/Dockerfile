# Usamos una imagen oficial de Java 21
FROM eclipse-temurin:21-jdk-alpine

# Definimos un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo .jar compilado al contenedor
COPY target/*.jar app.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]