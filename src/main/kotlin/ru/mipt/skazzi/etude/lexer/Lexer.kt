package ru.mipt.skazzi.etude.lexer

import ru.mipt.skazzi.etude.lexer.TokenType.*
import java.io.Reader

class Lexer(reader: Reader) {
    private val scanner = Scanner(reader)
    private var unmatched: Token? = null

    fun matches(tokenType: TokenType) = getNextToken().let {
        if (it.type != tokenType) {
            unmatched = it
            return@let false
        } else {
            return@let true
        }
    }

    fun getTokens(): List<Token> {
        val tokens = ArrayList<Token>()
        while (getNextToken().also { tokens.add(it) }.type != TOKEN_EOF) {
        }
        return tokens
    }

    private fun getNextToken(): Token {
        if (unmatched != null) return unmatched!!.also { unmatched = null }
        skipWhitespace()
        skipComments()
        return matchEnd()
            ?: matchOperator()
            ?: matchLiteral()
            ?: matchWord()
            ?: throw Exception("Unknown token at ${scanner.getLocation()}")
    }

    private fun matchEnd() = if (scanner.current == null) SimpleToken(TOKEN_EOF, scanner.getLocation()) else null

    private fun matchOperator(): SimpleToken? {
        val operator = when (scanner.current) {
            '=' -> if (scanner.peek() == '=') scanner.read().let { EQUALS } else ASSIGN
            '!' -> if (scanner.peek() == '=') scanner.read().let { NOT_EQ } else NOT
            '<' -> LT
            '>' -> GT
            '+' -> PLUS
            '-' -> MINUS
            '*' -> STAR
            '/' -> DIV
            '(' -> LEFT_PAREN
            ')' -> RIGHT_PAREN
            '{' -> LEFT_CBRACE
            '}' -> RIGHT_CBRACE
            ',' -> COMMA
            ':' -> COLON
            else -> null
        }
        return operator?.also { scanner.read() }?.let { SimpleToken(it, scanner.getLocation()) }
    }

    private fun matchLiteral() = matchNumber() ?: matchString()

    private fun matchNumber(): NumberToken? {
        var num = 0
        var mult = 1
        while (scanner.current?.isDigit() == true) {
            num += scanner.current!!.digitToInt()
            mult *= 10
            scanner.read()
        }
        return if (mult != 1) NumberToken(NUMBER, scanner.getLocation(), num) else null
    }


    private fun matchString(): StringToken? {
        if (scanner.current != '\"') {
            return null
        }
        scanner.read()

        val literal = scanner.takeWhile { it != '\"' }

        scanner.read()
        return StringToken(STRING, scanner.getLocation(), literal)
    }

    private fun matchWord(): Token? {
        val word = scanner.takeWhile { it?.isLetterOrDigit() == true || it == '_' }.toString()
        if (word.isEmpty()) {
            return null
        }
        val type = TokenType.lookupWord(word)
        if (type == IDENTIFIER) {
            return IdentifierToken(type, scanner.getLocation(), word)
        }
        return SimpleToken(type, scanner.getLocation())
    }

    private fun skipComments() {
        while (scanner.current == '#') {
            scanner.skipLine()
            skipWhitespace()
        }
    }

    private fun skipWhitespace() {
        while (scanner.current?.isWhitespace() == true) {
            scanner.read()
        }
    }
}
