# API RESTful User Genius

# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Para más información, leer el articulo: [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura - Diagrama de la solución

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.


Este microservicio esta construido con Spring Boot, utilizando Arquitectura Limpia y Hexagonal y como gestor de dependencias
Gradle. El proyecto está basado en Java 17, utiliza Hibernate como ORM y H2 como base de datos en memoria. 
Además, se ha integrado Swagger para la documentación de la API.

## Contenidos

- **Clean Architecture**: Estructura del proyecto siguiendo los principios de la arquitectura limpia y hexagonal.
- **Spring Boot**: Framework principal para el desarrollo del microservicio.
- **H2**: Base de datos en memoria para desarrollo y pruebas.
- **Gradle**: Sistema de construcción y gestión de dependencias.
- **Java 8+**: Versión mínima requerida de Java.
- **Hibernate**: ORM utilizado para la gestión de la persistencia de datos.
- **Swagger**: Herramienta para la documentación y prueba de la API.


## **Patrones de diseño**
- Chain of Responsability
- Builder
- Singleton
- Chain of Responsability

## **Estilo Arquitectónicos**
- Microservices
- Representational State Transfer(REST)

## **Patrones Arquitectónicos**

- Data Transfer Object(DTO)
- Data Access Object(DAO)


## **Requisitos**

- Java 8 o superior
- Gradle 6.x o superior (para construcción y gestión de dependencias)


## **Instalación**

1. **Clonar el repositorio**:

    ```bash
    git clone https://github.com/DavidSegura3/user-genius-api-restful.git
    cd tu_repositorio - ubicación en dónde guardaste el proyecto clonado
    ```

2. **Construir el proyecto**:

    ```bash
    ./gradlew build
    ```

3. **Ejecutar el microservicio**:

    ```bash
    ./gradlew bootRun
    ```

4. **Acceder a la documentación de la API**:

   Una vez que el microservicio esté en ejecución, puedes acceder a la documentación generada por Swagger en la siguiente URL:

    ```
    http://localhost:9001/swagger-ui/index.html
    ```

    ```
    http://localhost:9001/v3/api-docs
    ```

   5. **cURL**:

**Colección postman**

[nisum.postman_collection.json](nisum.postman_collection.json)

       ```
       curl --location 'http://localhost:9001/api/register' \
       --header 'Content-Type: application/json' \
       --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEb2UiLCJhdXRob3JpdGllcyI6IltdIiwidXNlcm5hbWUiOiJEb2UiLCJleHAiOjE3MjM1NTgwMzEsImlhdCI6MTcyMzU1NDQzMX0.apZbRaD4HSQseY1BuHUI_Ecdq-5lt5K3tUIDbw6ZRBc' \
       --data-raw '{
              "username" : "juan",
              "name" : "Juan Rodriguez",
              "admin": true,
              "email" : "juanrodriguezz@example.com",
              "password" : "Juanito03@",
              "phones" : [
                   {
                      "number" : "1111111",
                      "citycode" : "1",
                      "countrycode" : "1"
                   },
                   {
                      "number" : "2222222",
                      "citycode" : "2",
                      "countrycode" : "2"
                   },
                   {
                      "number" : "3333333",
                      "citycode" : "3",
                      "countrycode" : "3"
                   }
              ]
       }'
    ```
Para poder acceder al recurso anterior, primero se debe generar un token bearer y luego pasarlo como header o como Authorization,
aquí esta el paso a paso:
   
5.1. Debemos acceder al siguiente cURL:

```
    curl --location 'http://localhost:9001/login' \
    --header 'Content-Type: application/json' \
    --data '{
        "username" : "Doe",
        "password" : "Password1!"
    }'
```
En la parte de username y password, tenemos los siguientes registros que se cargan por defecto una vez se inicia la aplicación:

```
    ---------------------------------
    |    "username" : "Doe",        |
    |    "password" : "Password1!"  |
    ---------------------------------
    |    "username" : "Smith"       |
    |    "password" : "Password2!"  |
    ---------------------------------
```
Para probar necesita alguno de ellos, luego de eso puedo validar con un usuario nuevo

Si las credenciales son validas, este generara el token y mostrara un mensaje como el siguiente:

![img.png](img.png)

De lo contrario, genera una excepción controlada como la siguiente:

![img_1.png](img_1.png)


Una vez generado el token, se debe asignar al cURL inicial para poder acceder al recurso, sin agregar el bearer token,
mostrara un mensaje como el siguiente:

![img_2.png](img_2.png)


Parra poder acceder al recurso, debe asignar el token generado y pasarlo por **Header** o por **Authorization**


**Por HEADER**
Debe tener en cuenta que al agregarlo como Header, debe antepoder la palabra Bearer y un espacio en el valor
del header.
![img_3.png](img_3.png)


existe un manera más sencilla que la anterior y es hacerlo directamente por Authorization


**Por AUTHORIZATION**
Aquí debe tener en cuenta que el tipo de autorización debe ser **Bearer Token**, seguido de eso en el campo
Token, debe asignar el token generado anteriormente.

![img_4.png](img_4.png)


Una vez creado y asignado el token, por debajo se valida la autenticidad del token y si todo es correcto
deja acceder al recurso y puede crear el usuario que necesite, como lo muestra la siguiente imagen:

![img_5.png](img_5.png)

Como mencione anteriormente, con el usuario nuevo que creemos, también puede generar un token:

Como muestra la imagen anterior, el usuario que creamos tiene las siguientes credenciales:

```
    ---------------------------------
    |    "username" : "juan",       |
    |    "password" : "Juanito03@"  |
    ---------------------------------
```


Las cuales le vamos a pasar al generador de token y me debe devolver un token con dichas credenciales:

![img_6.png](img_6.png)

De igual forma si las credenciales no son validas, debe generar un error como el mencionado anteriormente:

![img_7.png](img_7.png)

## Estructura del Proyecto

- **src/main/java**: Código fuente del microservicio.
    - **com.nisum.api.cl**: Paquete principal del servicio.
        - **infrastructure**
          - **adapters**: Adaptadores para establecer la conexión de la base de datos jpa.
            - **config**: Configuración del servicio para el mapper y la documentación
            - **jpa**: Interfaces para la persistencia de datos.
            - **security**: Implementación de token con jwt y spring security.
              - **config**: Configuración y permisos del token.
              - **enums**: Clase para obtener valores que se usan en varias partes.
              - **filter**: Clase con los filtros de validación del token, parte exitosa y no exitosa.
              - **service**: Implementación del token.
              - **utils**: Utilitaria para usar en varias partes.
          - **entrypoints**: Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.
            - **exceptions**: Manejador de excepciones y clase de configuración para salida controlada del error 
        - **domain**: Interfaces para la persistencia de datos.
          - **exceptions**: Configuración de clase de excepciones para negocio y cliente
            - **model**: Entidades y DTOs.
            - **usecase**: implementa el caso de uso guardar el usuario, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points 
- **src/main/resources**: Recursos y archivos de configuración.
    - **application.yml**: Configuración del microservicio.
- **src/test/java**: Pruebas del microservicio.

## **Configuración**

La configuración del microservicio se encuentra en `src/main/resources/application.yml`. Puedes ajustar la configuración de la base de datos, el puerto del servidor, entre otros parámetros.

## **Contribución**

Las contribuciones son bienvenidas. Por favor, abre un *issue* o envía una *pull request* para cualquier mejora o corrección.