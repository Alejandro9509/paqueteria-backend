# Usa Ubuntu 24.04 como base
FROM ubuntu:24.04

# Establece la zona horaria si es necesario (opcional)
ENV TZ=America/Mexico/Tijuana
RUN apt-get update && apt-get install -y tzdata

# Instala Java (puedes usar OpenJDK 21 para Spring Boot 3)
RUN apt-get update && apt-get install -y openjdk-21-jdk

# Crea un directorio para la app
WORKDIR /app

# Copia el jar al contenedor
COPY target/mi-app.jar app.jar

# Expón el puerto en el que corre tu app (normalmente 8080)
EXPOSE 8091

# Comando para ejecutar la app
CMD ["java", "-jar", "app.jar"]
