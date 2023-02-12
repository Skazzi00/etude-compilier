package ru.mipt.skazzi.etude.lexer

enum class TokenType(val word: String? = null) {
    NUMBER,
    STRING,
    IDENTIFIER,
    TRUE("true"),
    FALSE("false"),
    PLUS,
    MINUS,
    STAR,
    DIV,
    ASSIGN,
    EQUALS, NOT_EQ,
    NOT,
    LT, GT,
    LEFT_PAREN, RIGHT_PAREN,
    LEFT_CBRACE, RIGHT_CBRACE,
    COMMA,
    COLON,
    FUN("fun"),
    FOR("for"),
    VAR("var"),
    IF("if"),
    THEN("then"),
    ELSE("else"),
    RETURN("return"),
    TOKEN_EOF;

    companion object {
        private val identTable: Map<String, TokenType> = values()
            .filter { it.word != null }
            .associateBy { it.word!! }

        fun lookupWord(word: String): TokenType = identTable[word] ?: IDENTIFIER
    }
}