# Código Problema (Pre-refactorización)

Esta carpeta contiene el código **PROBLEMÁTICO** original entregado en el examen como punto de partida del análisis SOLID en [`../3A-analisis-violaciones.md`](../3A-analisis-violaciones.md).

> El archivo está como `.java.txt` y no `.java` para evitar que Maven intente compilarlo. Vive en otro paquete (`codigo.problema`) que no forma parte del sistema final.

## Por qué se preserva

Sirve como **evidencia pedagógica**: muestra qué se identificó como antiético antes de refactorizar. La refactorización vive en los paquetes `dominio/`, `aplicacion/`, e `infraestructura/` del proyecto Maven.

## Contenido

- [`GestorBiblioteca.java.txt`](./GestorBiblioteca.java.txt) — Clase original con 7 métodos heterogéneos que viola SRP, OCP, DIP e ISP.
