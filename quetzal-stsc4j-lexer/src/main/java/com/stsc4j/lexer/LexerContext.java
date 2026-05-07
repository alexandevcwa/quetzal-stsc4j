package com.stsc4j.lexer;

import com.stsc4j.lexer.exception.LexerException;
import com.stsc4j.lexer.state.InitialState;
import com.stsc4j.lexer.state.LexerState;

import java.util.ArrayList;
import java.util.List;

/**
 * La clase LexerContext proporciona el contexto y la lógica necesarios para llevar
 * a cabo el análisis léxico de una entrada de texto según un conjunto de reglas
 * específicas del lenguaje.
 * <p>
 * Es responsable de mantener el estado actual del analizador, gestionar el contenido
 * del búfer, almacenar los tokens generados y manejar las transiciones entre estados
 * del analizador mientras se procesa la entrada.
 * <p>
 * El análisis léxico implica identificar unidades significativas en el texto de entrada,
 * como palabras clave, identificadores, literales o símbolos, y generar tokens que las
 * representen. Esta clase, junto con los estados e implementaciones de tokens, constituye
 * un componente clave en un analizador léxico.
 */
public class LexerContext {

    /**
     * Representa el estado actual del analizador léxico (lexer).
     * <p>
     * Este campo es una instancia de {@code LexerState} que determina
     * la lógica de procesamiento que se ejecutará en función del contexto
     * en el que se encuentra el analizador. El estado puede cambiar dinámicamente
     * en función de los caracteres procesados o las reglas específicas del lenguaje
     * que se está analizando.
     * <p>
     * Es utilizado para delegar la lógica de procesamiento de caracteres
     * y para gestionar las transiciones de estado durante el análisis léxico.
     */
    private LexerState state;

    /**
     * Contiene un lexema procesado
     */
    private final StringBuilder buffer = new StringBuilder();

    /**
     * Lista de tokens generados
     */
    private final List<Token> tokens = new ArrayList<>();


    private int line = 1;
    private int column = 0;

    /**
     * Constructor de la clase LexerContext.
     * <p>
     * Este constructor inicializa el estado del analizador léxico con una instancia del estado inicial (InitialState).
     * El contexto del analizador léxico administra el estado actual y almacena los tokens generados
     * durante el procesamiento de la entrada.
     */
    public LexerContext() {
        this.state = new InitialState();
    }

    /**
     * Procesa una cadena de entrada para generar tokens, según las reglas definidas
     * en el estado actual del analizador léxico.
     * Este método identifica y omite los comentarios, delegando a un estado
     * la lógica de procesamiento de caracteres según su posición y contexto.
     *
     * @param input La cadena de entrada que se analizará para generar tokens.
     *              Si es nula o vacía, no se realiza ningún procesamiento.
     */
    public void process(String input) {
        if (input == null || input.isEmpty()) return;

        char[] chars = input.toCharArray();
        int length = chars.length;
        boolean omit = false;

        for (int i = 0; i < length; i++) {
            this.column += 1;
            char currentChar = chars[i];

            if (i + 1 < length) {
                if (isCommentStart(currentChar, chars[i + 1])) {
                    omit = true;
                }
            }

            if (currentChar == '\n') {
                omit = false;
            }

            if (!omit) {
                state.process(currentChar, length, i, this);
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));

        if (buffer.length() > 0) {
            state.finalize(this);
        }
    }

    /**
     * Determina si la combinación de dos caracteres representa el inicio de un comentario
     * en el código fuente. Los comentarios pueden ser de bloque "/*" o de línea "//".
     *
     * @param currentChar El carácter actual que se está evaluando.
     * @param nextChar    El siguiente carácter tras el actual, usado para identificar posibles combinaciones.
     * @return true si los caracteres representan el inicio de un comentario,
     * false en caso contrario.
     */
    private boolean isCommentStart(char currentChar, char nextChar) {
        return currentChar == '/' && (nextChar == '*' || nextChar == '/');
    }

    /**
     * Agrega un carácter al búfer del contexto del analizador léxico.
     * Este método permite construir progresivamente secuencias de caracteres
     * para su posterior análisis o generación de tokens.
     *
     * @param c El carácter que se añadirá al búfer del contexto.
     */
    public void add(char c) {
        buffer.append(c);
    }

