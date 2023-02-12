package ru.mipt.skazzi.etude.lexer

sealed class Token(val type: TokenType, val location: Location) {
    override fun toString(): String {
        return "$type $location"
    }
}

class SimpleToken(type: TokenType, location: Location) : Token(type, location)

class NumberToken(type: TokenType, location: Location, val num: Int) : Token(type, location) {
    override fun toString(): String {
        return "${super.toString()} $num"
    }
}

class StringToken(type: TokenType, location: Location, val sequence: CharSequence) : Token(type, location) {
    override fun toString(): String {
        return "${super.toString()} $sequence"
    }
}

class IdentifierToken(type: TokenType, location: Location, val identifier: CharSequence) : Token(type, location) {
    override fun toString(): String {
        return "${super.toString()} $identifier"
    }
}