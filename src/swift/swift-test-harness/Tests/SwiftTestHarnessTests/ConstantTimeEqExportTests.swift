import Testing
import ConstantTimeEq
import ExportedKotlinPackages

@Suite struct ConstantTimeEqExportTests {
    private typealias KotlinByteArray = ExportedKotlinPackages.kotlin.ByteArray

    private func bytes(_ value: String) -> KotlinByteArray {
        let utf8 = Array(value.utf8)
        let array = KotlinByteArray(size: Int32(utf8.count))
        for (index, byte) in utf8.enumerated() {
            array[Int32(index)] = Int8(bitPattern: byte)
        }
        return array
    }

    private func repeated(_ size: Int32, _ value: Int8) -> KotlinByteArray {
        let array = KotlinByteArray(size: size)
        for index in 0..<size {
            array[index] = value
        }
        return array
    }

    private func sequential(_ size: Int32) -> KotlinByteArray {
        let array = KotlinByteArray(size: size)
        for index in 0..<size {
            array[index] = Int8(truncatingIfNeeded: index)
        }
        return array
    }

    private func sequentialWithHighBitDifference(_ size: Int32, index: Int32) -> KotlinByteArray {
        let array = sequential(size)
        array[index] = Int8(bitPattern: UInt8(bitPattern: array[index]) ^ 0x80)
        return array
    }

    @Test func testConstantTimeEqDocExamples() throws {
        #expect(Bool(constantTimeEq(a: bytes("foo"), b: bytes("foo"))))
        #expect(Bool(!constantTimeEq(a: bytes("foo"), b: bytes("bar"))))
        #expect(Bool(!constantTimeEq(a: bytes("bar"), b: bytes("baz"))))
        #expect(Bool(constantTimeEq(a: bytes(""), b: bytes(""))))

        #expect(Bool(!constantTimeEq(a: bytes("foo"), b: bytes(""))))
        #expect(Bool(!constantTimeEq(a: bytes("foo"), b: bytes("quux"))))
    }

    @Test func testConstantTimeEqAcrossByteRange() throws {
        let left = sequential(256)
        for index in 0..<256 {
            let right = sequentialWithHighBitDifference(256, index: Int32(index))
            #expect(Bool(!constantTimeEq(a: left, b: right)), "diff at index \(index) should compare unequal")
        }
        #expect(Bool(constantTimeEq(a: left, b: sequential(256))))
    }

    @Test func testConstantTimeEqNDocExamples() throws {
        #expect(Bool(constantTimeEqN(a: repeated(20, 3), b: repeated(20, 3))))
        #expect(Bool(!constantTimeEqN(a: repeated(20, 3), b: repeated(20, 7))))
    }

    @Test func testConstantTimeEq16DocExamples() throws {
        #expect(Bool(constantTimeEq16(a: repeated(16, 3), b: repeated(16, 3))))
        #expect(Bool(!constantTimeEq16(a: repeated(16, 3), b: repeated(16, 7))))
    }

    @Test func testConstantTimeEq32DocExamples() throws {
        #expect(Bool(constantTimeEq32(a: repeated(32, 3), b: repeated(32, 3))))
        #expect(Bool(!constantTimeEq32(a: repeated(32, 3), b: repeated(32, 7))))
    }

    @Test func testConstantTimeEq64DocExamples() throws {
        #expect(Bool(constantTimeEq64(a: repeated(64, 3), b: repeated(64, 3))))
        #expect(Bool(!constantTimeEq64(a: repeated(64, 3), b: repeated(64, 7))))
    }

    private func assertEqualitySweep(size: Int32, a: Int8, b: Int8, compare: (KotlinByteArray, KotlinByteArray) -> Bool) {
        let left = repeated(size, a)

        let fromStart = repeated(size, b)
        for index in 0..<(Int(size) - 1) {
            fromStart[Int32(index)] = a
            #expect(Bool(!compare(left, fromStart)))
        }
        fromStart[size - 1] = a
        #expect(Bool(compare(left, fromStart)))

        let fromEnd = repeated(size, b)
        for matched in 1..<Int(size) {
            fromEnd[size - Int32(matched)] = a
            #expect(Bool(!compare(left, fromEnd)))
        }
        fromEnd[0] = a
        #expect(Bool(compare(left, fromEnd)))
    }

    @Test func testSweepEqualityAcrossAllPrefixSuffixLengths() throws {
        assertEqualitySweep(size: 64, a: 65, b: 66) { left, right in
            constantTimeEq(a: left, b: right)
        }
        assertEqualitySweep(size: 64, a: 0x55, b: Int8(bitPattern: 0xAA)) { left, right in
            constantTimeEq(a: left, b: right)
        }
    }

    @Test func testSweepEqualityForFixedSizes() throws {
        for size in [16, 20, 24, 32, 48, 64] as [Int32] {
            assertEqualitySweep(size: size, a: 65, b: 66) { left, right in
                constantTimeEqN(a: left, b: right)
            }
            assertEqualitySweep(size: size, a: 0x55, b: Int8(bitPattern: 0xAA)) { left, right in
                constantTimeEqN(a: left, b: right)
            }
        }
    }
}
