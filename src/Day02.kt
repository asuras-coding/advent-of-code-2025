fun main() {
    fun parseIdRange(input: List<String>): List<LongRange> {
        return input.map {
            val (start, end) = it.split("-")
            start.toLong()..end.toLong()
        }
    }

    fun part1(input: List<String>): Long {
        val ranges = parseIdRange(input.first().split(","))
        return ranges.map { it.filter { it.invalidId() } }.flatten().sumOf { it }
    }

    fun part2(input: List<String>): Long {
        val ranges = parseIdRange(input.first().split(","))
        return ranges.map { it.filter { it.invalidId2() } }.flatten().sumOf { it }
    }

    val testInput = readInput("Day02_test")
    part1(testInput).println()
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

fun Long.invalidId(): Boolean = this.toString().let { s -> s.take(s.length / 2) == s.drop(s.length / 2) }
fun Long.invalidId2(): Boolean {
    val s = this.toString()
    var index = 1
    while (index <= s.length/2) {
        val sub = s.take(index)
        if (sub.repeat(s.length/sub.length) == s) return true
        index++
    }
    return false
}