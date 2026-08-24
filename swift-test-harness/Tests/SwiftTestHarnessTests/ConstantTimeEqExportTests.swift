import Testing
import ConstantTimeEq
import ExportedKotlinPackages

@Suite struct ConstantTimeEqExportTests {
    private typealias KotlinByteArray = ExportedKotlinPackages.kotlin.ByteArray

    private func bytes(_ value: String) -> KotlinByteArray {
        constantTimeEqBytesFromUtf8(value: value)
    }

    private func repeated(_ size: Int32, _ value: Int8) -> KotlinByteArray {
        constantTimeEqRepeatedBytes(size: size, value: value)
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
        let left = constantTimeEqSequentialBytes(size: 256)
        for index in 0..<256 {
            let right = constantTimeEqSequentialBytesWithHighBitDifference(size: 256, index: Int32(index))
            #expect(Bool(!constantTimeEq(a: left, b: right)), "diff at index \(index) should compare unequal")
        }
        #expect(Bool(constantTimeEq(a: left, b: constantTimeEqSequentialBytes(size: 256))))
    }

    @Test func testConstantTimeEqNDocExamples() throws {
        #expect(Bool(constantTimeEqN(a: repeated(20, 3), b: repeated(20, 3))))
        #expect(Bool(!constantTimeEqN(a: repeated(20, 3), b: repeated(20, 7))))
    }

    @Test func testConstantTimeEqNRequiresEqualLengths() throws {
        #expect(Bool(constantTimeEqNRejectsUnequalLengths()))
    }

    @Test func testConstantTimeEq16DocExamples() throws {
        #expect(Bool(constantTimeEq16(a: repeated(16, 3), b: repeated(16, 3))))
        #expect(Bool(!constantTimeEq16(a: repeated(16, 3), b: repeated(16, 7))))
    }

    @Test func testConstantTimeEq16RequiresFixedLength() throws {
        #expect(Bool(constantTimeEq16RejectsWrongLength()))
    }

    @Test func testConstantTimeEq32DocExamples() throws {
        #expect(Bool(constantTimeEq32(a: repeated(32, 3), b: repeated(32, 3))))
        #expect(Bool(!constantTimeEq32(a: repeated(32, 3), b: repeated(32, 7))))
    }

    @Test func testConstantTimeEq32RequiresFixedLength() throws {
        #expect(Bool(constantTimeEq32RejectsWrongLength()))
    }

    @Test func testConstantTimeEq64DocExamples() throws {
        #expect(Bool(constantTimeEq64(a: repeated(64, 3), b: repeated(64, 3))))
        #expect(Bool(!constantTimeEq64(a: repeated(64, 3), b: repeated(64, 7))))
    }

    @Test func testConstantTimeEq64RequiresFixedLength() throws {
        #expect(Bool(constantTimeEq64RejectsWrongLength()))
    }

    private func assertTransitionParity(size: Int32, a: Int8, b: Int8, compare: (KotlinByteArray, KotlinByteArray) -> Bool) {
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

    @Test func testCountInstructionTransitionParity() throws {
        assertTransitionParity(size: 64, a: 65, b: 66) { left, right in
            constantTimeEq(a: left, b: right)
        }
        assertTransitionParity(size: 64, a: 0x55, b: Int8(bitPattern: 0xAA)) { left, right in
            constantTimeEq(a: left, b: right)
        }
    }

    @Test func testCountInstructionTransitionParityForFixedSizes() throws {
        for size in [16, 20, 24, 32, 48, 64] as [Int32] {
            assertTransitionParity(size: size, a: 65, b: 66) { left, right in
                constantTimeEqN(a: left, b: right)
            }
            assertTransitionParity(size: size, a: 0x55, b: Int8(bitPattern: 0xAA)) { left, right in
                constantTimeEqN(a: left, b: right)
            }
        }
    }
}
