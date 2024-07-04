package com.mycompany.practica_programada_2;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class DirectoriosPorHilos extends Thread {

    private final String directorio;
    private final List<Reporte> reportes;

    public DirectoriosPorHilos(String directorio, List<Reporte> reportes) {
        this.directorio = directorio;
        this.reportes = reportes;
    }

    @Override
    public void run() {// override del meotod run que hereda de thread
        try {
            Files.walk(Paths.get(directorio)).filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".txt")).forEach(path -> {
                // aca se toman unicamente los archivos y no los directorios por decirlo de una manera y unicamnte se toman los que son un txt de esta maner evitamos entrar en los cvs y png 
                Reporte reporte = extraerInformacionReporte(path.toFile());// se crea un reporte 
                if (reporte != null) {// si no es nulo osea si no es vacio 
                    synchronized (reportes) {// bloque temporalmente el acceso a la ruta para hacerlo de una manera sincronizada y de esta manera evitar la condicin de carrera 
                        reportes.add(reporte);// se anade el reporte a la lista 
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // se recibe un archivo como parametro
    private static Reporte extraerInformacionReporte(File file) {// mediante esta funcino se extrae el reporte final 
        Reporte reporte = new Reporte();// se crea un nuevo reporte
        reporte.fecha = obtenerFechaDelNombre(file.getName());// la fecha se optiene del nombre previamente acortado en la clase Practica_programada_2

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // creamos un buffer que se usa para recorrer el acrchivo
            String linea;// delcaramos una variable tipo string con la cual iremos "recorriendo"
            boolean esHallazgo = false; // se crea una variable boleana para saber si ya llegamos a los hallazgos
            while ((linea = reader.readLine()) != null) {// se recorre el texto o el txt hasta llegar al final o mientras sea distinta a null osea que la linea tenga texto 
                if (linea.startsWith("# Topico del reporte")) {// aca timamos la infromacion si la linea empieza con el fromato establecido
                    reporte.topico = reader.readLine().trim();// setiamos el topico y con trim eliminamos espacios en blanco 
                } else if (linea.startsWith("# Hallazgos")) {// encambio si empieza con el fromato para los hallasgos 
                    esHallazgo = true;
                } else if (esHallazgo && linea.startsWith("- ")) {// aca se toma cada hallaszo y se empieza 2 espacios despues para tomar solo el contenido 
                    reporte.hallazgos.add(linea.substring(2).trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // Validar que el reporte tenga los campos necesarios
        if (reporte.fecha != null && reporte.topico != null && !reporte.hallazgos.isEmpty()) {
            return reporte;// si tienen los campos necesarios se devuelve el reporte de no ser asi se devuelve un obejto nulo
        } else {
            return null;
        }
    }

    private static String obtenerFechaDelNombre(String nombreArchivo) {
        if (nombreArchivo.startsWith("reporte_") && nombreArchivo.endsWith(".txt")) {// aca nos aseguramos de estar trabajando unicamnte lo archivos que nos interesa
            return nombreArchivo.substring(8, 18); //aca devolvemos unicamnete laparte de titulo del reporque nos interesa que es la fecha
        }// en caso de no pasar por el if anteriror no se hace nada ( osea es un archivo que no nos interesa) 
        return null;
    }

}