    /**
     * Genera un nuevo token basado en el tipo especificado y el contenido actual
     * del búfer, y reinicia el estado interno para procesar nuevos tokens.
     * <p>
     * El método realiza las siguientes operaciones:
     * - Crea un nuevo token con el tipo proporcionado y el contenido del búfer actual.
     * - Agrega el token generado a la lista de tokens en el contexto del analizador léxico.
     * - Limpia el búfer para preparar la acumulación de nuevos caracteres.
     * - Restablece el estado del analizador léxico a su estado inicial.
     *
     * @param type El tipo del token que se generará, representado como {@code TokenType}.
     *             Este parámetro define la categoría semántica del token (por ejemplo,
     *             palabra clave, identificador, símbolo, literal, etc.).
     */
    public void generateToken(TokenType type) {
        if (type == TokenType.UNKNOW) {
            tokens.add(new Token(type, buffer.toString(), line));
            throwContext();
        } else {
            tokens.add(new Token(type, buffer.toString(), line));
            buffer.setLength(0);
            this.state = new InitialState();
        }
    }

    /**
     * Limpia la lista de tokens almacenados en el contexto del analizador léxico.
     * <p>
     * Este método elimina todos los tokens previamente generados y almacenados en el
     * campo `tokens`, dejando la lista vacía. Es útil para reiniciar el contexto o
     * prepararlo para el procesamiento de una nueva entrada.
     */
    public void cleanToken() {
        tokens.clear();
    }

    /**
     * Establece un nuevo estado para el analizador léxico.
     * Este método actualiza el estado actual del contexto del analizador léxico
     * con el estado proporcionado, permitiendo cambiar la lógica de procesamiento
     * según sea necesario.
     *
     * @param newState El nuevo estado que se asignará al analizador léxico.
     *                 Este estado debe implementar la interfaz {@code LexerState}.
     */
    public void setState(LexerState newState) {
        this.state = newState;
    }

    /**
     * Devuelve el estado actual del analizador léxico.
     *
     * @return La instancia de {@code LexerState} que representa el estado actual
     * del analizador léxico.
     */
    public LexerState getState() {
        return state;
    }

    /**
     * Devuelve el contenido actual del búfer del analizador léxico como una cadena.
     *
     * @return Una representación en forma de texto del contenido acumulado en el búfer.
     */
    public String getBuffer() {
        return buffer.toString();
    }

    /**
     * Devuelve la lista de tokens generados y almacenados en el contexto del analizador léxico.
     * Esta lista contiene representaciones de las unidades léxicas identificadas durante
     * el análisis del código fuente.
     *
     * @return Una lista de instancias de {@code Token} que representa los tokens
     * generados durante el análisis léxico.
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Incrementa en uno el valor del número de línea del contexto léxico.
     */
    public void oneMoreLine() {
        this.line += 1;
    }

    /**
     * Lanza una excepción de tipo {@code LexerException} en caso de que se detecte un error
     * durante el proceso de análisis léxico (tokenización) del código fuente.
     *
     * @throws LexerException Sí ocurre un error de tokenización, con un mensaje descriptivo
     *                        del problema detectado.
     */
    public void throwContext() {
        final String codePortion = buildCodePortion();
        final String message = String.format("Error al tokenizar código fuente en la línea %d, columna %d, cerca de ↓↓↓ \n%s", line, column - 1, codePortion);
        throw new LexerException("Tokenization Error", message);
    }

    private String buildCodePortion() {
        int tokenSize = tokens.size();
        String codePortion = null;
        if (tokenSize > 0) {
            int position = tokenSize > 10 ? tokenSize - 11 : 0;

            StringBuilder buff = new StringBuilder();
            buff.append("...");
            Integer currentLine = null;
            for (int i = position; i < tokenSize; i++) {
                if (i == position) {
                    currentLine = tokens.get(i).line;
                }
                if (currentLine != tokens.get(i).line) {
                    buff.append("\n");
                    currentLine = tokens.get(i).line;
                }
                buff.append(tokens.get(i).lexeme).append(" ");
            }
            buff.append("←");
            codePortion = buff.toString();
            codePortion = codePortion.trim();
            buff.setLength(0);
        }
        return codePortion;
    }
}