package com.stsc4j.generator.bytecode;

public class TypeDescriptor {
    public static String obtener(String tipoQuetzal) {
        if (tipoQuetzal == null) return "V"; // Void por defecto

        // Ajusta los nombres ("entero", "número", etc.) a las palabras exactas que usa tu Token
        switch (tipoQuetzal.toLowerCase()) {
            case "entero": return "I"; // Integer
            case "numero":
            case "decimal": return "F"; // Float
            case "booleano": return "Z"; // Boolean
            case "vacio":
            case "nada": return "V"; // Void
            case "texto":
            case "cadena": return "Ljava/lang/String;"; // Objeto String
            default: return "V";
        }
    }
}