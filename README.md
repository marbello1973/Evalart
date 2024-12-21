# API Documentación

Este proyecto permite gestionar **Franquisias**, **Sucursales**, y **Productos** a través de una API REST. A continuación, se documentan los endpoints disponibles y ejemplos de cómo usarlos.

## **Endpoints**

### **Carpeta Franquisia**

#### **Agregar Franquisia**
- **POST** `http://localhost:8080/franquisia/add`

**Ejemplo de Uso:**
```json
{

  "nombre": "Yamaha Colombia",
  "sucursalDTO": [
    {
      "nombre": "Tornilleria",
      "productoDTO": [
        {
          "nombre": "Tornilleria rosca fina",
          "stock": 40
        }
      ]
    }
  ]
}
```

#### **Listar Todas las Franquisias**
- **GET** `http://localhost:8080/franquisia/all`

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
            "nombre": "franquisia 1 sucursal 1 prodcuto 19 ",
            "stock": 1614
          }
        ]
      },
      {
        "id": 6,
        "nombre": "Sucursal Bogotá",
        "producto": [
          {
            "id": 20,
            "nombre": "productos para franquisia 1 sucursal 6",
            "stock": 110
          }
        ]
      },
      {
        "id": 7,
        "nombre": "Sucursal Barranqulla",
        "producto": [
          {
            "id": 21,
            "nombre": "productos para franquisia 1 sucursal 7",
            "stock": 150
          }
        ]
      }      
    ]
  }
]
```

#### **Actualizar Franquisia**
- **PUT** `http://localhost:8080/franquisia/update/{id}`

**Ejemplo de Uso:**
```json
{
  "nombre": "Franquisia Actualizada"  
}
```

---

### **Carpeta Sucursal**

#### **Agregar Sucursal a una Franquisia**
- **POST** `http://localhost:8080/sucursal/add/frq/{idFranquisia}`

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
      "nombre": "productos para franquisia 8 sucursal 8",
      "stock": 184
    },
    {
      "id": 26,
      "nombre": "productos para franquisia 8 sucursal 9",
      "stock": 189
    }
  ]
}
```

#### **Actualizar Sucursal**
- **PUT** `http://localhost:8080/sucursal/update/frq/{idFranquisia}/sucursal/{idSucursal}`

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
- **POST** `http://localhost:8080/producto/add/frq/{idFranquisia}/suc/{idSucursal}`

**Ejemplo de Uso:**
```json
{
  "nombre": "productos para franquisia 8 sucursal 9",
  "stock": 149
}
```
**Ejemplo de Respuesta:**
```json
{
  "id": 26,
  "nombre": "productos para franquisia 8 sucursal 9",
  "stock": 149
}
```
#### **Eliminar Producto**
- **DELETE** `http://localhost:8080/producto/delete/frq/{idFranquisia}/suc/{idSucursal}/prod/{idProducto}`

**Ejemplo de Respuesta:**
```json
200 OK
Fallido al eliminar el producto
204 No Content
```

#### **Actualizar Producto**
- **PUT** `http://localhost:8080/producto/update/frq/{idFranquisia}/suc/{idSucursal}/prod/{idProducto}`

**Ejemplo de Uso:**
```json
{
  "nombre": "franquisia 3 sucursal 3 prodcuto 13",
  "stock": 1647
}
```
**Ejemplo de Respuesta:**
```json
200 OK
```

#### **Consultar Productos por Stock en una Franquisia**
- **GET** `http://localhost:8080/producto/prodstock/frq/{idFranquisia}`

**Ejemplo de Respuesta:**
```json
[
  {
    "id": 26,
    "nombre": "productos para franquisia 8 sucursal 9",
    "stock": 189
  },
  {
    "id": 25,
    "nombre": "productos para franquisia 8 sucursal 8",
    "stock": 184
  },
  {
    "id": 28,
    "nombre": "productos para franquisia 8 sucursal 9",
    "stock": 149
  },
  {
    "id": 30,
    "nombre": "productos para franquisia 8 sucursal 9",
    "stock": 149
  },
  {
    "id": 27,
    "nombre": "productos para franquisia 8 sucursal 10",
    "stock": 139
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

### **Documentación del API con Swagger**
- **Acceso a Swagger UI:**  
  Visita [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) para interactuar con los endpoints disponibles.


   
