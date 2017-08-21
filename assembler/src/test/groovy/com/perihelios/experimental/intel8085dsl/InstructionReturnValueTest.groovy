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

import spock.lang.Specification
import spock.lang.Unroll

import static com.perihelios.experimental.intel8085dsl.Intel8085AssemblerDsl.asm

class InstructionReturnValueTest extends Specification {
	@Unroll
	"#instNONE method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instNONE"().bytesUsed
			}

		then:
			bytesTaken == 1

		where:
			instNONE << InstructionOperandTypes.NONE
	}

	@Unroll
	"#instD3 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instD3"(0).bytesUsed
			}

		then:
			bytesTaken == 1

		where:
			instD3 << InstructionOperandTypes.D3
	}

	@Unroll
	"#instD8 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instD8"(0).bytesUsed
			}

		then:
			bytesTaken == 2

		where:
			instD8 << InstructionOperandTypes.D8
	}

	@Unroll
	"#instP8 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instP8"(0).bytesUsed
			}

		then:
			bytesTaken == 2

		where:
			instP8 << InstructionOperandTypes.P8
	}

	@Unroll
	"#instA16 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instA16"(0).bytesUsed
			}

		then:
			bytesTaken == 3

		where:
			instA16 << InstructionOperandTypes.A16
	}

	@Unroll
	"#instREGM8 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instREGM8"(A).bytesUsed
			}

		then:
			bytesTaken == 1

		where:
			instREGM8 << InstructionOperandTypes.REGM8
	}

	@Unroll
	"#instREG16 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instREG16"(B).bytesUsed
			}

		then:
			bytesTaken == 1

		where:
			instREG16 << InstructionOperandTypes.REG16
	}

	@Unroll
	"#instREGM8_D8 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instREGM8_D8"(A, 0).bytesUsed
			}

		then:
			bytesTaken == 2

		where:
			instREGM8_D8 << InstructionOperandTypes.REGM8_D8
	}

	@Unroll
	"#instREG16_D16 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instREG16_D16"(B, 0).bytesUsed
			}

		then:
			bytesTaken == 3

		where:
			instREG16_D16 << InstructionOperandTypes.REG16_D16
	}

	@Unroll
	"#instREGM8_REGM8 method returns correct value"() {
		setup:
			int bytesTaken = 0

		when:
			asm {
				bytesTaken = "$instREGM8_REGM8"(A, B).bytesUsed
			}

		then:
			bytesTaken == 1

		where:
			instREGM8_REGM8 << InstructionOperandTypes.REGM8_REGM8
	}
}
