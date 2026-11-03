package io.github.kotlinmania.constanttimeeq

// ByteArray construction helpers for the exported Swift API test harness.
fun constantTimeEqBytesFromUtf8(value: String): ByteArray = value.encodeToByteArray()

fun constantTimeEqRepeatedBytes(size: Int, value: Byte): ByteArray {
    require(size >= 0)
    return ByteArray(size) { value }
}

fun constantTimeEqSequentialBytes(size: Int): ByteArray {
    require(size >= 0)
    return ByteArray(size) { it.toByte() }
}

fun constantTimeEqSequentialBytesWithHighBitDifference(size: Int, index: Int): ByteArray {
    require(index in 0 until size)
    val bytes = constantTimeEqSequentialBytes(size)
    bytes[index] = (bytes[index].toInt() xor 0x80).toByte()
    return bytes
}

fun constantTimeEqNRejectsUnequalLengths(): Boolean =
    runCatching {
        constantTimeEqN(ByteArray(4), ByteArray(5))
    }.exceptionOrNull() is IllegalArgumentException

fun constantTimeEq16RejectsWrongLength(): Boolean =
    runCatching {
        constantTimeEq16(ByteArray(15), ByteArray(15))
    }.exceptionOrNull() is IllegalArgumentException &&
        runCatching {
            constantTimeEq16(ByteArray(16), ByteArray(15))
        }.exceptionOrNull() is IllegalArgumentException

fun constantTimeEq32RejectsWrongLength(): Boolean =
    runCatching {
        constantTimeEq32(ByteArray(31), ByteArray(31))
    }.exceptionOrNull() is IllegalArgumentException &&
        runCatching {
            constantTimeEq32(ByteArray(32), ByteArray(31))
        }.exceptionOrNull() is IllegalArgumentException

fun constantTimeEq64RejectsWrongLength(): Boolean =
    runCatching {
        constantTimeEq64(ByteArray(63), ByteArray(63))
    }.exceptionOrNull() is IllegalArgumentException &&
        runCatching {
            constantTimeEq64(ByteArray(64), ByteArray(63))
        }.exceptionOrNull() is IllegalArgumentException
