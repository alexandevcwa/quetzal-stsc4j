package com.stsc4j.lexer;

public class Main {
    public static void main(String[] args) {
        final LexerContext context = new LexerContext();

        System.out.println("=============CODE============");

        String code = "entero sumar_entero(entero a, entero b) {\n" +
                "    retornar a + b\n" +
                "// hHOLA \n" +
                "// hHOLA\n" +
                "}\n" +
                "\n" +
                "objeto Usuario {\n" +
                "    privado:\n" +
                "        texto nombre\n" +
                "        entero edad\n" +
                "        libre número var saldo = 0\n" +
                "    publico:\n" +
                "        Usuario(texto nombre, entero edad) {\n" +
                "            ambiente.nombre = nombre\n" +
                "            ambiente.edad = edad\n" +
                "        }\n" +
                "\n" +
                "        texto obtener_nombre() {\n" +
                "            retornar ambiente.nombre\n" +
                "        }\n" +
                "\n" +
                "        entero obtener_edad() {\n" +
                "            retornar ambiente.edad\n" +
                "        }\n" +
                "}\n" +
                "\n" +
                "// Instancia de objeto\n" +
                "Usuario instancia_usuario = nuevo Usuario(\"Juan\", 30)\n" +
                "\n" +
                "// Declaración variable\n" +
                "texto saludo = \"Hola, Quetzal!\"\n" +
                "\n" +
                "exportar {\n" +
                "    // Exportar función\n" +
                "    sumar_entero,\n" +
                "    // Exportar definición de objeto\n" +
                "    Usuario,\n" +
                "    // Exportar instancia de objeto\n" +
                "    instancia_usuario,\n" +
                "    // Exportar variable\n" +
                "    saludo\n" +
                "}";
        context.process(code);
        context.getTokens().forEach(System.out::println);
        context.cleanToken();
    }
}
