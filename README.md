# API Documentación

Este proyecto permite gestionar **Franquisias**, **Sucursales**, y **Productos** a través de una API REST. A continuación, se documentan los endpoints disponibles y ejemplos de cómo usarlos.

## Despliegue de la Aplicación desde un Editor

A continuación se explican los pasos para desplegar la aplicación en tu entorno local utilizando un editor de código como **IntelliJ IDEA** o **Visual Studio Code**.

### Requisitos previos

Antes de desplegar la aplicación, asegúrate de tener instalados los siguientes componentes:

- **Java 17 o superior**: La aplicación está desarrollada utilizando Spring Boot, Java 23 o superior.
- **IDE compatible**:
  - **IntelliJ IDEA** (se recomienda la versión Community o Ultimate).
  - **Visual Studio Code** (con los plugins de Java instalados).
- **MySQL**: Asegúrate de tener MySQL instalado y configurado en tu máquina local o en un servidor accesible.
- **Maven**: La aplicación utiliza Maven como herramienta de construcción.

---

### Despliegue en IntelliJ IDEA

1. **Abrir el proyecto en IntelliJ IDEA**:
   - Abre IntelliJ IDEA.
   - Haz clic en "Open" y selecciona el directorio del proyecto.

2. **Configurar la conexión a la base de datos**:
   - Asegúrate de que la base de datos MySQL esté en funcionamiento.
   - Abre el archivo `application.yml` y verifica que las configuraciones de conexión a la base de datos (`url`, `username`, `password`) sean correctas para tu entorno local.
   
3. **Ejecutar la aplicación**:
   - En IntelliJ IDEA, abre la clase principal que contiene el método `main` (por lo general, se encuentra en el paquete raíz de tu proyecto, como `com.evalart.EvalartApplication`).
   - Haz clic derecho en la clase y selecciona **Run 'EvalartApplication'**.
   - IntelliJ IDEA compilará el proyecto y lo ejecutará, lanzando la aplicación en el servidor embebido de Spring Boot en `http://localhost:8080`.

### Despliegue en Visual Studio Code

1. **Abrir el proyecto en Visual Studio Code**:
   - Abre Visual Studio Code.
   - Haz clic en **File > Open Folder** y selecciona el directorio del proyecto.

2. **Instalar las extensiones necesarias**:
   - Si no tienes los plugins de Java, instálalos:
     - Abre la vista de extensiones (`Ctrl+Shift+X` o `Cmd+Shift+X` en Mac).
     - Busca "Java Extension Pack" y haz clic en **Install**.

3. **Configurar la conexión a la base de datos**:
   - Abre el archivo `application.yml` y asegúrate de que las configuraciones de conexión a la base de datos sean correctas.

4. **Ejecutar la aplicación**:
   - Abre el terminal integrado de Visual Studio Code (`Ctrl+` o `Cmd+` en Mac).
   - En el terminal, ejecuta el siguiente comando para compilar y ejecutar la aplicación:

     ```bash
     mvn spring-boot:run
     ```

   - Maven descargará las dependencias necesarias y ejecutará la aplicación, lanzando el servidor de Spring Boot en `http://localhost:8080`.
---
##   **Acceder a la documentación de la API**:
   - Una vez que la aplicación esté en ejecución, puedes acceder a la documentación interactiva de Swagger en `http://localhost:8080/swagger-ui.html`.
---
### Notas adicionales

- **Verificar el puerto de la aplicación**: Si el puerto `8080` está ocupado o deseas cambiarlo, puedes modificar el archivo `application.yml` añadiendo la siguiente configuración:

  ```yaml
  server:
    port: 8081  # Cambia 8081 por el puerto que desees utilizar


## Configuración del archivo `application.yml`

El archivo `application.yml` es donde se definen las configuraciones esenciales para la conexión a la base de datos, la gestión de perfiles y la configuración de seguridad, entre otras.

### Configuración de Perfiles y Base de Datos

```yaml
spring: 
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost/acenture  # URL de la base de datos MySQL
    username: tu-usuario  # Usuario de la base de datos
    password: tu-password  # Contraseña de la base de datos
  jpa:
    show-sql: true  # Muestra las consultas SQL generadas por Hibernate
    properties:
      hibernate:
        format_sql: true  # Formatea el SQL generado para una lectura más fácil
```
  

## **Endpoints**

### **Carpeta Franquisia**

#### **Agregar Franquisia**
- **POST** `http://localhost:8080/franquicia/add`

**Ejemplo de Uso:**
```json
{
 
  "nombre": "Franquisia numero 3",
  "sucursales": []
}
```
**Ejemplo de Respuesta:**
```json
{
  "id": 4,
  "nombre": "Franquisia numero 3",
  "sucursales": []
}
```

#### **Listar Todas las Franquisias**
- **GET** `http://localhost:8080/franquicia/all`

**Ejemplo de Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Country Motors",
    "sucursales": [
      {
        "id": 1,
        "nombre": "Sucursal Cali",
        "producto": [
          {
            "id": 19,
            "nombre": "franquicia 1 sucursal 1 prodcuto 19 ",
            "stock": 1614
          }
        ]
      }                 
    ]
  }
]
```

#### **Actualizar Franquisia**
- **PUT** `http://localhost:8080/franquicia/update/{id}`

