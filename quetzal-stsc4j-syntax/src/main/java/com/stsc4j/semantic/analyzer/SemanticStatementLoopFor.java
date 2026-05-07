package com.stsc4j.semantic.analyzer;

import com.stsc4j.parser.v1.ast.Expression;
import com.stsc4j.parser.v1.ast.Statement;
import com.stsc4j.parser.v1.ast.StatementLoopFor;
import com.stsc4j.semantic.Environment;
import com.stsc4j.semantic.SemanticAbstractAnalyzer;
import com.stsc4j.semantic.SemanticError;

public class SemanticStatementLoopFor extends SemanticAbstractAnalyzer {

    private Environment currentEnv;

    public SemanticStatementLoopFor (Environment currentEnv) {
        this.currentEnv = currentEnv;
    }

    @Override
    public String visit(StatementLoopFor statementLoopFor) {
        // TRUCO DE COMPILADORES:
        // Creamos un entorno temporal AQUÍ MISMO para que la variable que se declare
        // en la primera parte del 'para' (ej: entero i = 0) nazca y muera con el ciclo.
        Environment entornoAnterior = this.currentEnv;
        this.currentEnv = new Environment(entornoAnterior);

        try {
            // 1. Analizar la declaración inicial (puede ser un StatementVariable o un ExpressionVariable)
            if (statementLoopFor.declaration instanceof Statement) {
                (statementLoopFor.declaration).accept(this);
            } else if (statementLoopFor.declaration instanceof Expression) {
                (statementLoopFor.declaration).accept(this);
            }

            // 2. Validar que la condición sea booleana
            String tipoCondicion = statementLoopFor.condition.accept(this);
            if (tipoCondicion != null && !tipoCondicion.equals("booleano")) {
                throw new SemanticError("La condición del ciclo 'para' debe ser de tipo 'booleano', pero se encontró un '" + tipoCondicion + "'.");
            }

            // 3. Analizar la expresión de incremento/decremento (ej. i++)
            if (statementLoopFor.increment != null) {
                statementLoopFor.increment.accept(this);
            }

            // 4. Finalmente, analizamos el bloque de código a ejecutar.
            // (Nota: el StatementBlock creará otro sub-entorno, lo cual es perfectamente
            // seguro y mantiene nuestra variable 'i' visible para el bloque).
            statementLoopFor.block.accept(this);

        } finally {
            // Al terminar el ciclo for, destruimos el entorno y la variable iteradora desaparece
            this.currentEnv = entornoAnterior;
        }

        return null;
    }

}
