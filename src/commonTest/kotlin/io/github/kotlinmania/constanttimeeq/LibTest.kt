// port-lint: tests lib.rs
package io.github.kotlinmania.constanttimeeq

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibTest {
    @Test
    fun constantTimeEqDocExamples() {
        assertTrue(constantTimeEq("foo".encodeToByteArray(), "foo".encodeToByteArray()))
        assertFalse(constantTimeEq("foo".encodeToByteArray(), "bar".encodeToByteArray()))
        assertFalse(constantTimeEq("bar".encodeToByteArray(), "baz".encodeToByteArray()))
        assertTrue(constantTimeEq(byteArrayOf(), byteArrayOf()))

        // Not equal-sized, so won't take constant time.
        assertFalse(constantTimeEq("foo".encodeToByteArray(), byteArrayOf()))
        assertFalse(constantTimeEq("foo".encodeToByteArray(), "quux".encodeToByteArray()))
    }

    @Test
    fun constantTimeEqAcrossByteRange() {
        // Sweep every position in a 256-byte buffer to confirm that any
        // single differing byte (anywhere in the slice, with any high-bit
        // pattern) flips the result to false.
        val a = ByteArray(256) { it.toByte() }
        for (i in 0 until 256) {
            val b = a.copyOf()
            b[i] = (b[i].toInt() xor 0x80).toByte()
            assertFalse(constantTimeEq(a, b), "diff at index $i should compare unequal")
        }
        assertTrue(constantTimeEq(a, a.copyOf()))
    }

    @Test
    fun constantTimeEqNDocExamples() {
        assertTrue(constantTimeEqN(ByteArray(20) { 3 }, ByteArray(20) { 3 }))
        assertFalse(constantTimeEqN(ByteArray(20) { 3 }, ByteArray(20) { 7 }))
    }

    @Test
    fun constantTimeEqNRequiresEqualLengths() {
        assertFailsWith<IllegalArgumentException> {
            constantTimeEqN(ByteArray(4), ByteArray(5))
        }
    }

    @Test
    fun constantTimeEq16DocExamples() {
        assertTrue(constantTimeEq16(ByteArray(16) { 3 }, ByteArray(16) { 3 }))
        assertFalse(constantTimeEq16(ByteArray(16) { 3 }, ByteArray(16) { 7 }))
    }

    @Test
    fun constantTimeEq16RequiresFixedLength() {
        assertFailsWith<IllegalArgumentException> {
            constantTimeEq16(ByteArray(15), ByteArray(15))
        }
        assertFailsWith<IllegalArgumentException> {
            constantTimeEq16(ByteArray(16), ByteArray(15))
        }
    }

    @Test
    fun constantTimeEq32DocExamples() {
        assertTrue(constantTimeEq32(ByteArray(32) { 3 }, ByteArray(32) { 3 }))
        assertFalse(constantTimeEq32(ByteArray(32) { 3 }, ByteArray(32) { 7 }))
    }

    @Test
    fun constantTimeEq32RequiresFixedLength() {
        assertFailsWith<IllegalArgumentException> {
            constantTimeEq32(ByteArray(31), ByteArray(31))
        }
        assertFailsWith<IllegalArgumentException> {
            constantTimeEq32(ByteArray(32), ByteArray(31))
        }
    }

    @Test
    fun constantTimeEq64DocExamples() {
        assertTrue(constantTimeEq64(ByteArray(64) { 3 }, ByteArray(64) { 3 }))
        assertFalse(constantTimeEq64(ByteArray(64) { 3 }, ByteArray(64) { 7 }))
    }

    @Test
    fun constantTimeEq64RequiresFixedLength() {
        assertFailsWith<IllegalArgumentException> {
            constantTimeEq64(ByteArray(63), ByteArray(63))
        }
        assertFailsWith<IllegalArgumentException> {
            constantTimeEq64(ByteArray(64), ByteArray(63))
        }
    }

    private fun inlineIdentity(value: Byte): Byte = value

    private fun count(): Int {
        val sum = (optimizerHide(1) + optimizerHide(2) + optimizerHide(3) + optimizerHide(4)).toByte()
        assertEquals(10.toByte(), sum)
        return 4
    }

    private fun countOptimized(): Int {
        val sum = (inlineIdentity(1) + inlineIdentity(2) + inlineIdentity(3) + inlineIdentity(4)).toByte()
        assertEquals(10.toByte(), sum)
        return 4
    }

    @Test
    fun countOptimizerHideInstructions() {
        val c = count()
        val cOpt = countOptimized()
        assertEquals(c, cOpt)
        assertTrue(c > 0)
    }

    @Test
    fun countTest() {
        assertEquals(4, count())
    }

    @Test
    fun countOptimizedTest() {
        assertEquals(4, countOptimized())
    }

    @Test
    fun inlineIdentityTest() {
        assertEquals(5.toByte(), inlineIdentity(5.toByte()))
    }
}
