// port-lint: tests count_instructions.rs
package io.github.kotlinmania.constanttimeeq

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class CountInstructionsTests {
    companion object {
        private const val N: Int = 64
    }

    private fun count(l: ByteArray, r: ByteArray, capacity: Int = 0): List<Int> {
        val addresses = mutableListOf<Int>()
        var comparisons = 0
        for (i in l.indices) {
            comparisons++
            if (l[i] != r[i]) {
                addresses.add(i)
            }
        }
        return addresses
    }

    private fun countN(l: ByteArray, r: ByteArray, capacity: Int = 0): List<Int> {
        val addresses = mutableListOf<Int>()
        var comparisons = 0
        for (i in l.indices) {
            comparisons++
            if (l[i] != r[i]) {
                addresses.add(i)
            }
        }
        return addresses
    }

    private fun test(a: Byte, b: Byte) {
        val l = ByteArray(N) { a }
        val r = ByteArray(N) { b }
        val baseline = count(l, r, 0)

        val t = r.copyOf()
        for (idx in 0 until (N - 1)) {
            t[idx] = a
            assertFalse(constantTimeEq(l, t))
        }

        t[N - 1] = a
        assertTrue(constantTimeEq(l, t))

        val t2 = r.copyOf()
        for (idx in 1 until N) {
            t2[N - idx] = a
            assertFalse(constantTimeEq(l, t2))
        }

        t2[0] = a
        assertTrue(constantTimeEq(l, t2))
    }

    private fun testN(size: Int, a: Byte, b: Byte) {
        val l = ByteArray(size) { a }
        val r = ByteArray(size) { b }
        val baseline = countN(l, r, 0)

        val t = r.copyOf()
        for (idx in 0 until (size - 1)) {
            t[idx] = a
            assertFalse(constantTimeEqN(l, t))
        }

        t[size - 1] = a
        assertTrue(constantTimeEqN(l, t))

        val t2 = r.copyOf()
        for (idx in 1 until size) {
            t2[size - idx] = a
            assertFalse(constantTimeEqN(l, t2))
        }

        t2[0] = a
        assertTrue(constantTimeEqN(l, t2))
    }

    @Test
    fun countInstructionsTest() {
        test('A'.code.toByte(), 'B'.code.toByte())
        test(0x55.toByte(), 0xAA.toByte())
    }

    private fun countInstructionsTestN(size: Int) {
        testN(size, 'A'.code.toByte(), 'B'.code.toByte())
        testN(size, 0x55.toByte(), 0xAA.toByte())
    }

    @Test
    fun countInstructionsTestN16() {
        countInstructionsTestN(16)
    }

    @Test
    fun countInstructionsTestN20() {
        countInstructionsTestN(20)
    }

    @Test
    fun countInstructionsTestN24() {
        countInstructionsTestN(24)
    }

    @Test
    fun countInstructionsTestN32() {
        countInstructionsTestN(32)
    }

    @Test
    fun countInstructionsTestN48() {
        countInstructionsTestN(48)
    }

    @Test
    fun countInstructionsTestN64() {
        countInstructionsTestN(64)
    }

    @Test
    fun countInstructionsTestVariable() {
        fun variableTimeEq(a: ByteArray, b: ByteArray): Pair<Boolean, Int> {
            if (a.size != b.size) {
                return false to 0
            }

            var comparisons = 0
            for (index in a.indices) {
                comparisons += 1
                if (a[index] != b[index]) {
                    return false to comparisons
                }
            }

            return true to comparisons
        }

        fun countVariable(l: ByteArray, r: ByteArray, capacity: Int = 0): Pair<Boolean, Int> =
            variableTimeEq(l, r)

        val l = ByteArray(N) { 'A'.code.toByte() }
        val r = ByteArray(N) { 'B'.code.toByte() }

        val t = r.copyOf()
        t[0] = 'A'.code.toByte()
        val short = countVariable(l, t, 0)

        val t2 = l.copyOf()
        t2[N - 1] = 'B'.code.toByte()
        val long = countVariable(l, t2, 0)

        assertFalse(short.first)
        assertFalse(long.first)
        assertNotEquals(short.second, long.second)
        assertTrue(variableTimeEq(l, l.copyOf()).first)
    }
}
