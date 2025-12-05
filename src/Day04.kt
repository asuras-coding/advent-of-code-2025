fun main() {

    fun part1(input: List<String>): Int {
        val grid: Grid<Char> = input.toGrid { it }
        var counter = 0
        for (cell in grid.cells()) {
            if (cell.value != '@') continue
            if (grid.adjacentCells(cell).count { it.value == '@' } < 4) counter++
        }
        return counter
    }

    fun part2(input: List<String>): Int {
        val grid: Grid<Char> = input.toGrid { it }
        var counter = 0
        val rollsToRemove = mutableListOf<Cell<Char>>()
        var done = false
        while (!done) {
            for (cell in grid.cells()) {
                if (cell.value != '@') continue
                if (grid.adjacentCells(cell).count { it.value == '@' } < 4) {
                    counter++
                    rollsToRemove.add(cell)
                }
            }
            if (rollsToRemove.isEmpty()) done = true
            rollsToRemove.forEach { cell -> cell.value = '.' }
            rollsToRemove.clear()
        }
        return counter
    }

    fun part2b(input: List<String>): Int {
        val grid: Grid<Char> = input.toGrid { it }
        return generateSequence {
            grid.cells()
                .filter { it.value == '@' && grid.adjacentCells(it).count { adj -> adj.value == '@' } < 4 }
                .ifEmpty { null }
        }.sumOf { cells ->
            cells.forEach { cell -> cell.value = '.' }
            cells.size
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 43)
    check(part2b(testInput) == 43)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}