/*
 Copyright 2017, Perihelios LLC

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package com.perihelios.experimental.intel8085dsl

import com.perihelios.experimental.intel8085dsl.exceptions.OverflowException
import com.perihelios.experimental.intel8085dsl.util.InvalidValue
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.asm
import static com.perihelios.experimental.intel8085dsl.util.TestUtil.combine

class OverflowTest extends Specification {
	@Shared
	List<Tuple2<String, InvalidValue>> d3Pairs = combine(InstructionOperandTypes.D3, [
		new InvalidValue(-1, "Operand value must be from 0 to 7; got -1 (0xf, 077, 0b1111)"),
		new InvalidValue(8, "Operand value must be from 0 to 7; got 8 (0x8, 010, 0b1000)"),
		new InvalidValue(-1_000_000, "Operand value must be from 0 to 7; got -1000000 " +
			"(0xf0bdc0, 04136700, 0b100001011110111000000)"),
		new InvalidValue(1_000_000, "Operand value must be from 0 to 7; got 1000000 " +
			"(0xf4240, 03641100, 0b11110100001001000000)")
	])

	@Shared
	List<Tuple2<String, InvalidValue>> d8Pairs = combine(InstructionOperandTypes.D8, [
		new InvalidValue(-129, "Operand value must be from -128 to 255; got -129 (0xf7f, 0577, 0b101111111)"),
		new InvalidValue(256, "Operand value must be from -128 to 255; got 256 (0x100, 0400, 0b100000000)"),
		new InvalidValue(-1_927, "Operand value must be from -128 to 255; got -1927 (0x879, 04171, 0b100001111001)"),
		new InvalidValue(31_140, "Operand value must be from -128 to 255; got 31140 " +
			"(0x79a4, 074644, 0b111100110100100)")
	])

	@Shared
	List<Tuple2<String, InvalidValue>> p8Pairs = combine(InstructionOperandTypes.P8, [
		new InvalidValue(-1, "Operand value must be from 0 to 255; got -1 (0xfff, 0777, 0b111111111)"),
		new InvalidValue(256, "Operand value must be from 0 to 255; got 256 (0x100, 0400, 0b100000000)"),
		new InvalidValue(-2_413, "Operand value must be from 0 to 255; got -2413 (0xf693, 073223, 0b1011010010011)"),
		new InvalidValue(8_334, "Operand value must be from 0 to 255; got 8334 (0x208e, 020216, 0b10000010001110)")
	])

	@Shared
	List<Tuple2<String, InvalidValue>> a16Pairs = combine(InstructionOperandTypes.A16, [
		new InvalidValue(-1, "Operand value must be from 0 to 65535; got -1 (0xfffff, 0777777, 0b11111111111111111)"),
		new InvalidValue(65536, "Operand value must be from 0 to 65535; got 65536" +
			" (0x10000, 0200000, 0b10000000000000000)"),
		new InvalidValue(-1_231_015_361, "Operand value must be from 0 to 65535; got -1231015361 " +
			"(0xb6a0323f, 066650031077, 0b10110110101000000011001000111111)"),
		new InvalidValue(260_055, "Operand value must be from 0 to 65535; got 260055 " +
			"(0x3f7d7, 0773727, 0b111111011111010111)")
	])

	@Shared
	List<Tuple2<String, InvalidValue>> regm8d8Pairs = combine(InstructionOperandTypes.REGM8_D8, [
		new InvalidValue(-129, "Operand value must be from -128 to 255; got -129 (0xf7f, 0577, 0b101111111)"),
		new InvalidValue(256, "Operand value must be from -128 to 255; got 256 (0x100, 0400, 0b100000000)"),
		new InvalidValue(-29_316, "Operand value must be from -128 to 255; got -29316 " +
			"(0x8d7c, 0706574, 0b1000110101111100)"),
		new InvalidValue(2_014, "Operand value must be from -128 to 255; got 2014 (0x7de, 03736, 0b11111011110)")
	])

	@Shared
	List<Tuple2<String, InvalidValue>> reg16d16Pairs = combine(InstructionOperandTypes.REG16_D16, [
		new InvalidValue(-32769, "Operand value must be from -32768 to 65535; got -32769 " +
			"(0xf7fff, 0677777, 0b10111111111111111)"),
		new InvalidValue(65536, "Operand value must be from -32768 to 65535; got 65536 " +
			"(0x10000, 0200000, 0b10000000000000000)"),
		new InvalidValue(-2_560_223_872_285L, "Operand value must be from -32768 to 65535; got -2560223872285 " +
			"(0xdabe6c3fae3, 0732574660775343, 0b1011010101111100110110000111111101011100011)"),
		new InvalidValue(2_560_223_872_285L, "Operand value must be from -32768 to 65535; got 2560223872285 " +
			"(0x254193c051d, 045203117002435, 0b100101010000011001001111000000010100011101)")
	])

	@Unroll
	"#d3inst #invalid.value"() {
		when:
			asm {
				"$d3inst"(invalid.value)
			}

		then:
			OverflowException expected = thrown()
			expected.message == invalid.message

		where:
			d3inst << d3Pairs*.first
			invalid << d3Pairs*.second
	}

	@Unroll
	"#d8inst #invalid.value"() {
		when:
			asm {
				"$d8inst"(invalid.value)
			}

		then:
			OverflowException expected = thrown()
			expected.message == invalid.message

		where:
			d8inst << d8Pairs*.first
			invalid << d8Pairs*.second
	}

	@Unroll
	"#p8inst #invalid.value"() {
		when:
			asm {
				"$p8inst"(invalid.value)
			}

		then:
			OverflowException expected = thrown()
			expected.message == invalid.message

		where:
			p8inst << p8Pairs*.first
			invalid << p8Pairs*.second
	}

	@Unroll
	"#a16inst #invalid.value"() {
		when:
			asm {
				"$a16inst"(invalid.value)
			}

		then:
			OverflowException expected = thrown()
			expected.message == invalid.message

		where:
			a16inst << a16Pairs*.first
			invalid << a16Pairs*.second
	}

	@Unroll
	"#regm8d8inst A, #invalid.value"() {
		when:
			asm {
				"$regm8d8inst"(A, invalid.value)
			}

		then:
			OverflowException expected = thrown()
			expected.message == invalid.message

		where:
			regm8d8inst << regm8d8Pairs*.first
			invalid << regm8d8Pairs*.second
	}

	@Unroll
	"#reg16d16inst H, #invalid.value"() {
		when:
			asm {
				"$reg16d16inst"(H, invalid.value)
			}

		then:
			OverflowException expected = thrown()
			expected.message == invalid.message

		where:
			reg16d16inst << reg16d16Pairs*.first
			invalid << reg16d16Pairs*.second
	}
}
