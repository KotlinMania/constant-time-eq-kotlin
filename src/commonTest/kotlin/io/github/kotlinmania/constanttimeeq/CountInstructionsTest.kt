// port-lint: source ../tests/count_instructions.rs
package io.github.kotlinmania.constanttimeeq

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class CountInstructionsTest {
    private fun test(a: Byte, b: Byte) {
        val size = 64
        val left = ByteArray(size) { a }
        val right = ByteArray(size) { b }

        val fromStart = right.copyOf()
        for (index in 0 until size - 1) {
            fromStart[index] = a
            assertFalse(constantTimeEq(left, fromStart))
        }

        fromStart[size - 1] = a
        assertTrue(constantTimeEq(left, fromStart))

        val fromEnd = right.copyOf()
        for (matched in 1 until size) {
            fromEnd[size - matched] = a
            assertFalse(constantTimeEq(left, fromEnd))
        }

        fromEnd[0] = a
        assertTrue(constantTimeEq(left, fromEnd))
    }

    private fun testN(size: Int, a: Byte, b: Byte) {
        val left = ByteArray(size) { a }
        val right = ByteArray(size) { b }

        val fromStart = right.copyOf()
        for (index in 0 until size - 1) {
            fromStart[index] = a
            assertFalse(constantTimeEqN(left, fromStart))
        }

        fromStart[size - 1] = a
        assertTrue(constantTimeEqN(left, fromStart))

        val fromEnd = right.copyOf()
        for (matched in 1 until size) {
            fromEnd[size - matched] = a
            assertFalse(constantTimeEqN(left, fromEnd))
        }

        fromEnd[0] = a
        assertTrue(constantTimeEqN(left, fromEnd))
    }

    @Test
    fun testCountInstructions() {
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

        val size = 64
        val left = ByteArray(size) { 'A'.code.toByte() }
        val right = ByteArray(size) { 'B'.code.toByte() }

        val nearStart = right.copyOf()
        nearStart[0] = 'A'.code.toByte()
        val short = variableTimeEq(left, nearStart)

        val nearEnd = left.copyOf()
        nearEnd[size - 1] = 'B'.code.toByte()
        val long = variableTimeEq(left, nearEnd)

        assertFalse(short.first)
        assertFalse(long.first)
        assertNotEquals(short.second, long.second)
        assertTrue(variableTimeEq(left, left.copyOf()).first)
    }
}