**Ejemplo de Uso:**
```json
{
  "nombre": "Franquisia Actualizada"  
}
```

---

### **Carpeta Sucursal**

#### **Agregar Sucursal a una Franquisia**
- **POST** `http://localhost:8080/sucursal/add/frq/{idFranquicia}`

**Ejemplo de Uso:**
```json
{
  "nombre": "Sucursal Norte"  
}
```
**Ejemplo de Respuesta:**
```json
{
  "id": 8,
  "nombre": "Sucursal Norte",
  "producto": null  
}
```

#### **Consultar Sucursal por ID**
- **GET** `http://localhost:8080/sucursal/getSucursal/{idSucursal}`

**Ejemplo de Respuesta:**
```json
{
  "id": 8,
  "nombre": "Tornilleria",
  "producto": [
    {
      "id": 25,
      "nombre": "productos para franquicia 8 sucursal 8",
      "stock": 184
    },
    {
      "id": 26,
      "nombre": "productos para franquicia 8 sucursal 9",
      "stock": 189
    }
  ]
}
```

#### **Actualizar Sucursal**
- **PUT** `http://localhost:8080/sucursal/update/frq/{idFranquicia}/sucursal/{idSucursal}`

**Ejemplo de Uso:**
```json
{
  "nombre": "Sucursal Cucuta"
}
```
**Ejemplo de Respuesta:**
```json
{
  "id": 4,
  "nombre": "Sucursal Cucuta",
  "producto": []
}
```

---

### **Carpeta Productos**

#### **Agregar Producto a una Sucursal**
- **POST** `http://localhost:8080/producto/add/frq/{idFranquicia}/suc/{idSucursal}`

**Ejemplo de Uso:**
```json
{
  "nombre": "productos para franquicia 8 sucursal 9",
  "stock": 149
}
```
**Ejemplo de Respuesta:**
```json
{
  "id": 26,
  "nombre": "productos para franquicia 8 sucursal 9",
  "stock": 149
}
```
#### **Eliminar Producto**
- **DELETE** `http://localhost:8080/producto/delete/frq/{idFranquicia}/suc/{idSucursal}/prod/{idProducto}`

**Ejemplo de Respuesta:**
```json
200 OK
Fallido al eliminar el producto
204 No Content
```

#### **Actualizar Producto**
- **PUT** `http://localhost:8080/producto/update/frq/{idFranquicia}/suc/{idSucursal}/prod/{idProducto}`

**Ejemplo de Uso:**
```json
{
  "nombre": "franquicia 3 sucursal 3 prodcuto 13",
  "stock": 1647
}
```
**Ejemplo de Respuesta:**
```json
200 OK
```

#### **Consultar Productos por Stock en una Franquisia**
- **GET** `http://localhost:8080/producto/prodstock/frq/{idFranquicia}`

**Ejemplo de Respuesta:**
```json
[
  {
    "id": 26,
    "nombre": "productos para franquicia 8 sucursal 9",
    "stock": 189
  },
  {
    "id": 25,
    "nombre": "productos para franquicia 8 sucursal 8",
    "stock": 184
  }
]
```

---

## **Requisitos Previos**

- Tener configurado el entorno de desarrollo con **Java 23** y **Spring Boot**.
- Base de datos configurada (MySQL).

## **Cómo Ejecutar el Proyecto**

1. Clonar el repositorio.
2. Configurar el archivo `application.properties` o `application.yml`.
3. Ejecutar el proyecto con:
   ```bash
   mvn spring-boot:run
---

## Iniciar la Aplicación con Docker

Este proyecto está disponible como una imagen Docker en **Docker Hub**. La imagen necesaria para ejecutar la API es `marbello1973/evalart-api-imag`.

### Paso 1: Descargar la Imagen desde Docker Hub

Para descargar la imagen [https://hub.docker.com/r/marbello1973/evalart-api-imag](https://hub.docker.com/r/marbello1973/evalart-api-imag), ejecuta el siguiente comando en la terminal:

```bash
  docker pull marbello1973/evalart-api-imag:v1
```
---   

### **Documentación del API con Swagger**
- **Acceso a Swagger UI:**  
  Visita [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) para interactuar con los endpoints disponibles.

---
## Uso de Postman para probar la API

Hemos preparado un archivo de colección de Postman que contiene todos los endpoints de la API configurados y listos para usar. Puedes importarlo fácilmente en Postman siguiendo estos pasos:

1. Descarga el archivo [`Accenture.postman_collection.json`](./Accenture.postman_collection.json).
2. Abre Postman.
3. Haz clic en el botón **Importar** ubicado en la parte superior izquierda.
4. Selecciona el archivo descargado (`Accenture.postman_collection.json`) y haz clic en **Abrir**.
5. Una vez importado, verás la colección en el panel lateral izquierdo de Postman, donde podrás probar los endpoints de la API.

### Requisitos
- [Postman](https://www.postman.com/downloads/) instalado en tu máquina.

### Notas
- Asegúrate de que el servidor de la API esté ejecutándose antes de realizar las pruebas.
- Si tu API requiere autenticación, recuerda actualizar los encabezados o las variables necesarias antes de enviar las solicitudes.
---
   
