// port-lint: tests tests/count_instructions.rs
package io.github.kotlinmania.constanttimeeq

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CountInstructionsTests {
    companion object {
        private const val N: Int = 64
    }

    private fun verifySliceMismatchSweep(a: Byte, b: Byte) {
        val l = ByteArray(N) { a }
        val r = ByteArray(N) { b }

        // Unported assertion from tests/count_instructions.rs::count: Linux ptrace hardware instruction-address equality across mismatched offsets is outside portable KMP.
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

    private fun verifyFixedSizeMismatchSweep(size: Int, a: Byte, b: Byte) {
        val l = ByteArray(size) { a }
        val r = ByteArray(size) { b }

        // Unported assertion from tests/count_instructions.rs::count_n: Linux ptrace hardware instruction-address equality across mismatched offsets is outside portable KMP.
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
        verifySliceMismatchSweep('A'.code.toByte(), 'B'.code.toByte())
        verifySliceMismatchSweep(0x55.toByte(), 0xAA.toByte())
    }

    private fun countInstructionsTestN(size: Int) {
        verifyFixedSizeMismatchSweep(size, 'A'.code.toByte(), 'B'.code.toByte())
        verifyFixedSizeMismatchSweep(size, 0x55.toByte(), 0xAA.toByte())
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

    // Unported from tests/count_instructions.rs::count_instructions_test_variable: relies on Linux ptrace instruction-address trace comparison to detect early-return control flow.
}
