# 🦅 Quetzal STSC4J Parser

<div align="center">

![Java](https://img.shields.io/badge/Java-11+-orange?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue)
![Version](https://img.shields.io/badge/Version-0.0.1-green)

**Librería nativa Java para análisis sintáctico (Parser) del lenguaje Quetzal**

[Características](#-características) •
[Arquitectura](#-arquitectura) •
[Instalación](#-instalación) •
[Uso](#-uso) •
[Contribución](#-contribución)

</div>

---

## 📋 Descripción

**Quetzal STSC4J Parser** es un módulo del compilador Quetzal encargado del **análisis sintáctico** (parsing). Transforma una secuencia de tokens generados por el lexer en un **Árbol de Sintaxis Abstracta (AST)**, permitiendo el análisis semántico y la generación de código posteriores.

El parser implementa el patrón de diseño **Visitor** para recorrer y procesar el AST de manera extensible y mantenible.

---

## ✨ Características

- 🌳 **Generación de AST** - Construcción de árbol de sintaxis abstracta tipado
- 🔄 **Patrón Visitor** - Arquitectura extensible para procesar nodos del AST
- 📝 **Soporte de tipos** - Manejo de tipos primitivos y genéricos (`lista<entero>`)
- 🎯 **Variables mutables/inmutables** - Distinción entre variables modificables y constantes
- 🧩 **Expresiones binarias** - Soporte para operaciones aritméticas y lógicas
- 📊 **AST Printer** - Visualización del árbol para debugging

---

## 🏗 Arquitectura

El proyecto sigue una arquitectura modular basada en el patrón **Abstract Syntax Tree (AST)** con el patrón **Visitor** para el recorrido del árbol.

### Diagrama General de Paquetes

```mermaid
graph TB
    subgraph "com.stsc4j.parser"
        Main[Main]
    end
    
    subgraph "com.stsc4j.parser.ast"
        ASTNode[ASTNode]
        Expression[Expression]
        Statement[Statement]
        Parser[Parser]
        Visitor[Visitor]
        ASTPrinter[ASTPrinter]
        TypeInfo[TypeInfo]
    end
    
    subgraph "com.stsc4j.parser.declaration"
        VarDeclaration[VarDeclaration]
        FunctionDeclaration[FunctionDeclaration]
        Block[Block]
        ReturnStatement[ReturnStatement]
        BinaryExpression[BinaryExpression]
        LiteralExpression[LiteralExpression]
        ListExpression[ListExpression]
        VariableExpression[VariableExpression]
    end
    
    Main --> Parser
    Main --> ASTPrinter
    Parser --> Statement
    ASTPrinter --> Visitor
    
    style Main fill:#e1f5fe
    style Parser fill:#fff3e0
    style ASTPrinter fill:#f3e5f5
```

---

## 📊 Diagramas de Clases

### 1. Jerarquía Principal del AST

Este diagrama muestra la estructura de herencia fundamental del árbol de sintaxis abstracta.

```mermaid
classDiagram
    class ASTNode {
        <<abstract>>
        #accept(Visitor~R~ visitor)* R
    }
    
    class Expression {
        <<abstract>>
    }
    
    class Statement {
        <<abstract>>
    }
    
    class Visitor~R~ {
        <<interface>>
        +visit(VarDeclaration stmt) R
        +visit(FunctionDeclaration stmt) R
        +visit(ReturnStatement expr) R
        +visit(Block block) R
        +visit(LiteralExpression expr) R
        +visit(ListExpression expr) R
        +visit(BinaryExpression expr) R
        +visit(VariableExpression expr) R
    }
    
    ASTNode <|-- Expression : extends
    ASTNode <|-- Statement : extends
    ASTNode ..> Visitor : uses
```

#### Descripción de Clases Base

| Clase | Tipo | Descripción |
|-------|------|-------------|
| `ASTNode` | Abstract | Clase raíz de todos los nodos del AST. Define el método `accept()` para el patrón Visitor. |
| `Expression` | Abstract | Representa expresiones que producen un valor (literales, operaciones, variables). |
| `Statement` | Abstract | Representa sentencias que ejecutan acciones (declaraciones, bloques, retornos). |
| `Visitor<R>` | Interface | Define operaciones de visita para cada tipo de nodo. Parámetro `R` para tipo de retorno. |

---

### 2. Sentencias (Statements)

Las sentencias representan acciones o declaraciones en el código fuente.

```mermaid
classDiagram
    class Statement {
        <<abstract>>
    }
    
    class VarDeclaration {
        -TypeInfo type
        -boolean isMutable
        -String name
        -Expression initializer
        +getType() TypeInfo
        +isMutable() boolean
        +getName() String
        +getInitializer() Expression
        #accept(Visitor~R~ visitor) R
    }
    
    class FunctionDeclaration {
        -TokenType returnType
        -String name
        -List~VarDeclaration~ parameters
        -Block body
        +getReturnType() TokenType
        +getName() String
        +getParameters() List~VarDeclaration~
        +getBody() Block
        #accept(Visitor~R~ visitor) R
    }
    
    class Block {
        -List~Statement~ statements
        +getStatements() List~Statement~
        #accept(Visitor~R~ visitor) R
    }
    
    class ReturnStatement {
        -Expression expression
        #accept(Visitor~R~ visitor) R
    }
    
    Statement <|-- VarDeclaration : extends
    Statement <|-- FunctionDeclaration : extends
    Statement <|-- Block : extends
    Statement <|-- ReturnStatement : extends
    
    FunctionDeclaration --> Block : body
    FunctionDeclaration --> VarDeclaration : parameters
    Block --> Statement : contains
    ReturnStatement --> Expression : returns
    VarDeclaration --> TypeInfo : type
    VarDeclaration --> Expression : initializer
```

#### Descripción de Sentencias

| Clase | Responsabilidad |
|-------|-----------------|
| `VarDeclaration` | Declaración de variables con tipo, mutabilidad, nombre y valor inicial. Soporta la sintaxis `entero var x = 10`. |
| `FunctionDeclaration` | Declaración de funciones con tipo de retorno, parámetros y cuerpo. Encapsula la definición completa de una función. |
| `Block` | Bloque de código conteniendo múltiples sentencias. Representa el cuerpo de funciones, ciclos y condicionales. |
| `ReturnStatement` | Sentencia de retorno con expresión opcional. Indica el valor de retorno de una función. |

---

### 3. Expresiones (Expressions)

Las expresiones producen valores y pueden anidarse para formar expresiones complejas.

```mermaid
classDiagram
    class Expression {
        <<abstract>>
    }
    
    class LiteralExpression {
        -Object value
        +getValue() Object
        #accept(Visitor~R~ visitor) R
    }
    
    class VariableExpression {
        -String name
        +getName() String
        #accept(Visitor~R~ visitor) R
    }
    
    class BinaryExpression {
        -Expression left
        -Expression right
        -String operator
        +getLeft() Expression
        +getRight() Expression
        +getOperator() String
        #accept(Visitor~R~ visitor) R
    }
    
    class ListExpression {
        -List~Expression~ elements
        +getElements() List~Expression~
        #accept(Visitor~R~ visitor) R
    }
    
    Expression <|-- LiteralExpression : extends
    Expression <|-- VariableExpression : extends
    Expression <|-- BinaryExpression : extends
    Expression <|-- ListExpression : extends
    
    BinaryExpression --> Expression : left
    BinaryExpression --> Expression : right
    ListExpression --> Expression : elements
```

#### Descripción de Expresiones

| Clase | Responsabilidad |
|-------|-----------------|
| `LiteralExpression` | Valores literales: números (`10`, `3.14`), cadenas (`"hola"`), booleanos (`true`, `false`). |
| `VariableExpression` | Referencias a variables por nombre. Permite acceder al valor de variables declaradas. |
| `BinaryExpression` | Operaciones binarias como suma, resta, comparaciones. Contiene operador y dos operandos. |
| `ListExpression` | Expresión de lista literal `[1, 2, 3]`. Contiene una lista de expresiones como elementos. |

---

### 4. Sistema de Tipos

El sistema de tipos soporta tipos primitivos y tipos genéricos (como listas tipadas).

```mermaid
classDiagram
    class TypeInfo {
        -String name
        -TypeInfo genericType
        +TypeInfo(String name, TypeInfo genericType)
        +toString() String
    }
    
    TypeInfo --> TypeInfo : genericType
    
    note for TypeInfo "Tipos soportados:\n- entero (int, long, short)\n- numero (double, float)\n- texto (String)\n- log (boolean)\n- lista~tipo~"
```

#### Tipos Primitivos Soportados

| Tipo Quetzal | Tipos Java Mapeados | Descripción |
|--------------|---------------------|-------------|
| `entero` | `int`, `long`, `short` | Números enteros |
| `numero` | `double`, `float` | Números decimales |
| `texto` | `String` | Cadenas de texto |
| `log` | `boolean` | Valores lógicos |
| `lista<T>` | Genérico | Lista tipada de elementos |

---

### 5. Parser y ASTPrinter

Componentes principales para la construcción y visualización del AST.

```mermaid
classDiagram
    class Parser {
        -List~Token~ tokens
        -int current
        +Parser(List~Token~ tokens)
        +parse() List~Statement~
        -parseDeclaration() Statement
        -parseVarDeclaration() VarDeclaration
        -parseExpression() Expression
        -parseTerm() Expression
        -parsePrimary() Expression
        -parseType() TypeInfo
        -peek() Token
        -consume(TokenType, String) Token
        -match(TokenType) boolean
        -check(TokenType) boolean
        -isAtEnd() boolean
    }
    
    class ASTPrinter {
        +print(List~Statement~ statements) String
        +visit(VarDeclaration stmt) String
        +visit(FunctionDeclaration stmt) String
        +visit(ReturnStatement expr) String
        +visit(Block block) String
        +visit(LiteralExpression expr) String
        +visit(ListExpression expr) String
        +visit(BinaryExpression expr) String
        +visit(VariableExpression expr) String
    }
    
    class Visitor~R~ {
        <<interface>>
    }
    
    ASTPrinter ..|> Visitor : implements
    Parser --> Statement : produces
    Parser --> TypeInfo : uses
    ASTPrinter --> Statement : prints
```

#### Descripción de Componentes

| Clase | Responsabilidad |
|-------|-----------------|
| `Parser` | Analizador sintáctico descendente recursivo. Consume tokens y construye el AST. Implementa gramática del lenguaje Quetzal. |
| `ASTPrinter` | Implementación del Visitor para serializar el AST a formato texto legible. Útil para debugging y visualización. |

---

### 6. Diagrama Completo de Clases

```mermaid
classDiagram
    %% Core AST
    class ASTNode {
        <<abstract>>
        #accept(Visitor~R~)* R
    }
    
    class Expression {
        <<abstract>>
    }
    
    class Statement {
        <<abstract>>
    }
    
    %% Statements
    class VarDeclaration {
        -TypeInfo type
        -boolean isMutable
        -String name
        -Expression initializer
    }
    
    class FunctionDeclaration {
        -TokenType returnType
        -String name
        -List~VarDeclaration~ parameters
        -Block body
    }
    
    class Block {
        -List~Statement~ statements
    }
    
    class ReturnStatement {
        -Expression expression
    }
    
    %% Expressions
    class LiteralExpression {
        -Object value
    }
    
    class VariableExpression {
        -String name
    }
    
    class BinaryExpression {
        -Expression left
        -Expression right
        -String operator
    }
    
    class ListExpression {
        -List~Expression~ elements
    }
    
    %% Support Classes
    class TypeInfo {
        -String name
        -TypeInfo genericType
    }
    
    class Visitor~R~ {
        <<interface>>
    }
    
    class Parser {
        -List~Token~ tokens
        -int current
    }
    
    class ASTPrinter {
    }
    
    %% Inheritance
    ASTNode <|-- Expression
    ASTNode <|-- Statement
    
    Statement <|-- VarDeclaration
    Statement <|-- FunctionDeclaration
    Statement <|-- Block
    Statement <|-- ReturnStatement
    
    Expression <|-- LiteralExpression
    Expression <|-- VariableExpression
    Expression <|-- BinaryExpression
    Expression <|-- ListExpression
    
    Visitor <|.. ASTPrinter
    
    %% Associations
    VarDeclaration --> TypeInfo
    VarDeclaration --> Expression
    FunctionDeclaration --> Block
    Block --> Statement
    ReturnStatement --> Expression
    BinaryExpression --> Expression
    ListExpression --> Expression
    Parser --> Statement
    ASTPrinter --> Visitor
```

---

## 🚀 Instalación

### Prerrequisitos

- **Java 11** o superior
- **Maven 3.x**

### Dependencias

```xml
<dependency>
    <groupId>com.stsc4j</groupId>
    <artifactId>quetzal-stsc4j-parser</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Compilación

```bash
mvn clean install
```

---

## 💻 Uso

### Ejemplo Básico

```java
import com.stsc4j.lexer.LexerContext;
import com.stsc4j.parser.ast.ASTPrinter;
import com.stsc4j.parser.ast.Parser;
import com.stsc4j.parser.ast.Statement;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Tokenizar el código fuente
        LexerContext context = new LexerContext();
        context.process("entero var a = 10");
        
        // 2. Parsear los tokens
        Parser parser = new Parser(context.getTokens());
        List<Statement> ast = parser.parse();
        
        // 3. Imprimir el AST
        ASTPrinter printer = new ASTPrinter();
        System.out.println(printer.print(ast));
    }
}
```

### Salida Esperada

```
(VAR [MUTABLE] entero a = 10)
```

---

## 📁 Estructura del Proyecto

```
quetzal-stsc4j-parser/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── stsc4j/
                    └── parser/
                        ├── Main.java              # Punto de entrada
                        ├── ast/                   # Clases base del AST
                        │   ├── ASTNode.java       # Nodo raíz abstracto
                        │   ├── ASTPrinter.java    # Visitor para imprimir
                        │   ├── Expression.java    # Base de expresiones
                        │   ├── Parser.java        # Analizador sintáctico
                        │   ├── Statement.java     # Base de sentencias
                        │   ├── TypeInfo.java      # Información de tipos
                        │   └── Visitor.java       # Interface Visitor
                        └── declaration/           # Implementaciones concretas
                            ├── BinaryExpression.java
                            ├── Block.java
                            ├── FunctionDeclaration.java
                            ├── ListExpression.java
                            ├── LiteralExpression.java
                            ├── ReturnStatement.java
                            ├── VarDeclaration.java
                            └── VariableExpression.java
```

---

## 🧪 Testing

Ejecutar pruebas unitarias:

```bash
mvn test
```

---

## 🤝 Contribución

Las contribuciones son bienvenidas. Por favor:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add: nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

## 👥 Autores

- **STSC4J Team** - *Desarrollo inicial*

---

<div align="center">

**[⬆ Volver arriba](#-quetzal-stsc4j-parser)**

</div>

