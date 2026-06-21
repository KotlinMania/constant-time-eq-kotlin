// port-lint: source lib.rs
package io.github.kotlinmania.constanttimeeq

// The current implementation of blackBox in the main codegen backends is similar to
// {
//     val result = value
//     asm("", in(reg) &result)
//     result
// }
// which round-trips the value through the stack, instead of leaving it in a register.
// Experimental codegen backends might implement blackBox as a pure identity function,
// without the expected optimization barrier, so it's less guaranteed than inline asm.
// For that reason, we also keep this function non-inlined, which makes it harder for
// an optimizer to look inside this function.
private fun optimizerHide(value: Byte): Byte = value

private fun constantTimeNe(a: ByteArray, b: ByteArray): Byte {
    require(a.size == b.size)

    val len = a.size

    var tmp = 0
    for (i in 0 until len) {
        tmp = tmp or ((a[i].toInt() and 0xff) xor (b[i].toInt() and 0xff))
    }

    // The compare with 0 must happen outside this function.
    return optimizerHide(tmp.toByte())
}

/**
 * Compares two equal-sized byte strings in constant time.
 *
 * # Examples
 *
 * ```
 * constantTimeEq("foo".encodeToByteArray(), "foo".encodeToByteArray()) // true
 * constantTimeEq("foo".encodeToByteArray(), "bar".encodeToByteArray()) // false
 * constantTimeEq("bar".encodeToByteArray(), "baz".encodeToByteArray()) // false
 * constantTimeEq(byteArrayOf(),             byteArrayOf())             // true
 *
 * // Not equal-sized, so won't take constant time.
 * constantTimeEq("foo".encodeToByteArray(), byteArrayOf())              // false
 * constantTimeEq("foo".encodeToByteArray(), "quux".encodeToByteArray()) // false
 * ```
 */
fun constantTimeEq(a: ByteArray, b: ByteArray): Boolean =
    a.size == b.size && constantTimeNe(a, b) == 0.toByte()

// Fixed-size array variant.

private fun constantTimeNeN(a: ByteArray, b: ByteArray, n: Int): Byte {
    var tmp = 0
    for (i in 0 until n) {
        tmp = tmp or ((a[i].toInt() and 0xff) xor (b[i].toInt() and 0xff))
    }

    // The compare with 0 must happen outside this function.
    return optimizerHide(tmp.toByte())
}

/**
 * Compares two fixed-size byte strings in constant time.
 *
 * # Examples
 *
 * ```
 * constantTimeEqN(ByteArray(20) { 3 }, ByteArray(20) { 3 }) // true
 * constantTimeEqN(ByteArray(20) { 3 }, ByteArray(20) { 7 }) // false
 * ```
 */
fun constantTimeEqN(a: ByteArray, b: ByteArray): Boolean {
    require(a.size == b.size)
    return constantTimeNeN(a, b, a.size) == 0.toByte()
}

// Fixed-size variants for the most common sizes.

/**
 * Compares two 128-bit byte strings in constant time.
 *
 * # Examples
 *
 * ```
 * constantTimeEq16(ByteArray(16) { 3 }, ByteArray(16) { 3 }) // true
 * constantTimeEq16(ByteArray(16) { 3 }, ByteArray(16) { 7 }) // false
 * ```
 */
fun constantTimeEq16(a: ByteArray, b: ByteArray): Boolean {
    require(a.size == 16 && b.size == 16)
    return constantTimeEqN(a, b)
}

/**
 * Compares two 256-bit byte strings in constant time.
 *
 * # Examples
 *
 * ```
 * constantTimeEq32(ByteArray(32) { 3 }, ByteArray(32) { 3 }) // true
 * constantTimeEq32(ByteArray(32) { 3 }, ByteArray(32) { 7 }) // false
 * ```
 */
fun constantTimeEq32(a: ByteArray, b: ByteArray): Boolean {
    require(a.size == 32 && b.size == 32)
    return constantTimeEqN(a, b)
}

/**
 * Compares two 512-bit byte strings in constant time.
 *
 * # Examples
 *
 * ```
 * constantTimeEq64(ByteArray(64) { 3 }, ByteArray(64) { 3 }) // true
 * constantTimeEq64(ByteArray(64) { 3 }, ByteArray(64) { 7 }) // false
 * ```
 */
fun constantTimeEq64(a: ByteArray, b: ByteArray): Boolean {
    require(a.size == 64 && b.size == 64)
    return constantTimeEqN(a, b)
}
