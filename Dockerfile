FROM eclipse-temurin:23.0.1_11-jdk

# Información del puerto
EXPOSE 8080

# Definir directorio raíz de la aplicación
WORKDIR /root

# Instalar Maven directamente
RUN apt-get update && apt-get install -y maven && apt-get clean

# Copiar archivos necesarios
COPY ./pom.xml /root

# Descargar dependencias
RUN mvn dependency:go-offline

# Copiar el resto de los archivos del proyecto
COPY ./src /root/src

# Compilar el proyecto sin ejecutar pruebas
RUN mvn clean install -DskipTests

# Compilar el proyecto
#RUN mvn clean install

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/root/target/evalart-0.0.1-SNAPSHOT.jar"]
