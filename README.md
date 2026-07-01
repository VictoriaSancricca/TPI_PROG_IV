# ⚽ Predicta - Mundial 2026

## Trabajo Práctico Integrador - Programación IV

### Universidad Tecnológica Nacional
### Facultad Regional Villa María

---

## Integrantes

- Victoria Sancricca

Profesor:
Juan Pablo Dealbera

---

# Descripción

Predicta es una plataforma web desarrollada para la gestión de pronósticos deportivos del Mundial 2026.

El sistema permite a los usuarios registrarse, realizar predicciones sobre los resultados de los partidos, competir mediante un ranking general y administrar todas las fases del torneo mediante un panel exclusivo para administradores.

El proyecto fue desarrollado utilizando una arquitectura cliente-servidor basada en Spring Boot para el backend y HTML, CSS y JavaScript para el frontend.

---

# Objetivos

El objetivo principal del proyecto es desarrollar una API REST segura y escalable que permita gestionar un sistema completo de pronósticos deportivos, aplicando los conocimientos adquiridos durante la materia Programación IV.

Objetivos específicos:

- Implementar autenticación mediante JWT.
- Gestionar usuarios y roles.
- Administrar equipos participantes.
- Administrar fechas y partidos.
- Registrar pronósticos.
- Calcular automáticamente los puntos obtenidos.
- Mostrar rankings generales.
- Implementar reglas de negocio definidas por el enunciado.
- Desplegar la aplicación en un servidor online.

---

# Tecnologías utilizadas

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- Maven

## Frontend

- HTML5
- CSS3
- JavaScript

## Base de datos

- MySQL

## Herramientas

- Git
- GitHub
- Postman
- Render

---

# Arquitectura

El sistema fue desarrollado utilizando una arquitectura en capas.

Frontend

↓

Controladores REST

↓

Servicios

↓

Repositorios

↓

Base de Datos MySQL

Esta arquitectura permite mantener una correcta separación de responsabilidades, facilitando el mantenimiento y escalabilidad del proyecto.

---

# Funcionalidades

## Autenticación

- Registro de usuarios.
- Inicio de sesión mediante JWT.
- Protección de endpoints.
- Diferenciación entre ADMIN y USER.

---

## Gestión de Equipos

El administrador puede:

- Crear equipos.
- Modificar equipos.
- Eliminar equipos.
- Consultar equipos registrados.

---

## Gestión de Fechas

El administrador puede:

- Crear fechas.
- Modificarlas.
- Eliminarlas.
- Consultarlas.

El estado de cada fecha se actualiza automáticamente según el estado de sus partidos.

---

## Gestión de Partidos

Permite:

- Registrar partidos.
- Modificar partidos.
- Cargar resultados.
- Cambiar estados.
- Consultar encuentros.

Además, el sistema genera automáticamente las fases eliminatorias una vez finalizada cada etapa del torneo.

---

## Pronósticos

Cada usuario puede:

- Crear un pronóstico.
- Modificarlo.
- Consultar su historial.

Los pronósticos solamente pueden modificarse hasta 30 minutos antes del inicio del partido.

---

## Ranking

El sistema calcula automáticamente:

- Puntos obtenidos.
- Cantidad de resultados exactos (plenos).

El ranking se ordena por:

1. Puntos.
2. Plenos.
3. Antigüedad del pronóstico.

---

# Reglas de negocio implementadas

## Bloqueo de pronósticos

Los usuarios no pueden modificar ni crear pronósticos cuando faltan menos de 30 minutos para el inicio del partido.

---

## Resultado exacto

Si el usuario acierta exactamente el marcador obtiene:

- 3 puntos

---

## Tendencia correcta

Si acierta únicamente el ganador o empate obtiene:

- 1 punto

---

## Resultado incorrecto

Si no acierta la tendencia obtiene:

- 0 puntos

---

## Estados del partido

Los partidos atraviesan los siguientes estados:

Por jugar

↓

En juego

↓

Finalizado

---

## Generación automática de fases

Una vez finalizada una etapa del torneo, el sistema genera automáticamente la siguiente fase respetando el reglamento del Mundial.

---

# Seguridad

La aplicación implementa:

- Spring Security.
- JWT.
- BCrypt para almacenamiento de contraseñas.
- Roles ADMIN y USER.
- Protección de endpoints.

---

# Base de Datos

El sistema utiliza MySQL como motor de base de datos.

Principales entidades:

- Usuario
- Equipo
- Fecha
- Partido
- Pronóstico
- Grupo
- MiembroGrupo

Las relaciones fueron implementadas mediante JPA e Hibernate.

---

# Frontend

El frontend fue desarrollado utilizando HTML, CSS y JavaScript puro.

Entre las principales pantallas se encuentran:

- Login
- Registro
- Dashboard
- Equipos
- Fechas
- Partidos
- Pronósticos
- Ranking
- Perfil de Usuario

El diseño fue desarrollado buscando una interfaz moderna, intuitiva y responsive.

---

# Despliegue

El proyecto fue desplegado utilizando Render.

Backend:

(Agregar URL)

Frontend:

(Agregar URL)

---

# Pruebas

Las pruebas de la API fueron realizadas mediante Postman.

Se verificó el correcto funcionamiento de:

- Registro.
- Login.
- Seguridad.
- CRUD de equipos.
- CRUD de partidos.
- CRUD de fechas.
- Pronósticos.
- Ranking.
- Generación automática de fases.

---

# Dificultades encontradas

Durante el desarrollo surgieron distintos desafíos técnicos, entre ellos:

- Implementación de autenticación JWT.
- Restricción temporal para los pronósticos.
- Generación automática de las fases eliminatorias.
- Cálculo del ranking.
- Protección de endpoints según el rol del usuario.
- Despliegue del backend en Render.

Todos estos inconvenientes fueron resueltos mediante la implementación de servicios específicos, validaciones en el backend y una arquitectura organizada en capas.

---

# Conclusiones

El desarrollo de Predicta permitió integrar los principales conocimientos adquiridos durante la materia Programación IV.

Durante el proyecto se aplicaron conceptos relacionados con arquitectura de software, desarrollo de APIs REST, persistencia de datos, seguridad, autenticación mediante JWT y consumo de servicios desde un frontend.

Además, el trabajo permitió comprender la importancia de implementar correctamente las reglas de negocio, mantener una adecuada organización del código y utilizar herramientas modernas para el desarrollo colaborativo y el despliegue de aplicaciones.

Como resultado se obtuvo una plataforma completamente funcional que cumple con los requerimientos planteados en el Trabajo Práctico Integrador y constituye una solución escalable para la gestión de pronósticos deportivos.
