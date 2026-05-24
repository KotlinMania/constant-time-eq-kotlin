import XCTest
import ConstantTimeEq
import ExportedKotlinPackages

final class ConstantTimeEqExportTests: XCTestCase {
    private typealias KotlinByteArray = ExportedKotlinPackages.kotlin.ByteArray

    private func bytes(_ value: String) -> KotlinByteArray {
        constantTimeEqBytesFromUtf8(value: value)
    }

    private func repeated(_ size: Int32, _ value: Int8) -> KotlinByteArray {
        constantTimeEqRepeatedBytes(size: size, value: value)
    }

    func testConstantTimeEqDocExamples() throws {
        XCTAssertTrue(constantTimeEq(a: bytes("foo"), b: bytes("foo")))
        XCTAssertFalse(constantTimeEq(a: bytes("foo"), b: bytes("bar")))
        XCTAssertFalse(constantTimeEq(a: bytes("bar"), b: bytes("baz")))
        XCTAssertTrue(constantTimeEq(a: bytes(""), b: bytes("")))

        XCTAssertFalse(constantTimeEq(a: bytes("foo"), b: bytes("")))
        XCTAssertFalse(constantTimeEq(a: bytes("foo"), b: bytes("quux")))
    }

    func testConstantTimeEqAcrossByteRange() throws {
        let left = constantTimeEqSequentialBytes(size: 256)
        for index in 0..<256 {
            let right = constantTimeEqSequentialBytesWithHighBitDifference(size: 256, index: Int32(index))
            XCTAssertFalse(constantTimeEq(a: left, b: right), "diff at index \(index) should compare unequal")
        }
        XCTAssertTrue(constantTimeEq(a: left, b: constantTimeEqSequentialBytes(size: 256)))
    }

    func testConstantTimeEqNDocExamples() throws {
        XCTAssertTrue(constantTimeEqN(a: repeated(20, 3), b: repeated(20, 3)))
        XCTAssertFalse(constantTimeEqN(a: repeated(20, 3), b: repeated(20, 7)))
    }

    func testConstantTimeEqNRequiresEqualLengths() throws {
        XCTAssertTrue(constantTimeEqNRejectsUnequalLengths())
    }

    func testConstantTimeEq16DocExamples() throws {
        XCTAssertTrue(constantTimeEq16(a: repeated(16, 3), b: repeated(16, 3)))
        XCTAssertFalse(constantTimeEq16(a: repeated(16, 3), b: repeated(16, 7)))
    }

    func testConstantTimeEq16RequiresFixedLength() throws {
        XCTAssertTrue(constantTimeEq16RejectsWrongLength())
    }

    func testConstantTimeEq32DocExamples() throws {
        XCTAssertTrue(constantTimeEq32(a: repeated(32, 3), b: repeated(32, 3)))
        XCTAssertFalse(constantTimeEq32(a: repeated(32, 3), b: repeated(32, 7)))
    }

    func testConstantTimeEq32RequiresFixedLength() throws {
        XCTAssertTrue(constantTimeEq32RejectsWrongLength())
    }

    func testConstantTimeEq64DocExamples() throws {
        XCTAssertTrue(constantTimeEq64(a: repeated(64, 3), b: repeated(64, 3)))
        XCTAssertFalse(constantTimeEq64(a: repeated(64, 3), b: repeated(64, 7)))
    }

    func testConstantTimeEq64RequiresFixedLength() throws {
        XCTAssertTrue(constantTimeEq64RejectsWrongLength())
    }

    private func assertTransitionParity(size: Int32, a: Int8, b: Int8, compare: (KotlinByteArray, KotlinByteArray) -> Bool) {
        let left = repeated(size, a)

        let fromStart = repeated(size, b)
        for index in 0..<(Int(size) - 1) {
            fromStart[Int32(index)] = a
            XCTAssertFalse(compare(left, fromStart))
        }
        fromStart[size - 1] = a
        XCTAssertTrue(compare(left, fromStart))

        let fromEnd = repeated(size, b)
        for matched in 1..<Int(size) {
            fromEnd[size - Int32(matched)] = a
            XCTAssertFalse(compare(left, fromEnd))
        }
        fromEnd[0] = a
        XCTAssertTrue(compare(left, fromEnd))
    }

    func testCountInstructionTransitionParity() throws {
        assertTransitionParity(size: 64, a: 65, b: 66) { left, right in
            constantTimeEq(a: left, b: right)
        }
        assertTransitionParity(size: 64, a: 0x55, b: Int8(bitPattern: 0xAA)) { left, right in
            constantTimeEq(a: left, b: right)
        }
    }

    func testCountInstructionTransitionParityForFixedSizes() throws {
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
