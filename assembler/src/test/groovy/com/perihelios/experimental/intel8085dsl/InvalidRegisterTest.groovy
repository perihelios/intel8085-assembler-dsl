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

import com.perihelios.experimental.intel8085dsl.exceptions.InvalidRegisterException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static Intel8085AssemblerDsl.asm
import static com.perihelios.experimental.intel8085dsl.Register.A
import static com.perihelios.experimental.intel8085dsl.Register.B
import static com.perihelios.experimental.intel8085dsl.Register.C
import static com.perihelios.experimental.intel8085dsl.Register.D
import static com.perihelios.experimental.intel8085dsl.Register.E
import static com.perihelios.experimental.intel8085dsl.Register.H
import static com.perihelios.experimental.intel8085dsl.Register.L
import static com.perihelios.experimental.intel8085dsl.Register.M
import static com.perihelios.experimental.intel8085dsl.Register.PSW
import static com.perihelios.experimental.intel8085dsl.Register.SP
import static com.perihelios.experimental.intel8085dsl.TestUtil.combine

class InvalidRegisterTest extends Specification{
	@Shared
	private List<Tuple2<String, Register>> invalidSingleRegisterInstructionCombos = combine(
		[
			"ADC",
			"ADD",
			"ANA",
			"CMP",
			"DCR",
			"INR",
			"ORA",
			"SBB",
			"SUB",
			"XRA"
		], EnumSet.complementOf(EnumSet.of(A, B, C, D, E, H, L, M))
	) +
		combine(["DCX", "INX", "DAD"], EnumSet.complementOf(EnumSet.of(B, D, H, SP))) +
		combine(["POP", "PUSH"], EnumSet.complementOf(EnumSet.of(B, D, H, PSW))) +
		combine(["LDAX", "STAX"], EnumSet.complementOf(EnumSet.of(B, D)))

	@Shared
	private List<Tuple2<String, Register>> invalidRegisterImmediateInstructionCombos =
		combine(["LXI"], EnumSet.complementOf(EnumSet.of(B, D, H, SP))) +
			combine(["MVI"], EnumSet.complementOf(EnumSet.of(A, B, C, D, E, H, L, M)))

	@Shared
	private List<Tuple2<String, Register>> invalidTwoRegisterInstructionCombos =
		combine(["MOV"], EnumSet.complementOf(EnumSet.of(A, B, C, D, E, H, L, M)))

	@Unroll
	"invalid register for instruction: #inst #reg"() {
		when:
			asm {
				"$inst"(reg)
			}

		then:
			InvalidRegisterException expected = thrown()
			expected.message == "Invalid register $reg for instruction"

		where:
			inst << invalidSingleRegisterInstructionCombos.collect { it[0] }
			reg << invalidSingleRegisterInstructionCombos.collect { it[1] }
	}

	@Unroll
	"invalid register for instruction: #inst #reg, #value"() {
		when:
			asm {
				"$inst"(reg, value)
			}

		then:
			InvalidRegisterException expected = thrown()
			expected.message == "Invalid register $reg for instruction"

		where:
			inst << invalidRegisterImmediateInstructionCombos.collect { it[0] }
			reg << invalidRegisterImmediateInstructionCombos.collect { it[1] }
			value << invalidRegisterImmediateInstructionCombos.collect { 0 }
	}

	@Unroll
	"invalid register for instruction: #inst #reg, B"() {
		when:
			asm {
				"$inst"(reg, B)
			}

		then:
			InvalidRegisterException expected = thrown()
			expected.message == "Invalid register $reg for instruction"

		where:
			inst << invalidTwoRegisterInstructionCombos.collect { it[0] }
			reg << invalidTwoRegisterInstructionCombos.collect { it[1] }
	}

	@Unroll
	"invalid register for instruction: #inst B, #reg"() {
		when:
			asm {
				"$inst"(B, reg)
			}

		then:
			InvalidRegisterException expected = thrown()
			expected.message == "Invalid register $reg for instruction"

		where:
			inst << invalidTwoRegisterInstructionCombos.collect { it[0] }
			reg << invalidTwoRegisterInstructionCombos.collect { it[1] }
	}

	def "MOV M, M is invalid"() {
		when:
			asm {
				MOV(M, M)
			}

		then:
			InvalidRegisterException expected = thrown()
			expected.message == "Source and destination cannot both be M"
	}
}
