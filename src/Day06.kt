fun main() {

    fun part1(input: List<String>): Long {
        return input.toGrid().transpose("").sumOf { it.map { it.value }.reduce() }
    }

    fun part2(input: List<String>): Long {
        return input.toGrid { it }.toCalculations().sumOf { it.calculate() }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 4277556L)
    println(part2(testInput))
    check(part2(testInput) == 3263827L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.reduce(): Long {
    val op = when (last()) {
        "*" -> { a: Long, b: Long -> a * b }
        "+" -> { a: Long, b: Long -> a + b }
        else -> error("Unknown operator")
    }
    return dropLast(1).map { it.toLong() }.reduce { acc, value -> op(acc, value) }
}

private fun Char.isOperator() = this == '*' || this == '+'
private fun Char.isSpace() = this == ' '

private fun Grid<Char>.toCalculations(): List<Calculation> {
    return buildList {
        val acc: MutableList<Char> = mutableListOf()
        var op: Char? = null
        val numbers: MutableList<Long> = mutableListOf()
        for (c: Char in transpose(' ').cells().reversed().map { it.value }) {
            when {
                c.isDigit() -> acc += c
                c.isOperator() -> {
                    op = c
                    if (acc.isNotEmpty()) {
                        numbers.add(acc.reversed().joinToString("").toLong())
                        acc.clear()
                    }
                }
                c.isSpace() -> {
                    if (acc.isNotEmpty()) {
                        numbers.add(acc.reversed().joinToString("").toLong())
                        acc.clear()
                        if (op != null) {
                            add(Calculation(op, numbers.toList()))
                            op = null
                            numbers.clear()
                        }
                    }
                }
            }
        }
        add(Calculation(op!!, numbers + acc.reversed().joinToString("").toLong()))
    }
}

data class Calculation(val op: Char, val numbers: List<Long>) {
    fun calculate(): Long = numbers.reduce { acc, value ->
        when (op) {
            '+' -> acc + value
            '*' -> acc * value
            else -> error("Unknown operator")
        }
    }
}