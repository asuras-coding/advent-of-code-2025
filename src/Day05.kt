import kotlin.math.max
import kotlin.math.min

fun main() {
    fun parseInput(input: List<String>): Pair<List<LongRange>, List<Long>> {
        val ranges = input.takeWhile { it.isNotBlank() }.map {
            val (start, end) = it.split("-").map { it.toLong() }
            LongRange(start, end)
        }
        val ids = input.takeLastWhile { it.isNotBlank() }.map { it.toLong() }
        return ranges to ids
    }

    fun part1(input: List<String>): Int {
        val (ranges, ids) = parseInput(input)
        return ids.count { id -> ranges.any { it.contains(id) } }
    }

    fun part2(input: List<String>): Long {
        val (ranges, _) = parseInput(input)
        var mergedRanges = ranges
        var previous: List<LongRange>
        do {
            previous = mergedRanges
            mergedRanges = mergeRanges(mergedRanges).sortedBy { it.first }
        } while (previous != mergedRanges)
        return mergedRanges.sumOf { it.countFast() }
    }

    fun part2b(input: List<String>): Long {
        val (ranges, _) = parseInput(input)
        return ranges.merge().sumOf { it.countFast() }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 14L)
    check(part2b(testInput) == 14L)

    val input = readInput("Day05")
    part1(input).println()
    check(part2(input) == 353507173555373L)
    check(part2b(input) == 353507173555373L)
    part2(input).println()
}

fun List<LongRange>.merge(): List<LongRange> {
    return fold(emptyList()) { acc, range ->
        val other = acc.find { it.intersects(range) }
        when {
            other == null -> acc.plusElement(range)
            else -> acc
                .minusElement(other)
                .plusElement(other.merge(range))
                .merge()
        }
    }
}
fun LongRange.countFast(): Long = (last - first + 1).coerceAtLeast(0)
fun LongRange.intersects(other: LongRange) = this.first in other || this.last in other || other.first in this || other.last in this
fun LongRange.merge(other: LongRange) = min(first, other.first)..max(last, other.last)

fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
    return ranges.fold(mutableListOf()) { a: MutableList<LongRange>, b: LongRange ->
        if (!a.any { b.first in it && b.last in it }) {
            val lowerIncluded = a.find { b.first in it && b.last !in it }
            val upperIncluded = a.find { b.last in it && b.first !in it }
            when {
                lowerIncluded != null -> {
                    a.remove(lowerIncluded)
                    a.add(lowerIncluded.first..b.last)
                }

                upperIncluded != null -> {
                    a.remove(upperIncluded)
                    a.add(b.first..upperIncluded.last)
                }

                else -> a.add(b)
            }
        }
        a
    }
}