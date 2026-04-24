package com.stsc4j.parser;

import com.stsc4j.lexer.LexerContext;
import com.stsc4j.parser.v1.ast.ASTPrinter;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;


import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Ejemplo de código fuente
        String sourceCode = "jsn var formulario_registro = {\n" +
                "    id: \"registro_usuario_2026\",\n" +
                "    titulo: \"Formulario de Registro Complejo\",\n" +
                "    descripcion: \"Captura de información personal, laboral y preferencias\",\n" +
                "    secciones: [\n" +
                "        {\n" +
                "            nombre: \"Datos Personales\",\n" +
                "            campos: [\n" +
                "                {\n" +
                "                    id: \"nombre\",\n" +
                "                    tipo: \"texto\",\n" +
                "                    etiqueta: \"Nombre completo\",\n" +
                "                    validaciones: {\n" +
                "                        requerido: verdadero,\n" +
                "                        longitud_minima: 3,\n" +
                "                        longitud_maxima: 100\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    id: \"fecha_nacimiento\",\n" +
                "                    tipo: \"fecha\",\n" +
                "                    etiqueta: \"Fecha de nacimiento\",\n" +
                "                    validaciones: {\n" +
                "                        requerido: verdadero,\n" +
                "                        formato: \"YYYY-MM-DD\",\n" +
                "                        rango: {\n" +
                "                            min: \"1900-01-01\",\n" +
                "                            max: \"2026-12-31\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    id: \"genero\",\n" +
                "                    tipo: \"seleccion\",\n" +
                "                    etiqueta: \"Género\",\n" +
                "                    opciones: [\"Masculino\", \"Femenino\", \"Otro\", \"Prefiero no decir\"],\n" +
                "                    validaciones: {\n" +
                "                        requerido: verdadero\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            nombre: \"Información Laboral\",\n" +
                "            campos: [\n" +
                "                {\n" +
                "                    id: \"empresa\",\n" +
                "                    tipo: \"texto\",\n" +
                "                    etiqueta: \"Nombre de la empresa\",\n" +
                "                    validaciones: {\n" +
                "                        requerido: falso\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    id: \"puesto\",\n" +
                "                    tipo: \"texto\",\n" +
                "                    etiqueta: \"Puesto de trabajo\",\n" +
                "                    validaciones: {\n" +
                "                        requerido: falso\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    id: \"salario\",\n" +
                "                    tipo: \"numero\",\n" +
                "                    etiqueta: \"Salario mensual\",\n" +
                "                    validaciones: {\n" +
                "                        requerido: falso,\n" +
                "                        min: 0,\n" +
                "                        max: 100000\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            nombre: \"Preferencias\",\n" +
                "            campos: [\n" +
                "                {\n" +
                "                    id: \"suscripcion\",\n" +
                "                    tipo: \"booleano\",\n" +
                "                    etiqueta: \"¿Desea recibir noticias por correo?\",\n" +
                "                    validaciones: {\n" +
                "                        requerido: verdadero\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    id: \"intereses\",\n" +
                "                    tipo: \"lista\",\n" +
                "                    etiqueta: \"Áreas de interés\",\n" +
                "                    opciones: [\"Tecnología\", \"Deportes\", \"Arte\", \"Ciencia\", \"Viajes\"],\n" +
                "                    validaciones: {\n" +
                "                        requerido: falso,\n" +
                "                        max_selecciones: 3\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    metadatos: {\n" +
                "        version: \"1.0\",\n" +
                "        creado_por: \"Sistema Central\",\n" +
                "        fecha_creacion: \"2026-04-11\"\n" +
                "    }\n" +
                "}";

        System.out.println("=== Código fuente ===");
        System.out.println(sourceCode);
        System.out.println();

        // Análisis léxico
        LexerContext context = new LexerContext();
        context.process(sourceCode);

        System.out.println("=== Tokens ===");
        context.getTokens().forEach(System.out::println);
        System.out.println();

        // Análisis sintáctico
        ParserPrincipal principal = new ParserPrincipal(new TokenStream(context.getTokens()));
        List<Statement> astTree = principal.parse();

        System.out.println("=== AST ===");
        ASTPrinter printer = new ASTPrinter();
        for (Statement statement : astTree) {
            System.out.println(printer.print(statement));
        }
        
    }
}
