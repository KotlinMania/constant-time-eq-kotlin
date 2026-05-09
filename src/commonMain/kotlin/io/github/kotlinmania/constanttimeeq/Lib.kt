// port-lint: source src/lib.rs
package io.github.kotlinmania.constanttimeeq

// Upstream Rust crate is `#![no_std]`. The Kotlin port likewise pulls only
// from the Kotlin standard library — no platform I/O, no JVM-only types.

// Upstream defines three platform-gated `optimizer_hide` implementations.
// The x86 / x86_64 variant uses `core::arch::asm!("/* {0} */", inout(reg_byte) value, ...)`
// with the `pure`, `nomem`, `nostack`, `preserves_flags` options as a no-op
// inline-assembly barrier; the ARM / AArch64 / RISC-V variant uses the same
// pattern with `inout(reg)` and the `asm_sub_register` lint suppression.
// On every other architecture (and under Miri) the fallback uses
// `core::hint::black_box` together with an `inline(never)` hint, which —
// quoting the upstream comment — round-trips the value through the stack
// instead of leaving it in a register, since "experimental codegen
// backends might implement black_box as a pure identity function, without
// the expected optimization barrier, so it's less guaranteed than inline
// asm. For that reason, we also use the inline(never) hint, which makes
// it harder for an optimizer to look inside this function."
//
// Kotlin Multiplatform exposes neither portable inline assembly nor a
// `black_box` intrinsic across every supported target, so this file
// keeps a single non-inlined identity function that the JVM, Native, JS,
// and Wasm-JS back-ends will not fold across the call boundary. Like the
// upstream fallback, it is the weakest of the three guarantees: it
// discourages constant folding of the accumulator inside `constantTimeNe`
// and `constantTimeNeN` but cannot promise true constant-time execution
// on a sufficiently aggressive backend.
private fun optimizerHide(value: Int): Int {
    return value
}

private fun constantTimeNe(a: ByteArray, b: ByteArray): Int {
    require(a.size == b.size)

    val len = a.size

    var tmp = 0
    for (i in 0 until len) {
        tmp = tmp or ((a[i].toInt() and 0xff) xor (b[i].toInt() and 0xff))
    }

    // The compare with 0 must happen outside this function.
    return optimizerHide(tmp)
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
fun constantTimeEq(a: ByteArray, b: ByteArray): Boolean {
    return a.size == b.size && constantTimeNe(a, b) == 0
}

// Fixed-size array variant.

private fun constantTimeNeN(a: ByteArray, b: ByteArray, n: Int): Int {
    var tmp = 0
    for (i in 0 until n) {
        tmp = tmp or ((a[i].toInt() and 0xff) xor (b[i].toInt() and 0xff))
    }

    // The compare with 0 must happen outside this function.
    return optimizerHide(tmp)
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
    return constantTimeNeN(a, b, a.size) == 0
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
