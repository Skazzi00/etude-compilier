package ru.mipt.skazzi.etude.lexer

import java.io.Closeable
import java.io.Reader

class Scanner(private val source: Reader) : Closeable {
    private val location: Location = Location(0, 0)

    fun getLocation() = location.copy()

    var current = readSource()
        private set

    fun read(): Char? {
        when (current) {
            '\n' -> {
                location.line++
                location.column = 0
            }
            null -> {}
            else -> location.column++
        }
        return readSource().also { current = it }
    }

    fun peek() = source.mark(1).let {
        readSource().also { source.reset() }
    }


    override fun close() {
        source.close()
    }

    private fun readSource(): Char? = source.read().takeIf { it != -1 }?.toChar()
    fun skipLine() {
        while (current?.let { it != '\n' } == true) {
            read()
        }
    }

    fun takeWhile(predicate: (Char?) -> Boolean): CharSequence {
        val builder = StringBuilder()
        while (predicate(current)) {
            builder.append(current)
            read()
        }
        return builder
    }
}