package ru.mipt.skazzi.etude.lexer

import org.junit.jupiter.api.Test

import ru.mipt.skazzi.etude.lexer.TokenType.*
import org.junit.jupiter.api.Assertions.*
import java.io.StringReader

class LexerTest {

    private fun Lexer.check(vararg tokenTypes: TokenType) =
        tokenTypes.map(this::matches).all { it } && this.matches(TOKEN_EOF)


    @Test
    fun justWorks() {
        val source = StringReader("1 + 2")
        val lexer = Lexer(source)
        assertTrue(lexer.check(NUMBER, PLUS, NUMBER))
    }

    @Test
    fun braces() {
        val source = StringReader("1 + (1)")
        val lexer = Lexer(source)
        assertTrue(lexer.check(NUMBER, PLUS, LEFT_PAREN, NUMBER, RIGHT_PAREN))
    }

    @Test
    fun keywords() {
        val source = StringReader("var fun for if else return true false")
        val lexer = Lexer(source)
        assertTrue(lexer.check(VAR, FUN, FOR, IF, ELSE, RETURN, TRUE, FALSE))
    }

    @Test
    fun consequent() {
        val source = StringReader("!true")
        val lexer = Lexer(source)
        assertTrue(lexer.check(NOT, TRUE))
    }

    @Test
    fun comments() {
        val source = StringReader("""
            # Comment if var a = 1; \n
            # One more comment \n
            1 # Token then comment 
            # Comment with no newline
        """.trimIndent())
        val lexer = Lexer(source)
        assertTrue(lexer.check(NUMBER))
    }

    @Test
    fun statement() {
        val source = StringReader("var abc = 0")
        val lexer = Lexer(source)
        assertTrue(lexer.check(VAR, IDENTIFIER, ASSIGN, NUMBER))
    }

    @Test
    fun funcDeclarationArgs() {
        val source = StringReader("(x, y)")
        val lexer = Lexer(source)
        assertTrue(lexer.check(LEFT_PAREN, IDENTIFIER, COMMA, IDENTIFIER, RIGHT_PAREN))
    }

    @Test
    fun curly() {
        val source = StringReader("{ it }")
        val lexer = Lexer(source)
        assertTrue(lexer.check(LEFT_CBRACE, IDENTIFIER, RIGHT_CBRACE))
    }

    @Test
    fun assignAndEquals() {
        val source = StringReader("== = ==")
        val lexer = Lexer(source)
        assertTrue(lexer.check(EQUALS, ASSIGN, EQUALS))
    }
}