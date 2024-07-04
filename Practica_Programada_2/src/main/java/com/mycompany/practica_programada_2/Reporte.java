package com.mycompany.practica_programada_2;

import java.util.ArrayList;
import java.util.List;

public class Reporte {
    String fecha;
    String topico;
    List<String> hallazgos;

    public Reporte() {
        hallazgos = new ArrayList<>();
    }
}
