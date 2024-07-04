package com.mycompany.practica_programada_2;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Practica_Programada_2 {
    public static void main(String[] args) {
        List<Reporte> reportes = Collections.synchronizedList(new ArrayList<>());
        // se utiliza la lista de esta manera ya que como el profe menciono el ca clase se debe de usar la sincronizacion para el tema de los hilos ya que 
        // permite que multiples hilos accedan y modifiquen la lista de reportes de manera segura sin causar problemas de concurrencia.

        //creamos un archivo desde el cual podemos empezar la busqueda
        File raiz = new File("C:\\Users\\Brandon VM\\Documents\\NetBeansProjects\\Practica_Programada_2\\bitacora");
        File[] directorios = raiz.listFiles(File::isDirectory);// aca se crea un array con todos los directorios de raiz que es el directorio de bitacora 
        //mediante un metood de la clase File

        if (directorios != null) { // si el directorio no es null o vacio 
            List<Thread> hilos = new ArrayList<>(); // se crea un array llamado hilos
            for (File dir : directorios) { // por cada directorio
                DirectoriosPorHilos procesador = new DirectoriosPorHilos(dir.getAbsolutePath(), reportes);// procesador es una instancia de la clase que hereda de thread
                //medinate dir.getAbsolutePath obteneos la ruta  absoluta y reportes es la lista sincronizada
                Thread hilo = new Thread(procesador);// se crea un hilo
                hilos.add(hilo);// se anade el hilo
                hilo.start();  // se ejecuta mediante el metoo run el cual tiane la informacin de la ejecucio 
            }

            // Nos aseguramos que todos los hilos se ejecuten antes de seguir mediante el metodo .join 
            for (Thread hilo : hilos) {// por cada uno de lo hilos creados 
                try {
                    hilo.join();// se espera a que el hilo termine 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Obtiene la fecha actual y la formatea
            LocalDate fechaActual = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaFormateada = fechaActual.format(formatter);
            String nombreArchivo = "reporte_" + fechaFormateada + ".txt";

            generarReporteFinal(reportes, nombreArchivo);// aca se genera el reporte final 
            imprimirContenidoArchivo(nombreArchivo);// se imprime la información del reporte
        }
    }

    private static void generarReporteFinal(List<Reporte> reportes, String nombreArchivo) {
        List<Reporte> reportesValidos = reportes.stream().filter(reporte -> reporte.fecha != null).collect(Collectors.toList());
        // mediante esta linea creamos una lista con los reportes validos minetras la fecha del mismo no sea nula

        Map<String, List<Reporte>> reportesPorMes = reportesValidos.stream().collect(Collectors.groupingBy(reporte -> reporte.fecha.substring(3, 10)));
        //se crea un mapa en el cual se almacenan una lista por fecha y con el ultimo parametro tomamos la parte de la fecha que nos importa y pasamos de 06_11_2023 a 11_2023
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            for (Map.Entry<String, List<Reporte>> entry : reportesPorMes.entrySet()) {// básicamente este for recorre el mapa por cada entrada que sería por fecha del mes y el año
                String mesYear = entry.getKey(); // acá guardamos el valor de cada entrada que sería el mes y el año basado en lo que guardamos en el mapa en la linea 47
                List<Reporte> reportesDelMes = entry.getValue();

                writer.write(getMes(mesYear.substring(0, 2)) + " " + mesYear.substring(3));// acá mediante el substring tomamos la parte de mesYear que ocupamos por ejemplo
                // al tomar del caracter 0 al 2 y usar la función de getMes tomamos los primeros 2 dígitos y los pasamos al mes correspondiente 
                writer.newLine();// esto es un salto de linea para el formato deseado 

                for (Reporte reporte : reportesDelMes) {// por cada reporte hacemos lo siguiente 
                    writer.write("# Reporte del " + reporte.fecha.replace('_', '/')); // de la fecha sustraemos los caracteres que no nos interesan 
                    writer.newLine(); //salto de línea
                    writer.write("# Tópico del reporte"); //escribimos tópico del reporte de manera "manual"
                    writer.newLine(); //salto de línea
                    writer.write(reporte.topico); //de la información guardada en el mapa tomamos el tópico 
                    writer.newLine(); // salto de línea
                    writer.write("# Hallazgos"); //escribimos hallazgos del reporte de manera "manual"
                    writer.newLine(); //salto de línea
                    for (String hallazgo : reporte.hallazgos) { //este for recorre los hallazgos 
                        writer.write("- " + hallazgo); //ponemos un guion medio y + el hallazgo 
                        writer.newLine(); //salto de línea
                    } // aclarar que la información del reporte y los hallazgos se toma de la clase DirectoriosPorHilo que es donde se analizan los archivos por así decirlo 
                    writer.newLine(); //salto de línea
                }
                writer.newLine(); //salto de línea entre cada reporte
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void imprimirContenidoArchivo(String rutaArchivo) { // acá se imprime el contenido del archivo de los hallazgos 
        //se pide el parámetro de la ruta del archivo deseado 
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {// se instancia un BufferedReader llamado reader para leer el archivo 
            String linea;
            while ((linea = reader.readLine()) != null) {// mientras el dato no sea null, o sea se lee línea por línea hasta el final 
                System.out.println(linea);//se imprime la línea
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getMes(String mes) { // este método es para pasar los meses de formato numeral a palabras por decirlo de una manera 
        switch (mes) {
            case "01": return "Enero";
            case "02": return "Febrero";
            case "03": return "Marzo";
            case "04": return "Abril";
            case "05": return "Mayo";
            case "06": return "Junio";
            case "07": return "Julio";
            case "08": return "Agosto";
            case "09": return "Septiembre";
            case "10": return "Octubre";
            case "11": return "Noviembre";
            case "12": return "Diciembre";
            default: return "Mes desconocido (revisar la sintaxis del archivo)";
        }
    }
}
