// port-lint: source constant_time_eq/src/lib.rs
package io.github.kotlinmania.constanttimeeq

/**
 * Optimizer barrier preventing compiler optimizations from removing comparison operations.
 *
 * Input value is passed through an optimization barrier so that constant propagation and folding
 * do not elide the calculation.
 *
 * The implementation keeps this function non-inlined to ensure that optimization passes
 * do not look inside this function.
 */
internal fun optimizerHide(value: Byte): Byte = value

/**
 * Compares two byte slices for inequality in constant time.
 *
 * Traverses every byte in the slices, accumulating differences with bitwise OR,
 * and passes the result through [optimizerHide].
 *
 * The compare with 0 must happen outside this function.
 */
internal fun constantTimeNe(a: ByteArray, b: ByteArray): Byte {
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
 * ```kotlin
 * constantTimeEq("foo".encodeToByteArray(), "foo".encodeToByteArray()) // true
 * constantTimeEq("foo".encodeToByteArray(), "bar".encodeToByteArray()) // false
 * constantTimeEq("bar".encodeToByteArray(), "baz".encodeToByteArray()) // false
 * constantTimeEq(byteArrayOf(), byteArrayOf()) // true
 *
 * // Not equal-sized, so won't take constant time.
 * constantTimeEq("foo".encodeToByteArray(), byteArrayOf()) // false
 * constantTimeEq("foo".encodeToByteArray(), "quux".encodeToByteArray()) // false
 * ```
 */
public fun constantTimeEq(a: ByteArray, b: ByteArray): Boolean =
    a.size == b.size && constantTimeNe(a, b) == 0.toByte()

/**
 * Compares two fixed-size byte arrays for inequality in constant time.
 *
 * Accumulates XOR differences across N elements and passes the result through [optimizerHide].
 *
 * The compare with 0 must happen outside this function.
 */
internal fun constantTimeNeN(a: ByteArray, b: ByteArray, n: Int): Byte {
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
 * ```kotlin
 * constantTimeEqN(ByteArray(20) { 3 }, ByteArray(20) { 3 }) // true
 * constantTimeEqN(ByteArray(20) { 3 }, ByteArray(20) { 7 }) // false
 * ```
 */
public fun constantTimeEqN(a: ByteArray, b: ByteArray): Boolean {
    require(a.size == b.size)
    return constantTimeNeN(a, b, a.size) == 0.toByte()
}

/**
 * Compares two 128-bit byte strings in constant time.
 *
 * # Examples
 *
 * ```kotlin
 * constantTimeEq16(ByteArray(16) { 3 }, ByteArray(16) { 3 }) // true
 * constantTimeEq16(ByteArray(16) { 3 }, ByteArray(16) { 7 }) // false
 * ```
 */
public fun constantTimeEq16(a: ByteArray, b: ByteArray): Boolean {
    require(a.size == 16 && b.size == 16)
    return constantTimeEqN(a, b)
}

/**
 * Compares two 256-bit byte strings in constant time.
 *
 * # Examples
 *
 * ```kotlin
 * constantTimeEq32(ByteArray(32) { 3 }, ByteArray(32) { 3 }) // true
 * constantTimeEq32(ByteArray(32) { 3 }, ByteArray(32) { 7 }) // false
 * ```
 */
public fun constantTimeEq32(a: ByteArray, b: ByteArray): Boolean {
    require(a.size == 32 && b.size == 32)
    return constantTimeEqN(a, b)
}

/**
 * Compares two 512-bit byte strings in constant time.
 *
 * # Examples
 *
 * ```kotlin
 * constantTimeEq64(ByteArray(64) { 3 }, ByteArray(64) { 3 }) // true
 * constantTimeEq64(ByteArray(64) { 3 }, ByteArray(64) { 7 }) // false
 * ```
 */
public fun constantTimeEq64(a: ByteArray, b: ByteArray): Boolean {
    require(a.size == 64 && b.size == 64)
    return constantTimeEqN(a, b)
}
