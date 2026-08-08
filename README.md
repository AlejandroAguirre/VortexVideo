
# VortexVideo Backend API

Backend API para plataforma de streaming de videos, el proyecto tiene actuator para poder tener obserbabilidad, por ejemplo con graphana.

La aplicación gestiona:

- Artistas.
- Videos.
- Información multimedia.
- Rutas físicas de archivos.
- Generación de miniaturas.
- Integración con almacenamiento físico de contenido.

---

# Arquitectura

La solución está basada en una arquitectura híbrida:

```text
             +----------------+
             |    Frontend    |
             +----------------+
                     |
                     |
                     v
          +--------------------+
          |  VortexVideo API   |
          |  Spring Boot       |
          +--------------------+
                |        |
                |        |
                v        v

          +---------+   +----------------+
          |  MySQL  |   | Filesystem     |
          | Database|   | Videos/Images  |
          +---------+   +----------------+
```

## Componentes

---

## Backend API

Tecnologías principales:

- Java
- Spring Boot
- Spring Data JPA
- REST API
- Maven

Responsabilidad:

- Exposición de servicios REST.
- Gestión de artistas.
- Gestión de videos.
- Administración del contenido multimedia.
- Comunicación con base de datos.

---

## Base de datos

Motor:

- MySQL

Responsabilidad:

- Artistas.
- Videos.
- Rutas físicas de archivos.
- Información multimedia.
- Metadatos.

La base de datos no almacena los archivos de video.

Únicamente mantiene la información necesaria para localizar y administrar el contenido multimedia.

---

## Almacenamiento físico

Los videos e imágenes se almacenan físicamente en disco.

La aplicación guarda únicamente las rutas de los archivos dentro de MySQL.

Ejemplo:

```text
/media/videos/Artistax/video1.mp4
/media/thumbnails/video1.jpg
```

---

# Estructura de archivos multimedia

Para que el proceso de carga inicial funcione correctamente, los videos deben estar organizados por artista.

Ruta utilizada:

```text
C:\Users\AlejandroAgRa\Videos\4K Video Downloader
```

Estructura requerida:

```text
4K Video Downloader
│
├── Artistax
│   ├── video1.mp4
│   └── video2.mp4
│
├── Artistay
│   ├── video1.mp4
│   └── video2.mp4
│
└── Artistaz
    ├── video1.mp4
    └── video2.mp4
```

El nombre de la carpeta representa el artista.

El proceso de escaneo inicial utiliza esta estructura para:

- Detectar artistas.
- Registrar videos.
- Crear las relaciones necesarias en la base de datos.
- Guardar las rutas físicas de los archivos.

---

# Ejecución Local

## Requisitos

- Java JDK.
- Maven.
- MySQL.
- Configurar variable de entorno de ffmpeg-master(en docker ya no es necesario).

---

## Compilación

Desde la raíz del proyecto:

```bash
mvn clean install
```

---

## Ejecutar aplicación

```bash
java -jar target/VortexVideo-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

---

# Ejecución con Docker

## Construcción de imagen

```bash
docker build -t vortexvideo:1.0 .
```

---

## Ejecutar contenedor
Formato separado:

```bash
docker run -d \
--name vortex-video \
-p 8080:8080 \
-v "C:\Users\AlejandroAgRa\Videos\4K Video Downloader:/media/videos" \
-v "C:\Users\AlejandroAgRa\Videos\Capturasx\thumbnails:/media/thumbnails" \
-e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/streaming_app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" \
-e SPRING_DATASOURCE_USERNAME=alejandro \
-e SPRING_DATASOURCE_PASSWORD=alejandro \
-e MEDIA_SCAN_ROOT=/media/videos \
-e MEDIA_THUMBNAIL_ROOT=/media/thumbnails \
vortexvideo:1.0
```

Formato una sola linea:

```bash
docker run -d --name vortex-video -p 8080:8080 -v "C:\Users\AlejandroAgRa\Videos\4K Video Downloader+:/media/videos" -v "C:\Users\AlejandroAgRa\Videos\Capturasx\thumbnails:/media/thumbnails" -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/streaming_app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" -e SPRING_DATASOURCE_USERNAME=alejandro -e SPRING_DATASOURCE_PASSWORD=alejandro -e MEDIA_SCAN_ROOT=/media/videos -e MEDIA_THUMBNAIL_ROOT=/media/thumbnails vortexvideo:1.0
```

---

# Carga inicial de contenido

Después de iniciar la aplicación es necesario ejecutar los procesos administrativos.

El orden de ejecución es importante:

1. Escaneo de artistas y videos.
2. Generación de miniaturas y previsualizaciones.

---

## 1. Escaneo de contenido multimedia

Este proceso:

- Recorre las carpetas de artistas.
- Detecta videos disponibles.
- Crea registros en MySQL.
- Guarda las rutas físicas.

Ejecutar:

```bash
curl --location --request POST http://localhost:8080/api/vortex/admin/scan
```

---

## 2. Generación de miniaturas

Este proceso:

- Procesa los videos registrados.
- Genera imágenes de preview.
- Guarda las miniaturas en la ubicación configurada.

Ejecutar:

```bash
curl --location --request POST http://localhost:8080/api/vortex/admin/thumbnails
```

---

# Flujo de carga inicial

```text
Videos físicos
      |
      |
      v
Carpetas por artista
      |
      |
      v
Proceso Scan API
      |
      |
      v
Registros en MySQL
      |
      |
      v
Generación de miniaturas
      |
      |
      v
Contenido disponible para streaming
```

---

# Proyecto

## VortexVideo Backend API

Backend para plataforma de streaming multimedia utilizando:

- Spring Boot.
- MySQL.
- Docker.
- Almacenamiento físico de archivos.
- API REST.
