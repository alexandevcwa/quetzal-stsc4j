import com.stsc4j.lexer.LexerContext;
import com.stsc4j.parser.v1.parser.ParserPrincipal;
import com.stsc4j.parser.v1.parser.TokenStream;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.ASTPrinter; // <-- Importamos el printer de tu compañero
import com.stsc4j.semantic.SemanticAnalyzer;
import com.stsc4j.semantic.SemanticError;

import java.util.List;

public class Main {
    public static void main(String[] args) {


        String sourceCode = "texto edad = aaa + 25";


        System.out.println("=== Código fuente en Quetzal ===");
        System.out.println(sourceCode + "\n");

        try {

            // Fase 1: Lexer
            LexerContext context = new LexerContext();
            context.process(sourceCode);

            // Fase 2: Parser
            ParserPrincipal parser = new ParserPrincipal(new TokenStream(context.getTokens()));
            List<Statement> astTree = parser.parse();

            // ==========================================
            // IMPRIMIR EL ÁRBOL SINTÁCTICO (AST)
            // ==========================================
            System.out.println("=== Árbol Sintáctico Abstracto (AST) ===");
            ASTPrinter printer = new ASTPrinter();
            for (Statement statement : astTree) {
                System.out.println(printer.print(statement));
            }
            System.out.println("========================================\n");

            // Fase 3: Análisis Semántico
            System.out.println("=== Iniciando Análisis Semántico ===");
            SemanticAnalyzer semantic = new SemanticAnalyzer();
            semantic.analyze(astTree);

            System.out.println("Análisis Semántico completado sin errores.");

        } catch (SemanticError e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Error en fases previas: " + e.getMessage());
        }
    }



}