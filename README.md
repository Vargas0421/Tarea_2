# Práctica Programada 2

### Autor:
**Nombre:** Brandon Josué Vargas Moreira  
**ID:** 402640854

### Curso:
**Cliente Servidor**

### Descripción del Proyecto:
Este proyecto consiste en una herramienta para procesar y analizar datos experimentales almacenados en una estructura compleja de directorios. La empresa de investigación científica necesita generar informes completos que resuman los hallazgos de cada mes a partir de archivos de texto específicos. Y extraer informacin de estos a un txt con toda la infromaion centralizada. 

# Problema

Desarrollar un sistema robusto para procesar datos experimentales y generar informes detallados basados en la estructura de directorios proporcionada. Utilizando el procesamiento por medio de hilos de manera sincronizada asegurando el buen funionamiento de los mimsos 

## Funcionalidades

### Análisis de Datos:
- **Procesamiento Eficiente:** Navegar por la estructura de directorios y analizar archivos de texto sin importar su tamaño o formato.
- **Extracción de Información:** Extraer información relevante de los archivos de reporte `reporte_<fecha>.txt`.

### Generación de Informes:
- **Reporte Final:** Generar un único reporte `reporte_hallazgos.txt` que resuma los hallazgos de cada mes.
- **Formato de Informe:** Cada sección del informe debe seguir un formato específico, incluyendo el mes, año, fecha del reporte y hallazgos.

### Multihilo:
- **Procesamiento Paralelo:** Utilizar hilos para procesar cada directorio `<mes>_<año>` de manera simultánea y optimizada.

