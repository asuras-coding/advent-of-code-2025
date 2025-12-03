fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val largestChar = it.dropLast(1).max()
            val lastChar = it.last()
            if (lastChar > largestChar) "$largestChar$lastChar".toInt()
            else "$largestChar${it.substringAfter(largestChar).max()}".toInt()
        }
    }

    fun part1b(input: List<String>): Long {
        return input.sumOf { it.part2Filter(2).toLong() }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { it.part2Filter(12).toLong() }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 357)
    check(part1b(testInput) == 357L)
    check(part2(testInput) == 3121910778619L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private fun String.part2Filter(remainingDigits: Int): String {
    var result = this
    val digitsToDrop = this.length - remainingDigits
    repeat(digitsToDrop) {
        val charToDrop = result.windowed(2).firstNotNullOfOrNull { if (it.first() < it.last()) it.first() else null }
        val indexToDrop = charToDrop?.let { result.indexOf(it) } ?: result.lastIndex
        result = result.take(indexToDrop) + result.drop(indexToDrop + 1)
    }
    return result
}
