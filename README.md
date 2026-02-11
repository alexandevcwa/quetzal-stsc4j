# 🐦 Quetzal STSC4J - Source to Source Compiler for Java

<p align="center">
  <img src="https://img.shields.io/badge/Java-11+-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 11+"/>
  <img src="https://img.shields.io/badge/.NET-10.0-purple?style=for-the-badge&logo=dotnet&logoColor=white" alt=".NET 10"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License MIT"/>
  <img src="https://img.shields.io/badge/Status-En%20Desarrollo-yellow?style=for-the-badge" alt="En Desarrollo"/>
</p>

---

## 📋 Descripción

**Quetzal STSC4J** (Source to Source Compiler for Java) es un compilador transpilador que convierte código fuente escrito en el lenguaje de programación **Quetzal** a código Java ejecutable. El proyecto está compuesto por un compilador modular escrito en Java y un IDE (Entorno de Desarrollo Integrado) desarrollado en C# con WPF.

### 🔗 Lenguaje Quetzal

Quetzal es un lenguaje de programación moderno con sintaxis intuitiva. Para más información sobre el lenguaje, visita la documentación oficial:

**📖 [Documentación Oficial de Quetzal](https://lenguaje-quetzal.antaresgt.com/introduccion/bienvenido/)**

---

## 🏗️ Arquitectura del Proyecto

El compilador sigue una arquitectura modular basada en las fases clásicas de compilación:

```
quetzal-stsc4j/
├── quetzal-stsc4j-lexer/     # Análisis Léxico (Java/Maven)
├── quetzal-stsc4j-parser/    # Análisis Sintáctico (Java/Maven)
└── quetzal-stsc4j-ide/       # IDE con WPF (C#/.NET 10)
```

### 📊 Flujo de Compilación

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Código Fuente  │───▶│     LEXER       │───▶│     PARSER      │───▶│  Código Java    │
│    Quetzal      │    │ Análisis Léxico │    │Análisis Sintác. │    │    Generado     │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
       .qz                  Tokens              AST (Árbol)               .java
```

---

## 🔍 Fases del Compilador

### 1️⃣ Análisis Léxico (Lexer)

El **Lexer** (Analizador Léxico) es la primera fase del proceso de compilación. Su función principal es leer el código fuente carácter por carácter y agruparlos en unidades significativas llamadas **tokens**.

#### ¿Qué es un Token?

Un token es la unidad mínima con significado en un lenguaje de programación. Por ejemplo:

| Código Fuente | Tokens Generados |
|---------------|------------------|
| `int x = 10;` | `INT`, `IDENTIFIER(x)`, `EQUAL`, `LIT_INT(10)` |
| `if (a > b)` | `IF`, `PARENTHESES_OPEN`, `IDENTIFIER(a)`, `GREATER_THAN`, `IDENTIFIER(b)`, `PARENTHESES_CLOSE` |

#### Características del Lexer de Quetzal STSC4J

- **Patrón State Machine**: Implementado usando el patrón de diseño State para manejar diferentes contextos léxicos
- **Tipos de Tokens Soportados**:
  - 🔑 **Palabras Reservadas**: `int`, `double`, `string`, `if`, `else`, `while`, `for`, `return`, etc.
  - 🔢 **Literales**: Números enteros, decimales, cadenas de texto, booleanos
  - 🔣 **Símbolos**: Operadores aritméticos, lógicos, de comparación, delimitadores
  - 📝 **Identificadores**: Nombres de variables, funciones y clases

#### Estructura del Módulo Lexer

```
quetzal-stsc4j-lexer/
└── src/main/java/com/stsc4j/lexer/
    ├── LexerContext.java      # Contexto principal del lexer
    ├── Token.java             # Representación de un token
    ├── TokenType.java         # Enum con todos los tipos de tokens
    └── state/                 # Estados del autómata
        ├── LexerState.java        # Interfaz base
        ├── InitialState.java      # Estado inicial
        ├── LetterState.java       # Procesamiento de letras
        ├── DigitState.java        # Procesamiento de dígitos
        ├── DigitLiteralState.java # Literales numéricos
        ├── StringLiteralState.java# Literales de cadena
        └── SymbolState.java       # Símbolos y operadores
```

---

### 2️⃣ Análisis Sintáctico (Parser)

El **Parser** (Analizador Sintáctico) es la segunda fase del compilador. Recibe los tokens generados por el Lexer y los organiza en una estructura jerárquica llamada **AST (Abstract Syntax Tree)** o Árbol de Sintaxis Abstracta.

#### ¿Qué es el AST?

El AST es una representación en forma de árbol de la estructura sintáctica del programa. Cada nodo del árbol representa una construcción del lenguaje:

```
         Programa
            │
    ┌───────┴───────┐
    │               │
Declaración     Expresión
    │               │
   int x        x + 10
                /   \
               x    10
```

#### ¿Qué hace el Parser?

1. **Validación Sintáctica**: Verifica que los tokens sigan las reglas gramaticales del lenguaje
2. **Construcción del AST**: Genera la estructura de árbol que representa el programa
3. **Detección de Errores**: Reporta errores de sintaxis con información de ubicación

#### Estructura del Módulo Parser

```
quetzal-stsc4j-parser/
└── src/main/java/com/stsc4j/parser/
    ├── ast/                   # Nodos del AST
    │   ├── ASTNode.java       # Nodo base
    │   ├── Expression.java    # Expresiones
    │   ├── Statement.java     # Sentencias
    │   └── Visitor.java       # Patrón Visitor
    └── declaration/           # Declaraciones
```

---

## 💻 IDE - Entorno de Desarrollo Integrado

El IDE está desarrollado en **C# con WPF** y proporciona una interfaz gráfica moderna para escribir, editar y compilar código Quetzal.

### Características del IDE

- 🎨 **Tema Oscuro Moderno**: Interfaz visual cómoda para programar
- 📂 **Explorador de Proyectos**: Navegación por archivos y carpetas
- 📝 **Editor de Código**: Con soporte para múltiples pestañas
- ▶️ **Compilación Integrada**: Ejecuta el compilador directamente desde el IDE
- 💾 **Gestión de Archivos**: Crear, abrir y guardar archivos `.qtzl`

### Tecnologías

- **Framework**: .NET 10.0
- **UI**: Windows Presentation Foundation (WPF)
- **Lenguaje**: C#

---

## 🚀 Requisitos del Sistema

### Para el Compilador (Java)
- **Java**: JDK 11 o superior
- **Maven**: 3.6 o superior

### Para el IDE (C#)
- **Sistema Operativo**: Windows 10/11
- **.NET SDK**: 10.0 o superior

---

## 📦 Instalación y Uso

### Compilar el Lexer

```bash
cd quetzal-stsc4j-lexer
mvn clean install
```

### Compilar el Parser

```bash
cd quetzal-stsc4j-parser
mvn clean install
```

### Ejecutar el IDE

```bash
cd quetzal-stsc4j-ide
dotnet run
```

---

## 📚 Documentación Adicional

### Conceptos de Compiladores

| Fase | Entrada | Salida | Descripción |
|------|---------|--------|-------------|
| **Lexer** | Código fuente | Tokens | Análisis léxico - tokenización |
| **Parser** | Tokens | AST | Análisis sintáctico - estructura |
| **Semántico** | AST | AST anotado | Verificación de tipos (próximamente) |
| **Generación** | AST | Código Java | Generación de código (próximamente) |

---

## 🛠️ Estado del Desarrollo

| Componente | Estado |
|------------|--------|
| Lexer (Análisis Léxico) | ✅ En desarrollo |
| Parser (Análisis Sintáctico) | ✅ En desarrollo |
| Análisis Semántico | 🔜 Próximamente |
| Generación de Código | 🔜 Próximamente |
| IDE | ✅ En desarrollo |

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un issue o pull request para sugerencias o mejoras.

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

<p align="center">
  <strong>Desarrollado con ❤️ para la comunidad de programadores</strong>
</p>

<p align="center">
  <a href="https://lenguaje-quetzal.antaresgt.com/introduccion/bienvenido/">
    📖 Documentación de Quetzal
  </a>
</p>

