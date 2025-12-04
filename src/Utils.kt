import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

typealias Grid<T> = Array<Array<Cell<T>>>

fun <T> List<String>.toGrid(convert: (Char) -> T): Grid<T> =
    mapIndexed { y, row -> row.toCharArray().mapIndexed { x, value -> Cell(convert(value), x, y) }.toTypedArray() }.toTypedArray()

data class Cell<T>(var value: T, val x: Int, val y: Int)

fun <T> Grid<T>.print() = forEach { println(it.joinToString { c -> c.value.toString() }) }

fun <T> Grid<T>.adjacentCells(cell: Cell<T>): List<Cell<T>> {
    return listOf(
        cell.x - 1 to cell.y - 1,
        cell.x - 1 to cell.y,
        cell.x - 1 to cell.y + 1,
        cell.x to cell.y - 1,
        cell.x to cell.y + 1,
        cell.x + 1 to cell.y - 1,
        cell.x + 1 to cell.y,
        cell.x + 1 to cell.y + 1
    ).mapNotNull { runCatching { this[it.second][it.first] }.getOrNull() }
}

fun <T> Grid<T>.cells() = this.flatMap { it.toList() }

inline fun <T> Grid<T>.forEachCell(action: (Cell<T>) -> Unit) =
    cells().forEach { cell -> action(cell) }