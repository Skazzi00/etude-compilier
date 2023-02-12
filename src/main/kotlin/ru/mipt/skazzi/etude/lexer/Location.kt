package ru.mipt.skazzi.etude.lexer

data class Location(var line: Int, var column: Int) {
    override fun toString(): String = "$line:$column"
}