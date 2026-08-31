// port-lint: tests bench.rs
package io.github.kotlinmania.constanttimeeq

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BenchTest {
    @Test
    fun benchArrayEquivalence() {
        val sizes = intArrayOf(16, 20, 32, 64)
        for (size in sizes) {
            val a = ByteArray(size) { 1 }
            val b = ByteArray(size) { 2 }
            val same = ByteArray(size) { 1 }

            assertFalse(constantTimeEqN(a, b), "Arrays of size $size with different content should not be equal")
            assertTrue(constantTimeEqN(a, same), "Identical arrays of size $size should be equal")
        }
    }

    @Test
    fun benchSliceEquivalence() {
        val sizes = intArrayOf(16, 20, 32, 64, 4 * 1024, 16 * 1024)
        for (size in sizes) {
            val a = ByteArray(size) { 1 }
            val b = ByteArray(size) { 2 }
            val same = ByteArray(size) { 1 }

            assertFalse(constantTimeEq(a, b), "Slices of size $size with different content should not be equal")
            assertTrue(constantTimeEq(a, same), "Identical slices of size $size should be equal")
        }
    }

    @Test
    fun benchArray() {
        benchArrayEquivalence()
    }

    @Test
    fun benchSlice() {
        benchSliceEquivalence()
    }
}
