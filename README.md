### DATOS RELEVANTES

#### IDE Utilizado

- **Visual Studio Code:** Para Frontend
- **IntelliJ IDEA:** Para Backend 

#### Version del lenguaje de programacion

- **Backend:**
   * Java: 17
   * Spring Boot: 3.5.10
   * Maven: 3.9.12

- **Frontend:**
   * TypeScript: 4.9.5
   * React: 19.2.4

- **Docker:**
   * Backend Image: eclipse-temurin:17 (Java 17)
   * Frontend Image: node:18-alpine (Node.js 18)
   * Database: MySQL 8.0

#### Lista de pasos para correr la aplicacion

- **Prerequisitos**
Asegurarse de tener instalado Docker y Docker Compose

- **Pasos para la ejecucion**

1. *Levantar los contenedores:*

El proyecto está configurado para funcionar con Docker Compose, lo que simplifica el proceso. Se necesita abrir una terminal en la raiz del proyecto y ejecuta el siguiente comando. Este comando construirá las imágenes de backend y frontend y luego iniciará todos los servicios (base de
datos, backend y frontend).

```console
docker compose up -d --build
```

Espera a que todos los contenedores se inicien correctamente.
Si en este paso ocurre algun problema puede revizar los logs con

```console
docker logs -f {nombre_container}
```

2. *Importar la base de datos:*

Una vez que el contenedor de la base de datos (mysql_db) funcione, se necesita importar
la estructura de la base de datos y los triggers. En la misma terminal de antes ejecuta los siguientes comandos, uno por uno:

```console
docker exec -it mysql_db mysql -u root -p1234 sistema_db < SCRIPTS/sistema_db.sql
docker exec -it mysql_db mysql -u root -p1234 sistema_db < SCRIPTS/trigger.sql
```

3. *Acceder a la aplicación:*
La aplicación debería estar funcionando. Puedes acceder a ella desde el navegador

    * Frontend: http://localhost:3000 (http://localhost:3000)
    * Backend (API): http://localhost:8080 (http://localhost:8080)
    * phpMyAdmin : http://localhost:8085 (http://localhost:8085)

Para detener la aplicación
Dentro de la carpeta raiz poner el siguiente comando.

```console
docker compose stop
```
  
